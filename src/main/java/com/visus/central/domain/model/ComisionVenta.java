package com.visus.central.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class ComisionVenta {

	private Long id;
	private Vendedor vendedor;
	private Venta venta;
	private Long idPago;
	private BigDecimal baseComisionable;
	private BigDecimal porcentaje;
	private BigDecimal comisionBruta;
	private BigDecimal ajustes;
	private BigDecimal comisionFinal;
	private LocalDate fechaCalculo;
	private EstadoComision estado;
	private String observaciones;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Vendedor getVendedor() {
		return vendedor;
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	public Venta getVenta() {
		return venta;
	}

	public void setVenta(Venta venta) {
		this.venta = venta;
	}

	public Long getIdPago() {
		return idPago;
	}

	public void setIdPago(Long idPago) {
		this.idPago = idPago;
	}

	public BigDecimal getBaseComisionable() {
		return baseComisionable;
	}

	public void setBaseComisionable(BigDecimal baseComisionable) {
		this.baseComisionable = baseComisionable;
	}

	public BigDecimal getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(BigDecimal porcentaje) {
		this.porcentaje = porcentaje;
	}

	public BigDecimal getComisionBruta() {
		return comisionBruta;
	}

	public void setComisionBruta(BigDecimal comisionBruta) {
		this.comisionBruta = comisionBruta;
	}

	public BigDecimal getAjustes() {
		return ajustes;
	}

	public void setAjustes(BigDecimal ajustes) {
		this.ajustes = ajustes;
	}

	public BigDecimal getComisionFinal() {
		return comisionFinal;
	}

	public void setComisionFinal(BigDecimal comisionFinal) {
		this.comisionFinal = comisionFinal;
	}

	public LocalDate getFechaCalculo() {
		return fechaCalculo;
	}

	public void setFechaCalculo(LocalDate fechaCalculo) {
		this.fechaCalculo = fechaCalculo;
	}

	public EstadoComision getEstado() {
		return estado;
	}

	public void setEstado(EstadoComision estado) {
		this.estado = estado;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ajustes, baseComisionable, comisionBruta, comisionFinal, estado, fechaCalculo, id, idPago,
				observaciones, porcentaje, venta, vendedor);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComisionVenta other = (ComisionVenta) obj;
		return Objects.equals(ajustes, other.ajustes) && Objects.equals(baseComisionable, other.baseComisionable)
				&& Objects.equals(comisionBruta, other.comisionBruta)
				&& Objects.equals(comisionFinal, other.comisionFinal) && estado == other.estado
				&& Objects.equals(fechaCalculo, other.fechaCalculo) && Objects.equals(id, other.id)
				&& Objects.equals(idPago, other.idPago) && Objects.equals(observaciones, other.observaciones)
				&& Objects.equals(porcentaje, other.porcentaje) && Objects.equals(venta, other.venta)
				&& Objects.equals(vendedor, other.vendedor);
	}

	@Override
	public String toString() {
		return "ComisionVenta [id=" + id + ", vendedor=" + vendedor + ", venta=" + venta + ", idPago=" + idPago
				+ ", baseComisionable=" + baseComisionable + ", porcentaje=" + porcentaje + ", comisionBruta="
				+ comisionBruta + ", ajustes=" + ajustes + ", comisionFinal=" + comisionFinal + ", fechaCalculo="
				+ fechaCalculo + ", estado=" + estado + ", observaciones=" + observaciones + "]";
	}
}
