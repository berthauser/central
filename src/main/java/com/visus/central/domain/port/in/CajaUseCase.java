package com.visus.central.domain.port.in;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.visus.central.domain.model.Caja;
import com.visus.central.domain.model.MovimientoCaja;

public interface CajaUseCase {

	Caja abrirCaja(BigDecimal saldoInicial, Integer idUsuarioApertura, String observaciones);

	void cerrarCaja(Integer idCaja, BigDecimal saldoRealCierre, Integer idUsuarioCierre, String observaciones);

	Caja obtenerCajaActual();

	List<MovimientoCaja> obtenerMovimientosDeCaja(Integer idCaja, LocalDate fecha);

	MovimientoCaja registrarMovimientoManual(MovimientoManualRec command);

	BigDecimal calcularSaldoActual(Caja caja);

	BigDecimal obtenerSaldoInicialSugerido();

	List<Caja> obtenerCajasCerradasPorRango(LocalDate fechaInicio, LocalDate fechaFin);

}
