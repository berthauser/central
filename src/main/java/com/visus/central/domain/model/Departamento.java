package com.visus.central.domain.model;

import com.visus.central.domain.model.Provincia;

public class Departamento {
	private Integer id;
    private String nombre;
    private Provincia provincia;
	
    public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}
    
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Provincia getProvincia() {
		return provincia;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

}
