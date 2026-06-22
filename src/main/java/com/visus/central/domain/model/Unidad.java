package com.visus.central.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Unidad {

	private Integer id;
	private Integer idPresentacion;
	private BigDecimal medida;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdPresentacion() {
		return idPresentacion;
	}

	public void setIdPresentacion(Integer idPresentacion) {
		this.idPresentacion = idPresentacion;
	}

	public BigDecimal getMedida() {
		return medida;
	}

	public void setMedida(BigDecimal medida) {
		this.medida = medida;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Unidad other = (Unidad) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Unidad [id=" + id + ", medida=" + medida + "]";
	}
}
