package com.visus.central.infraestructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.visus.central.domain.model.EstadoPlanPago;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "plan_pagos")
public class PlanPagoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idplan_pago")
	private Long id;

	@Column(name = "idventa", nullable = false)
	private Integer idVenta;

	@Column(name = "idcliente", nullable = false)
	private Integer idCliente;

	@Column(name = "idtipo_pago", nullable = false)
	private Integer idTipo_pago;

	@Column(name = "idcoeficiente")
	private Integer idCoeficiente;

	@Column(name = "monto_original", nullable = false, precision = 15, scale = 2)
	private BigDecimal montoOriginal;

	@Column(name = "nro_cuota")
	private Short nroCuota;

	@Column(name = "fecha_vencimiento")
	private LocalDate fechaVencimiento;

	@Column(name = "monto_pagado", nullable = false, precision = 15, scale = 2)
	private BigDecimal montoPagado;

	@Column(name = "monto_descuento_total", nullable = false, precision = 15, scale = 2)
	private BigDecimal montoDescuento = BigDecimal.ZERO;

	@Column(name = "fecha_pago")
	private LocalDate fechaPago;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado", length = 20, nullable = false)
	private EstadoPlanPago estado; // usa el ENUM del dominio

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getIdVenta() {
		return idVenta;
	}

	public void setIdVenta(Integer idVenta) {
		this.idVenta = idVenta;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public Integer getIdTipo_pago() {
		return idTipo_pago;
	}

	public void setIdTipo_pago(Integer idTipo_pago) {
		this.idTipo_pago = idTipo_pago;
	}

	public Integer getIdCoeficiente() {
		return idCoeficiente;
	}

	public void setIdCoeficiente(Integer idCoeficiente) {
		this.idCoeficiente = idCoeficiente;
	}

	public BigDecimal getMontoOriginal() {
		return montoOriginal;
	}

	public void setMontoOriginal(BigDecimal montoOriginal) {
		this.montoOriginal = montoOriginal;
	}

	public Short getNroCuota() {
		return nroCuota;
	}

	public void setNroCuota(Short nroCuota) {
		this.nroCuota = nroCuota;
	}

	public LocalDate getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(LocalDate fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public BigDecimal getMontoPagado() {
		return montoPagado;
	}

	public void setMontoPagado(BigDecimal montoPagado) {
		this.montoPagado = montoPagado;
	}

	public BigDecimal getMontoDescuento() {
		return montoDescuento;
	}

	public void setMontoDescuento(BigDecimal montoDescuento) {
		this.montoDescuento = montoDescuento;
	}

	public LocalDate getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(LocalDate fechaPago) {
		this.fechaPago = fechaPago;
	}

	public EstadoPlanPago getEstado() {
		return estado;
	}

	public void setEstado(EstadoPlanPago estado) {
		this.estado = estado;
	}

}
