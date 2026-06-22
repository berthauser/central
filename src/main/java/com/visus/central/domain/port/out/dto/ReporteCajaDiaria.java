package com.visus.central.domain.port.out.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ReporteCajaDiaria(
		Integer idCaja, 
		LocalDate fechaApertura, 
		LocalTime horaApertura, 
		LocalDate fechaCierre,
		LocalTime horaCierre, 
		BigDecimal saldoInicial, 
		BigDecimal totalIngresos, 
		BigDecimal totalEgresos,
		BigDecimal saldoFinal, 
		List<MovimientoReporte> movimientos
) {

}
