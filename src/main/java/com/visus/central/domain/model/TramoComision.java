package com.visus.central.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public class TramoComision {

	private Long id;
	private ReglaComision regla;
	private BigDecimal desde;
	private BigDecimal hasta;
	private BigDecimal porcentaje;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ReglaComision getRegla() {
		return regla;
	}

	public void setRegla(ReglaComision regla) {
		this.regla = regla;
	}

	public BigDecimal getDesde() {
		return desde;
	}

	public void setDesde(BigDecimal desde) {
		this.desde = desde;
	}

	public BigDecimal getHasta() {
		return hasta;
	}

	public void setHasta(BigDecimal hasta) {
		this.hasta = hasta;
	}

	public BigDecimal getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(BigDecimal porcentaje) {
		this.porcentaje = porcentaje;
	}

	@Override
	public int hashCode() {
		return Objects.hash(desde, hasta, id, porcentaje);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TramoComision other = (TramoComision) obj;
		return Objects.equals(desde, other.desde) && Objects.equals(hasta, other.hasta)
				&& Objects.equals(id, other.id) && Objects.equals(porcentaje, other.porcentaje);
	}

	@Override
	public String toString() {
		return "TramoComision [id=" + id + ", desde=" + desde + ", hasta=" + hasta + ", porcentaje=" + porcentaje + "]";
	}
}
