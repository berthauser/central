package com.visus.central.domain.model;

import java.math.BigDecimal;

public class ItemPago {

	private Long id;
	private Long idVenta;
	private TipoPago tipoPago;
	private Coeficiente coeficiente;
	private BigDecimal monto;
	private BigDecimal coeficienteAplicado;
	private Short cantidadCuotas;
	private Long idCoeficiente;
	private Integer idTipoPago;

	public ItemPago() {

	}

	// Constructor completo
	public ItemPago(TipoPago tipoPago, BigDecimal monto, Coeficiente coeficiente) {
		this.tipoPago = tipoPago;
		this.monto = monto;
		this.coeficiente = coeficiente;
		if (coeficiente != null) {
			this.coeficienteAplicado = coeficiente.getCoeficiente();
			this.cantidadCuotas = coeficiente.getCuotas();
			this.idCoeficiente = Long.valueOf(coeficiente.getId());
		} else {
			this.coeficienteAplicado = BigDecimal.ONE;
			this.cantidadCuotas = 1;
			this.idCoeficiente = null;
		}
		this.idTipoPago = tipoPago.getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdVenta() {
		return idVenta;
	}

	public void setIdVenta(Long idVenta) {
		this.idVenta = idVenta;
	}

	public TipoPago getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(TipoPago tipoPago) {
		this.tipoPago = tipoPago;
	}

	public Coeficiente getCoeficiente() {
		return coeficiente;
	}

	public void setCoeficiente(Coeficiente coeficiente) {
		this.coeficiente = coeficiente;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public BigDecimal getCoeficienteAplicado() {
		return coeficienteAplicado;
	}

	public void setCoeficienteAplicado(BigDecimal coeficienteAplicado) {
		this.coeficienteAplicado = coeficienteAplicado;
	}

	public Short getCantidadCuotas() {
		return cantidadCuotas;
	}

	public void setCantidadCuotas(Short cantidadCuotas) {
		this.cantidadCuotas = cantidadCuotas;
	}

	public Long getIdCoeficiente() {
		return idCoeficiente;
	}

	public void setIdCoeficiente(Long idCoeficiente) {
		this.idCoeficiente = idCoeficiente;
	}

	public Integer getIdTipoPago() {
		return idTipoPago;
	}

	public void setIdTipoPago(Integer idTipoPago) {
		this.idTipoPago = idTipoPago;
	}

}
