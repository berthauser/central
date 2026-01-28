package com.visus.central.infraestructure.persistence.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "alicuotas")
public class JpaAlicuotaEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idalicuota")
    private Integer id;

    @Column(name = "descripcion", nullable = false, length = 60)
    private String descripcion;

    @Column(name = "gravamen", nullable = false, precision = 18, scale = 2)
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
    
    

}
