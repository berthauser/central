package com.visus.central.domain.model;

public class Banco {
	private Integer id;
    private String nombre;
    private Integer idBcoCen;
	
    public Integer getId() {
		return id;
	}
	
    public void setId(Integer id) {
		this.id = id;
	}
	
    public String getNombre() {
		return nombre;
	}
	
    public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
    public void setIdBcoCen(Integer idBcoCen) {
		this.idBcoCen = idBcoCen;
	}

	public Integer getIdBcoCen() {
		return idBcoCen;
	}
    
    
	
}
