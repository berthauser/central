package com.visus.central.application.usecase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.visus.central.domain.model.ComisionVenta;
import com.visus.central.domain.model.EstadoComision;
import com.visus.central.domain.model.Pago;
import com.visus.central.domain.model.ReglaComision;
import com.visus.central.domain.model.TipoCalculoComision;
import com.visus.central.domain.model.TramoComision;
import com.visus.central.domain.model.Vendedor;
import com.visus.central.domain.model.Venta;
import com.visus.central.domain.port.in.ComisionUseCase;
import com.visus.central.domain.port.in.ReglaComisionUseCase;
import com.visus.central.domain.port.out.ComisionVentaRepository;
import com.visus.central.domain.port.out.PagoRepository;
import com.visus.central.domain.port.out.VentaRepository;

@Service
@Transactional
public class ComisionUseCaseImpl implements ComisionUseCase {

	private final ComisionVentaRepository comisionRepository;
	private final PagoRepository pagoRepository;
	private final VentaRepository ventaRepository;
	private final ReglaComisionUseCase reglaComisionUseCase;

	public ComisionUseCaseImpl(ComisionVentaRepository comisionRepository, PagoRepository pagoRepository,
			VentaRepository ventaRepository, ReglaComisionUseCase reglaComisionUseCase) {
		this.comisionRepository = comisionRepository;
		this.pagoRepository = pagoRepository;
		this.ventaRepository = ventaRepository;
		this.reglaComisionUseCase = reglaComisionUseCase;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ComisionVenta> findAll() {
		return comisionRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ComisionVenta> findByVendedorId(Integer idVendedor) {
		return comisionRepository.findByVendedorId(idVendedor);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ComisionVenta> findByFechaBetween(LocalDate desde, LocalDate hasta) {
		return comisionRepository.findByFechaCalculoBetween(desde, hasta);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ComisionVenta> findByVentaId(Integer idVenta) {
		return comisionRepository.findByVentaId(idVenta);
	}

	@Override
	public ComisionVenta calcularComisionPorPago(Long idPago) {
		if (comisionRepository.existsByPagoId(idPago)) {
			throw new IllegalStateException("Ya existe una comisión calculada para el pago ID " + idPago);
		}

		Pago pago = pagoRepository.findById(idPago)
				.orElseThrow(() -> new IllegalArgumentException("Pago no encontrado: " + idPago));

		if (pago.getIdVenta() == null) {
			throw new IllegalArgumentException("El pago " + idPago + " no está asociado a ninguna venta");
		}

		Venta venta = ventaRepository.findById(pago.getIdVenta())
				.orElseThrow(() -> new IllegalArgumentException("Venta no encontrada: " + pago.getIdVenta()));

		Vendedor vendedor = venta.getVendedor();
		if (vendedor == null || vendedor.getId() == null) {
			throw new IllegalArgumentException("La venta " + pago.getIdVenta() + " no tiene vendedor asignado");
		}

		ReglaComision regla = reglaComisionUseCase.resolverReglaActiva(vendedor.getId());

		if (regla.getTipoCalculo() == TipoCalculoComision.ESCALONADO) {
			throw new UnsupportedOperationException(
					"Las reglas escalonadas se calculan por período. Use calcularComisionesPeriodo().");
		}

		BigDecimal base = calcularBase(regla, pago, venta);
		BigDecimal comisionCalculada = calcularComision(regla, base);

		ComisionVenta comision = new ComisionVenta();
		comision.setVendedor(vendedor);
		comision.setVenta(venta);
		comision.setIdPago(idPago);
		comision.setBaseComisionable(base);
		comision.setPorcentaje(regla.getValorCalculo());
		comision.setComisionBruta(comisionCalculada);
		comision.setAjustes(BigDecimal.ZERO);
		comision.setComisionFinal(comisionCalculada);
		comision.setFechaCalculo(LocalDate.now());
		comision.setEstado(EstadoComision.PENDIENTE);

		return comisionRepository.save(comision);
	}

	private BigDecimal calcularBase(ReglaComision regla, Pago pago, Venta venta) {
		BigDecimal base = switch (regla.getTipoBaseComisionable()) {
			case MONTO_PAGO -> pago.getMontoTotal();
			case TOTAL_VENTA -> venta.getTotal();
			case NETO_VENTA -> {
				BigDecimal desc = Boolean.TRUE.equals(regla.getIncluirDescuentos())
						? venta.getSubtotalPorcentaje()
						: BigDecimal.ZERO;
				yield venta.getTotal().subtract(desc);
			}
			case SUBTOTAL_VENTA -> venta.getSubtotal();
		};
		return base.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : base;
	}

	private BigDecimal calcularComision(ReglaComision regla, BigDecimal base) {
		if (regla.getTipoCalculo() == TipoCalculoComision.MONTO_FIJO) {
			return regla.getValorCalculo().setScale(2, RoundingMode.HALF_UP);
		}
		return base.multiply(regla.getValorCalculo())
				.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
	}

	@Override
	public List<ComisionVenta> calcularComisionesPendientes() {
		List<ComisionVenta> generadas = new ArrayList<>();
		List<Pago> pagosSinComision = pagoRepository.findAll().stream()
				.filter(p -> p.getIdVenta() != null)
				.filter(p -> p.getMontoTotal().compareTo(BigDecimal.ZERO) > 0)
				.filter(p -> !comisionRepository.existsByPagoId(p.getIdPago()))
				.toList();

		Map<Integer, List<Pago>> porVenta = pagosSinComision.stream()
				.collect(Collectors.groupingBy(Pago::getIdVenta));

		for (Map.Entry<Integer, List<Pago>> entry : porVenta.entrySet()) {
			Integer idVenta = entry.getKey();
			try {
				Venta venta = ventaRepository.findById(idVenta)
						.orElse(null);
				if (venta == null || venta.getVendedor() == null)
					continue;

				ReglaComision regla = reglaComisionUseCase.resolverReglaActiva(venta.getVendedor().getId());

				if (regla.getTipoCalculo() == TipoCalculoComision.ESCALONADO) {
					continue;
				}

				for (Pago pago : entry.getValue()) {
					try {
						generadas.add(calcularComisionPorPago(pago.getIdPago()));
					} catch (Exception e) {
						// skip
					}
				}
			} catch (Exception e) {
				// skip
			}
		}
		return generadas;
	}

	@Override
	public List<ComisionVenta> calcularComisionesPeriodo(int mes, int anio) {
		LocalDate inicio = LocalDate.of(anio, mes, 1);
		LocalDate fin = inicio.withDayOfMonth(inicio.lengthOfMonth());

		List<Pago> pagosDelMes = pagoRepository.findByFechaBetween(inicio, fin).stream()
				.filter(p -> p.getIdVenta() != null)
				.filter(p -> p.getMontoTotal().compareTo(BigDecimal.ZERO) > 0)
				.toList();

		Map<Integer, List<Pago>> porVenta = pagosDelMes.stream()
				.collect(Collectors.groupingBy(Pago::getIdVenta));

		List<ComisionVenta> generadas = new ArrayList<>();

		for (Map.Entry<Integer, List<Pago>> entry : porVenta.entrySet()) {
			try {
				Venta venta = ventaRepository.findById(entry.getKey()).orElse(null);
				if (venta == null || venta.getVendedor() == null)
					continue;

				ReglaComision regla = reglaComisionUseCase.resolverReglaActiva(venta.getVendedor().getId());

				if (regla.getTipoCalculo() != TipoCalculoComision.ESCALONADO) {
					for (Pago pago : entry.getValue()) {
						if (!comisionRepository.existsByPagoId(pago.getIdPago())) {
							generadas.add(calcularComisionPorPago(pago.getIdPago()));
						}
					}
					continue;
				}

				List<TramoComision> tramos = regla.getTramos();
				if (tramos == null || tramos.isEmpty())
					continue;

				tramos.sort(Comparator.comparing(TramoComision::getDesde));

				BigDecimal baseTotalAgregada = entry.getValue().stream()
						.map(p -> calcularBase(regla, p, venta))
						.reduce(BigDecimal.ZERO, BigDecimal::add);

				BigDecimal restante = baseTotalAgregada;
				for (TramoComision tramo : tramos) {
					if (restante.compareTo(BigDecimal.ZERO) <= 0)
						break;

					BigDecimal tope = tramo.getHasta();
					BigDecimal baseTramo;
					if (tope == null || restante.compareTo(tope.subtract(tramo.getDesde())) <= 0) {
						baseTramo = restante;
					} else {
						baseTramo = tope.subtract(tramo.getDesde());
					}
					baseTramo = baseTramo.max(BigDecimal.ZERO);
					if (baseTramo.compareTo(BigDecimal.ZERO) <= 0)
						continue;

					BigDecimal comisionTramo = baseTramo.multiply(tramo.getPorcentaje())
							.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

					ComisionVenta cv = new ComisionVenta();
					cv.setVendedor(venta.getVendedor());
					cv.setVenta(venta);
					cv.setIdPago(null);
					cv.setBaseComisionable(baseTramo);
					cv.setPorcentaje(tramo.getPorcentaje());
					cv.setComisionBruta(comisionTramo);
					cv.setAjustes(BigDecimal.ZERO);
					cv.setComisionFinal(comisionTramo);
					cv.setFechaCalculo(LocalDate.now());
					cv.setEstado(EstadoComision.PENDIENTE);
					cv.setObservaciones("Período " + mes + "/" + anio + " | Tramo $" + tramo.getDesde()
							+ (tope != null ? " a $" + tope : "+"));

					generadas.add(comisionRepository.save(cv));

					restante = restante.subtract(baseTramo);
				}
			} catch (Exception e) {
				// skip
			}
		}
		return generadas;
	}

	@Override
	public void anularComision(Long idComision, String motivo) {
		ComisionVenta comision = comisionRepository.findById(idComision)
				.orElseThrow(() -> new IllegalArgumentException("Comisión no encontrada: " + idComision));

		if (comision.getEstado() == EstadoComision.PAGADA) {
			throw new IllegalStateException("No se puede anular una comisión ya pagada");
		}

		comision.setEstado(EstadoComision.ANULADA);
		comision.setObservaciones(motivo);
		comisionRepository.save(comision);
	}
}
