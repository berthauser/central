package com.visus.central.domain.model;

import java.util.Objects;

public class Linea {
	
	private Integer idLinea;
    private String descripcion;
    private Rubro rubro;
	
    public Integer getIdLinea() {
		return idLinea;
	}
	
    public void setIdLinea(Integer idLinea) {
		this.idLinea = idLinea;
	}
	
    public String getDescripcion() {
		return descripcion;
	}
	
    public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
    public Rubro getRubro() {
		return rubro;
	}
	
    public void setRubro(Rubro rubro) {
		this.rubro = rubro;
	}

	@Override
	public int hashCode() {
		return Objects.hash(descripcion, idLinea, rubro);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Linea other = (Linea) obj;
		return Objects.equals(descripcion, other.descripcion) && Objects.equals(idLinea, other.idLinea)
				&& Objects.equals(rubro, other.rubro);
	}

	@Override
	public String toString() {
		return "Linea [idLinea=" + idLinea + ", descripcion=" + descripcion + ", rubro=" + rubro + "]";
	}
    

}
