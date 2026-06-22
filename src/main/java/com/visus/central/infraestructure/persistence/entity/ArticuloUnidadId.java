package com.visus.central.infraestructure.persistence.entity;

import java.io.Serializable;
import java.util.Objects;

public class ArticuloUnidadId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer articulo;
	private Integer unidad;

	public ArticuloUnidadId() {
	}

	public ArticuloUnidadId(Integer articulo, Integer unidad) {
		this.articulo = articulo;
		this.unidad = unidad;
	}

	public Integer getArticulo() {
		return articulo;
	}

	public void setArticulo(Integer articulo) {
		this.articulo = articulo;
	}

	public Integer getUnidad() {
		return unidad;
	}

	public void setUnidad(Integer unidad) {
		this.unidad = unidad;
	}

	@Override
	public int hashCode() {
		return Objects.hash(articulo, unidad);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		ArticuloUnidadId that = (ArticuloUnidadId) obj;
		return Objects.equals(articulo, that.articulo) && Objects.equals(unidad, that.unidad);
	}
}
