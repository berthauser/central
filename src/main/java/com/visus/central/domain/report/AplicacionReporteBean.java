package com.visus.central.domain.report;

import java.math.BigDecimal;
import java.util.Date;

public class AplicacionReporteBean {

	private Date fechaPago;
	private BigDecimal montoPagado;
	private String formaPago;
	private String usuarioGestion;

	// Constructor
	public AplicacionReporteBean(Date fechaPago, BigDecimal montoPagado, String formaPago, String usuarioGestion) {
		this.fechaPago = fechaPago;
		this.montoPagado = montoPagado;
		this.formaPago = formaPago;
		this.usuarioGestion = usuarioGestion;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public BigDecimal getMontoPagado() {
		return montoPagado;
	}

	public void setMontoPagado(BigDecimal montoPagado) {
		this.montoPagado = montoPagado;
	}

	public String getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	public String getUsuarioGestion() {
		return usuarioGestion;
	}

	public void setUsuarioGestion(String usuarioGestion) {
		this.usuarioGestion = usuarioGestion;
	}

}
