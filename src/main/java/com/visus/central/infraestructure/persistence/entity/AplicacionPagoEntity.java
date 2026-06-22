package com.visus.central.infraestructure.persistence.entity;

/**
 * Aplicación de Pagos (Distribución detallada del pago a las cuotas)
 * 
 */

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "aplicacion_pagos")
public class AplicacionPagoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idaplicacion")
	private Long idAplicacion;

	@Column(name = "idpago", nullable = false)
	private Long idPago;

	@Column(name = "idplan_pago", nullable = false)
	private Long idPlanPago;

	@Column(name = "monto_aplicado", nullable = false)
	private BigDecimal montoAplicado;

	@Column(name = "pct_pronto_pago")
	private BigDecimal pctProntoPago;

	@Column(name = "monto_descuento_aplicado", nullable = false)
	private BigDecimal montoDescuentoAplicado;

	@Column(name = "monto_neto_pagado", nullable = false)
	private BigDecimal montoNetoPagado;

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

}