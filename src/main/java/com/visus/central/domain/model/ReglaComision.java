package com.visus.central.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReglaComision {

	private Long id;
	private Vendedor vendedor;
	private String nombre;
	private TipoBaseComisionable tipoBaseComisionable;
	private TipoCalculoComision tipoCalculo;
	private BigDecimal valorCalculo;
	private TipoEventoComision tipoEvento;
	private Boolean incluirDescuentos;
	private Boolean ajustarDevoluciones;
	private Integer ventanaAjusteDias;
	private Boolean activo;
	private LocalDate fechaCreacion;
	private List<TramoComision> tramos = new ArrayList<>();

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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public TipoBaseComisionable getTipoBaseComisionable() {
		return tipoBaseComisionable;
	}

	public void setTipoBaseComisionable(TipoBaseComisionable tipoBaseComisionable) {
		this.tipoBaseComisionable = tipoBaseComisionable;
	}

	public TipoCalculoComision getTipoCalculo() {
		return tipoCalculo;
	}

	public void setTipoCalculo(TipoCalculoComision tipoCalculo) {
		this.tipoCalculo = tipoCalculo;
	}

	public BigDecimal getValorCalculo() {
		return valorCalculo;
	}

	public void setValorCalculo(BigDecimal valorCalculo) {
		this.valorCalculo = valorCalculo;
	}

	public TipoEventoComision getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(TipoEventoComision tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public Boolean getIncluirDescuentos() {
		return incluirDescuentos;
	}

	public void setIncluirDescuentos(Boolean incluirDescuentos) {
		this.incluirDescuentos = incluirDescuentos;
	}

	public Boolean getAjustarDevoluciones() {
		return ajustarDevoluciones;
	}

	public void setAjustarDevoluciones(Boolean ajustarDevoluciones) {
		this.ajustarDevoluciones = ajustarDevoluciones;
	}

	public Integer getVentanaAjusteDias() {
		return ventanaAjusteDias;
	}

	public void setVentanaAjusteDias(Integer ventanaAjusteDias) {
		this.ventanaAjusteDias = ventanaAjusteDias;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public LocalDate getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDate fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public List<TramoComision> getTramos() {
		return tramos;
	}

	public void setTramos(List<TramoComision> tramos) {
		this.tramos = tramos;
	}

	@Override
	public int hashCode() {
		return Objects.hash(activo, ajustarDevoluciones, fechaCreacion, id, incluirDescuentos, nombre,
				tipoBaseComisionable, tipoCalculo, tipoEvento, valorCalculo, vendedor, ventanaAjusteDias);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReglaComision other = (ReglaComision) obj;
		return Objects.equals(activo, other.activo) && Objects.equals(ajustarDevoluciones, other.ajustarDevoluciones)
				&& Objects.equals(fechaCreacion, other.fechaCreacion) && Objects.equals(id, other.id)
				&& Objects.equals(incluirDescuentos, other.incluirDescuentos)
				&& Objects.equals(nombre, other.nombre) && tipoBaseComisionable == other.tipoBaseComisionable
				&& tipoCalculo == other.tipoCalculo && tipoEvento == other.tipoEvento
				&& Objects.equals(valorCalculo, other.valorCalculo) && Objects.equals(vendedor, other.vendedor)
				&& Objects.equals(ventanaAjusteDias, other.ventanaAjusteDias);
	}

	@Override
	public String toString() {
		return "ReglaComision [id=" + id + ", vendedor=" + vendedor + ", nombre=" + nombre
				+ ", tipoBaseComisionable=" + tipoBaseComisionable + ", tipoCalculo=" + tipoCalculo
				+ ", valorCalculo=" + valorCalculo + ", tipoEvento=" + tipoEvento + ", incluirDescuentos="
				+ incluirDescuentos + ", ajustarDevoluciones=" + ajustarDevoluciones + ", ventanaAjusteDias="
				+ ventanaAjusteDias + ", activo=" + activo + ", fechaCreacion=" + fechaCreacion + ", tramos="
				+ tramos + "]";
	}
}
