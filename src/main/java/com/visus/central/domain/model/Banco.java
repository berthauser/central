package com.visus.central.domain.model;

import java.util.Objects;

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

	@Override
	public int hashCode() {
		return Objects.hash(id, idBcoCen, nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Banco other = (Banco) obj;
		return Objects.equals(id, other.id) && Objects.equals(idBcoCen, other.idBcoCen)
				&& Objects.equals(nombre, other.nombre);
	}
    
    
	
}
