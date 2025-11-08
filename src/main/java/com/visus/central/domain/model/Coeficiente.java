package com.visus.central.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Coeficiente {
	
	private Integer id;
    private String descripcion;
    private BigDecimal coeficiente;
    private Short cuotas;
	
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
	
	public BigDecimal getCoeficiente() {
		return coeficiente;
	}
	
	public void setCoeficiente(BigDecimal coeficiente) {
		this.coeficiente = coeficiente;
	
	}
	public Short getCuotas() {
		return cuotas;
	}
	
	public void setCuotas(Short cuotas) {
		this.cuotas = cuotas;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(cuotas, descripcion, id);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coeficiente other = (Coeficiente) obj;
		return Objects.equals(cuotas, other.cuotas) && Objects.equals(descripcion, other.descripcion)
				&& Objects.equals(id, other.id);
	}
	
	@Override
	public String toString() {
		return "Coeficiente [id=" + id + ", descripcion=" + descripcion + ", cuotas=" + cuotas + "]";
	}
    
    

}
