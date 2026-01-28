package com.visus.central.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Alicuota {
	
	private Integer id;
    private String descripcion;
    private BigDecimal gravamen;
	
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
	
    public BigDecimal getGravamen() {
		return gravamen;
	}
	
    public void setGravamen(BigDecimal gravamen) {
		this.gravamen = gravamen;
	}

	@Override
	public int hashCode() {
		return Objects.hash(descripcion, gravamen, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Alicuota other = (Alicuota) obj;
		return Objects.equals(descripcion, other.descripcion) && Objects.equals(gravamen, other.gravamen)
				&& Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Alicuota [id=" + id + ", descripcion=" + descripcion + ", gravamen=" + gravamen + "]";
	}
    
}
