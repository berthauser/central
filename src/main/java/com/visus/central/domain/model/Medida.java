package com.visus.central.domain.model;

import java.util.Objects;

public class Medida {

	private Integer id;
	private String descripcion;
	private String abreviatura;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getAbreviatura() {
		return abreviatura;
	}
	
	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	@Override
	public int hashCode() {
		return Objects.hash(abreviatura, id, descripcion);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Medida other = (Medida) obj;
		return Objects.equals(abreviatura, other.abreviatura) && Objects.equals(id, other.id)
				&& Objects.equals(descripcion, other.descripcion);
	}

	@Override
	public String toString() {
		return "Medida [id=" + id + ", nombre=" + descripcion + ", abreviatura=" + abreviatura + "]";
	}
	
}
