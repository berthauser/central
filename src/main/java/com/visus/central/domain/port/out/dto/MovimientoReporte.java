package com.visus.central.domain.port.out.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record MovimientoReporte(
		LocalDate fecha,
	    LocalTime hora,
	    String descripcion,
	    BigDecimal debe,
	    BigDecimal haber,
	    String origen) {

}
