package com.visus.central.application.usecase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.visus.central.domain.model.Devolucion;
import com.visus.central.domain.model.EstadoComision;
import com.visus.central.domain.model.EstadoVenta;
import com.visus.central.domain.model.MovimientoCaja;
import com.visus.central.domain.model.OrigenMovimiento;
import com.visus.central.domain.model.PlanPago;
import com.visus.central.domain.model.TipoOperacionDevolucion;
import com.visus.central.domain.model.TipoPago;
import com.visus.central.domain.model.Venta;
import com.visus.central.domain.port.in.DevolucionUseCase;
import com.visus.central.domain.port.out.ArticuloRepository;
import com.visus.central.domain.port.out.CajaRepository;
import com.visus.central.domain.port.out.ComisionVentaRepository;
import com.visus.central.domain.port.out.DevolucionRepository;
import com.visus.central.domain.port.out.MovimientoCajaRepository;
import com.visus.central.domain.port.out.PlanPagoRepository;
import com.visus.central.domain.port.out.TipoPagoRepository;
import com.visus.central.domain.port.out.VentaRepository;

@Service
@Transactional
public class DevolucionUseCaseImpl implements DevolucionUseCase {

	private final VentaRepository ventaRepository;
	private final ArticuloRepository articuloRepository;
	private final DevolucionRepository devolucionRepository;
	private final PlanPagoRepository planPagoRepository;
	private final CajaRepository cajaRepository;
	private final MovimientoCajaRepository movimientoCajaRepository;
	private final ComisionVentaRepository comisionRepository;
	private final TipoPagoRepository tipoPagoRepository;

	public DevolucionUseCaseImpl(VentaRepository ventaRepository, ArticuloRepository articuloRepository,
			DevolucionRepository devolucionRepository, PlanPagoRepository planPagoRepository,
			CajaRepository cajaRepository,
			MovimientoCajaRepository movimientoCajaRepository, ComisionVentaRepository comisionRepository,
			TipoPagoRepository tipoPagoRepository) {
		this.ventaRepository = ventaRepository;
		this.articuloRepository = articuloRepository;
		this.devolucionRepository = devolucionRepository;
		this.planPagoRepository = planPagoRepository;
		this.cajaRepository = cajaRepository;
		this.movimientoCajaRepository = movimientoCajaRepository;
		this.comisionRepository = comisionRepository;
		this.tipoPagoRepository = tipoPagoRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Venta> buscarVentasPorCliente(Integer idCliente) {
		return ventaRepository.findByClienteId(idCliente).stream()
				.filter(v -> v.getEstado() == EstadoVenta.COBRADA)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Venta> buscarVentasPorCodigoBarra(String codigoBarra) {
		return ventaRepository.findByArticuloCodigoBarra(codigoBarra).stream()
				.filter(v -> v.getEstado() == EstadoVenta.COBRADA)
				.toList();
	}

	@Override
	public Devolucion procesarDevolucion(Integer idVenta, Integer idArticulo, Integer cantidad,
			BigDecimal montoDevuelto, boolean malEstado,
			String observaciones, Integer idUsuario) {

		Venta venta = ventaRepository.findById(idVenta)
				.orElseThrow(() -> new IllegalArgumentException("Venta no encontrada: " + idVenta));

		// Calcular proporción de efectivo (afecta_caja = TRUE) sobre el total pagado
		List<PlanPago> cuotas = planPagoRepository.findByVentaId(venta.getId());
		BigDecimal totalCash = BigDecimal.ZERO;
		BigDecimal totalVenta = BigDecimal.ZERO;
		for (PlanPago cuota : cuotas) {
			BigDecimal monto = cuota.getMontoPagado() != null ? cuota.getMontoPagado() : BigDecimal.ZERO;
			TipoPago tp = cuota.getIdTipoPago() != null
					? tipoPagoRepository.findById(cuota.getIdTipoPago()).orElse(null)
					: null;
			if (tp != null && Boolean.TRUE.equals(tp.getAfecta_caja())) {
				totalCash = totalCash.add(monto);
			}
			totalVenta = totalVenta.add(monto);
		}
		if (totalVenta.compareTo(BigDecimal.ZERO) <= 0) {
			totalVenta = montoDevuelto;
		}
		BigDecimal cashPortion = montoDevuelto.multiply(totalCash).divide(totalVenta, 2, RoundingMode.HALF_UP);

		// Generar Egreso de Caja solo si hay porción en efectivo
		Integer idMovimientoCaja = null;
		if (cashPortion.compareTo(BigDecimal.ZERO) > 0) {
			var cajaOpt = cajaRepository.findCajaAbierta();
			if (cajaOpt.isEmpty()) {
				throw new IllegalStateException("No hay una caja abierta para registrar el egreso");
			}
			MovimientoCaja mc = new MovimientoCaja();
			mc.setCaja(cajaOpt.get());
			mc.setFecha(LocalDate.now());
			mc.setHora(LocalTime.now());
			mc.setDebe(BigDecimal.ZERO);
			mc.setHaber(cashPortion);
			mc.setDescripcion("Devolución venta #" + venta.getNumeroComprobante() + " - " + observaciones);
			mc.setOrigen(OrigenMovimiento.AUTOMATICO);
			mc = movimientoCajaRepository.save(mc);
			idMovimientoCaja = mc.getId();
		}

		// Actualizar stock / estado del artículo
		var articuloOpt = articuloRepository.findById(idArticulo);
		articuloOpt.ifPresent(a -> {
			if (malEstado) {
				a.setEstado(com.visus.central.infraestructure.persistence.entity.JpaArticuloEntity.Estado.Baja);
			} else {
				a.setStock(a.getStock() + cantidad);
			}
			articuloRepository.save(a);
		});

		// Reversión de comisiones
		var comisiones = comisionRepository.findByVentaId(idVenta);
		for (var cv : comisiones) {
			switch (cv.getEstado()) {
				case PENDIENTE -> {
					cv.setEstado(EstadoComision.ANULADA);
					cv.setObservaciones("Devolución venta #" + idVenta);
				}
				case PAGADA -> {
					BigDecimal nuevoAjuste = cv.getAjustes().subtract(montoDevuelto);
					cv.setAjustes(nuevoAjuste);
					cv.setComisionFinal(cv.getComisionBruta().add(nuevoAjuste));
					cv.setEstado(EstadoComision.AJUSTADA);
					cv.setObservaciones("Devolución venta #" + idVenta);
				}
				default -> {}
			}
			comisionRepository.save(cv);
		}

		// Guardar registro de devolución (solo efectivo)
		Devolucion dev = new Devolucion(idVenta, idArticulo, cantidad, cashPortion,
				TipoOperacionDevolucion.EGRESO_CAJA, malEstado,
				observaciones, idUsuario);
		dev.setIdMovimientoCaja(idMovimientoCaja);
		dev = devolucionRepository.save(dev);

		ventaRepository.actualizarEstadoVenta(idVenta, EstadoVenta.DEVUELTA);

		return dev;
	}
}
