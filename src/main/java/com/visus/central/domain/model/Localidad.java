package com.visus.central.domain.model;

import java.util.Objects;

public class Localidad {
	
	private Integer idlocalidad;
    private String nombre;
    private Integer codigoPostal;
	
    public Integer getIdlocalidad() {
		return idlocalidad;
	}
    
	public void setIdlocalidad(Integer idlocalidad) {
		this.idlocalidad = idlocalidad;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public Integer getCodigoPostal() {
		return codigoPostal;
	}
	
	public void setCodigoPostal(Integer codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigoPostal, idlocalidad, nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Localidad other = (Localidad) obj;
		return Objects.equals(codigoPostal, other.codigoPostal)
				&& Objects.equals(idlocalidad, other.idlocalidad) && Objects.equals(nombre, other.nombre);
	}

	@Override
	public String toString() {
		return "Localidad [idlocalidad=" + idlocalidad + ", nombre=" + nombre + ", codigoPostal=" + codigoPostal + "]";
	}
}