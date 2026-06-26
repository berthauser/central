package com.visus.central.application.usecase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visus.central.domain.model.Caja;
import com.visus.central.domain.model.EstadoCaja;
import com.visus.central.domain.model.MovimientoCaja;
import com.visus.central.domain.model.OrigenMovimiento;
import com.visus.central.domain.model.TipoPago;
import com.visus.central.domain.port.in.CajaUseCase;
import com.visus.central.domain.port.in.MovimientoManualRec;
import com.visus.central.domain.port.out.CajaRepository;
import com.visus.central.domain.port.out.MovimientoCajaRepository;
import com.visus.central.domain.port.out.TipoPagoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CajaUseCaseImpl implements CajaUseCase {

	@Autowired
	private CajaRepository cajaRepo;

	@Autowired
	private MovimientoCajaRepository movimientoRepo;

	@Autowired
	private TipoPagoRepository tipoPagoRepo; // asumimos que existe

	@Override
	public Caja abrirCaja(BigDecimal saldoInicial, Integer idUsuarioApertura, String observaciones) {
		if (cajaRepo.findCajaAbierta().isPresent()) {
			throw new IllegalStateException("Ya existe una caja abierta. Ciérrela antes de abrir una nueva.");
		}
		Caja nueva = new Caja();
		nueva.setFechaApertura(LocalDate.now());
		nueva.setHoraApertura(LocalTime.now());
		nueva.setSaldoInicial(saldoInicial);
		nueva.setEstado(EstadoCaja.ABIERTA);
		nueva.setIdUsuarioApertura(idUsuarioApertura);
		nueva.setObservaciones(observaciones);
		return cajaRepo.save(nueva);
	}

	@Override
	public void cerrarCaja(Integer idCaja, BigDecimal saldoRealCierre, Integer idUsuarioCierre, String observaciones) {
		Caja caja = cajaRepo.findById(idCaja).orElseThrow(() -> new IllegalArgumentException("Caja no encontrada"));
		caja.cerrar(saldoRealCierre, idUsuarioCierre, observaciones);
		cajaRepo.save(caja);
	}

	@Override
	public Caja obtenerCajaActual() {
		return cajaRepo.findCajaAbierta().orElse(null);
	}

	@Override
	public List<MovimientoCaja> obtenerMovimientosDeCaja(Integer idCaja, LocalDate fecha) {
		Caja caja = cajaRepo.findById(idCaja).orElseThrow(() -> new IllegalArgumentException("Caja no encontrada"));
		if (fecha != null) {
			return movimientoRepo.findByCajaAndFecha(caja, fecha);
		} else {
			return movimientoRepo.findByCaja(caja);
		}
	}

	@Override
	public MovimientoCaja registrarMovimientoManual(MovimientoManualRec command) {
		Caja caja = cajaRepo.findById(command.idCaja())
				.orElseThrow(() -> new IllegalArgumentException("Caja no existe"));
		if (!caja.estaAbierta()) {
			throw new IllegalStateException("La caja está cerrada. No se pueden registrar movimientos.");
		}

		MovimientoCaja mov = new MovimientoCaja();
		mov.setCaja(caja);
		mov.setFecha(LocalDate.now());
		mov.setHora(LocalTime.now());
		mov.setDescripcion(command.descripcion());
		mov.setOrigen(OrigenMovimiento.MANUAL);

		if (Boolean.TRUE.equals(command.esIngreso())) {
			mov.setDebe(command.monto());
			mov.setHaber(BigDecimal.ZERO);
		} else {
			mov.setDebe(BigDecimal.ZERO);
			mov.setHaber(command.monto());
		}

		// Asignar tipo de pago si se proporciona
		if (command.idTipoPago() != null) {
			TipoPago tp = tipoPagoRepo.findById(command.idTipoPago()).orElse(null);
			mov.setTipoPago(tp);
		}
		// Asignar comprobante si se proporciona (requiere repositorio de comprobantes)
		// if (command.idComprobante() != null) { ... }

		return movimientoRepo.save(mov);
	}

	@Override
	public BigDecimal calcularSaldoActual(Caja caja) {
		List<MovimientoCaja> movimientos = movimientoRepo.findByCaja(caja);
		BigDecimal ingresos = movimientos.stream().map(MovimientoCaja::getDebe).reduce(BigDecimal.ZERO,
				BigDecimal::add);
		BigDecimal egresos = movimientos.stream().map(MovimientoCaja::getHaber).reduce(BigDecimal.ZERO,
				BigDecimal::add);
		return caja.getSaldoInicial().add(ingresos).subtract(egresos);
	}

	@Override
	public List<Caja> obtenerCajasCerradasPorRango(LocalDate fechaInicio, LocalDate fechaFin) {
		return cajaRepo.findCerradasPorRango(fechaInicio, fechaFin);
	}

	@Override
	public BigDecimal obtenerSaldoInicialSugerido() {
	    // Buscar todas las cajas ordenadas por fecha de cierre descendente, tomar la primera cerrada
	    List<Caja> todas = cajaRepo.findAll(); // necesitas agregar findAll() en CajaRepositoryPort si no existe
	    // Filtrar cerradas y ordenar por fechaCierre descendente
	    return todas.stream()
	            .filter(c -> !c.estaAbierta()) // cerradas
	            .max((c1, c2) -> {
	                if (c1.getFechaCierre() == null || c2.getFechaCierre() == null) return 0;
	                return c1.getFechaCierre().compareTo(c2.getFechaCierre());
	            })
	            .map(Caja::getSaldoRealCierre)
	            .orElse(BigDecimal.ZERO);
	}
}
