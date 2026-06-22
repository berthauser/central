package com.visus.central.domain.model;

/**
 * Cabecera del evento de pago del cliente
 * @idVenta: Referencia opcional a la venta principal
 * @montoTotal: Dinero físico entregado por el cliente
 * @idTipoPago: Medio con el que pagó (Efectivo, Cheque, etc.) 
 */

import java.math.BigDecimal;
import java.time.LocalDate;

public class Pago {

	private Long idPago;
	private Integer idCliente;
	private Integer idVenta;
	private LocalDate fecha;
	private BigDecimal montoTotal;
	private Integer idTipoPago;
	private Boolean aplicado;
	private String observaciones;

	public Long getIdPago() {
		return idPago;
	}

	public void setIdPago(Long idPago) {
		this.idPago = idPago;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public Integer getIdVenta() {
		return idVenta;
	}

	public void setIdVenta(Integer idVenta) {
		this.idVenta = idVenta;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public BigDecimal getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(BigDecimal montoTotal) {
		this.montoTotal = montoTotal;
	}

	public Integer getIdTipoPago() {
		return idTipoPago;
	}

	public void setIdTipoPago(Integer idTipoPago) {
		this.idTipoPago = idTipoPago;
	}

	public Boolean getAplicado() {
		return aplicado;
	}

	public void setAplicado(Boolean aplicado) {
		this.aplicado = aplicado;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

}
