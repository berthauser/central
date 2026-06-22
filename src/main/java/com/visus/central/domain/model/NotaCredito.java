package com.visus.central.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class NotaCredito {

	private Long id;
	private Integer idCliente;
	private BigDecimal monto;
	private LocalDate fecha;
	private String observaciones;
	private boolean consumido;

	public NotaCredito() {
	}

	public NotaCredito(Integer idCliente, BigDecimal monto, LocalDate fecha, String observaciones) {
		this.idCliente = idCliente;
		this.monto = monto;
		this.fecha = fecha;
		this.observaciones = observaciones;
		this.consumido = false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public boolean isConsumido() {
		return consumido;
	}

	public void setConsumido(boolean consumido) {
		this.consumido = consumido;
	}
}
