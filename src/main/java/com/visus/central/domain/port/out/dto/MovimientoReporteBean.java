package com.visus.central.domain.port.out.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class MovimientoReporteBean {

	private LocalDate fecha;
	private LocalTime hora;
	private String descripcion;
	private BigDecimal debe;
	private BigDecimal haber;
	private String origen;

	public MovimientoReporteBean(MovimientoReporte record) {
		this.fecha = record.fecha();
		this.hora = record.hora();
		this.descripcion = record.descripcion();
		this.debe = record.debe();
		this.haber = record.haber();
		this.origen = record.origen();
	}

	// Getters obligatorios para JasperReports
	public LocalDate getFecha() {
		return fecha;
	}

	public LocalTime getHora() {
		return hora;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public BigDecimal getDebe() {
		return debe;
	}

	public BigDecimal getHaber() {
		return haber;
	}

	public String getOrigen() {
		return origen;
	}
}
