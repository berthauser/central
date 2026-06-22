package com.visus.central.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class MovimientoCaja {

	private Integer id;
	private Caja caja;
	private LocalDate fecha;
	private LocalTime hora;
	private Comprobante comprobante; // opcional
	private Integer numeroComprobante;
	private Venta venta; // opcional
	private PlanPago planPago; // opcional
	private TipoPago tipoPago; // opcional
	private BigDecimal debe; // ingreso
	private BigDecimal haber; // egreso
	private String descripcion;
	private OrigenMovimiento origen; // "manual" o "automatico"

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Caja getCaja() {
		return caja;
	}

	public void setCaja(Caja caja) {
		this.caja = caja;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public LocalTime getHora() {
		return hora;
	}

	public void setHora(LocalTime hora) {
		this.hora = hora;
	}

	public Comprobante getComprobante() {
		return comprobante;
	}

	public void setComprobante(Comprobante comprobante) {
		this.comprobante = comprobante;
	}

	public Integer getNumeroComprobante() {
		return numeroComprobante;
	}

	public void setNumeroComprobante(Integer numeroComprobante) {
		this.numeroComprobante = numeroComprobante;
	}

	public Venta getVenta() {
		return venta;
	}

	public void setVenta(Venta venta) {
		this.venta = venta;
	}

	public PlanPago getPlanPago() {
		return planPago;
	}

	public void setPlanPago(PlanPago planPago) {
		this.planPago = planPago;
	}

	public TipoPago getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(TipoPago tipoPago) {
		this.tipoPago = tipoPago;
	}

	public BigDecimal getDebe() {
		return debe;
	}

	public void setDebe(BigDecimal debe) {
		this.debe = debe;
	}

	public BigDecimal getHaber() {
		return haber;
	}

	public void setHaber(BigDecimal haber) {
		this.haber = haber;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public OrigenMovimiento getOrigen() {
		return origen;
	}

	public void setOrigen(OrigenMovimiento origen) {
		this.origen = origen;
	}

	// helpers
	public boolean esIngreso() {
		return debe != null && debe.compareTo(BigDecimal.ZERO) > 0;
	}

	public boolean esEgreso() {
		return haber != null && haber.compareTo(BigDecimal.ZERO) > 0;
	}

	public BigDecimal getMonto() {
		return esIngreso() ? debe : haber;
	}

}
