package com.visus.central.domain.model;

import java.util.Objects;

public class Concepto {
	
	private Integer id;
    private String descripcion;

    public Integer getId() {
        return id;
    }

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Concepto [id=" + id + ", descripcion=" + descripcion + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(descripcion, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Concepto other = (Concepto) obj;
		return Objects.equals(descripcion, other.descripcion) && Objects.equals(id, other.id);
	}
    

}
