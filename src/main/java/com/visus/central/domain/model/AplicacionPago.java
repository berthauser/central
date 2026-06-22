package com.visus.central.domain.model;

import java.math.BigDecimal;

public class AplicacionPago {

	private Long idAplicacion;
	private Long idPago;
	private Long idPlanPago; // ← en lugar de idVencimiento
	private BigDecimal montoAplicado;
	private BigDecimal pctProntoPago;
	private BigDecimal montoDescuentoAplicado;
	private BigDecimal montoNetoPagado;
	// Campos auxiliares para la vista (no persistentes)
	private transient Integer numeroCuota;
	private transient String rangoCuotas;

	// Constructores
	public AplicacionPago() {

	}

	public AplicacionPago(Long idAplicacion, Long idPago, Long idPlanPago, BigDecimal montoAplicado,
			BigDecimal pctProntoPago, BigDecimal montoDescuentoAplicado, BigDecimal montoNetoPagado,
			Integer numeroCuota, String rangoCuotas) {
		super();
		this.idAplicacion = idAplicacion;
		this.idPago = idPago;
		this.idPlanPago = idPlanPago;
		this.montoAplicado = montoAplicado;
		this.pctProntoPago = pctProntoPago;
		this.montoDescuentoAplicado = montoDescuentoAplicado;
		this.montoNetoPagado = montoNetoPagado;
		this.numeroCuota = numeroCuota;
		this.rangoCuotas = rangoCuotas;
	}

	public Long getIdAplicacion() {
		return idAplicacion;
	}

	public void setIdAplicacion(Long idAplicacion) {
		this.idAplicacion = idAplicacion;
	}

	public Long getIdPago() {
		return idPago;
	}

	public void setIdPago(Long idPago) {
		this.idPago = idPago;
	}

	public Long getIdPlanPago() {
		return idPlanPago;
	}

	public void setIdPlanPago(Long idPlanPago) {
		this.idPlanPago = idPlanPago;
	}

	public BigDecimal getMontoAplicado() {
		return montoAplicado;
	}

	public void setMontoAplicado(BigDecimal montoAplicado) {
		this.montoAplicado = montoAplicado;
	}

	public BigDecimal getPctProntoPago() {
		return pctProntoPago;
	}

	public void setPctProntoPago(BigDecimal pctProntoPago) {
		this.pctProntoPago = pctProntoPago;
	}

	public BigDecimal getMontoDescuentoAplicado() {
		return montoDescuentoAplicado;
	}

	public void setMontoDescuentoAplicado(BigDecimal montoDescuentoAplicado) {
		this.montoDescuentoAplicado = montoDescuentoAplicado;
	}

	public BigDecimal getMontoNetoPagado() {
		return montoNetoPagado;
	}

	public void setMontoNetoPagado(BigDecimal montoNetoPagado) {
		this.montoNetoPagado = montoNetoPagado;
	}

	public Integer getNumeroCuota() {
		return numeroCuota;
	}

	public void setNumeroCuota(Integer numeroCuota) {
		this.numeroCuota = numeroCuota;
	}

	public String getRangoCuotas() {
		return rangoCuotas;
	}

	public void setRangoCuotas(String rangoCuotas) {
		this.rangoCuotas = rangoCuotas;
	}

}
