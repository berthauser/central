package com.visus.central.domain.model;

import java.util.Objects;

public class Presentacion {
	
	private Integer id;
    private String descripcion;
	
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
		Presentacion other = (Presentacion) obj;
		return Objects.equals(descripcion, other.descripcion) && Objects.equals(id, other.id);
	}
    
    

}
