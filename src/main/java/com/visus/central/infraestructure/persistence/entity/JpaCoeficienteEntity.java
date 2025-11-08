package com.visus.central.infraestructure.persistence.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "coeficientes")
public class JpaCoeficienteEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcoeficiente")
    private Integer id;

    @Column(name = "descripcion", nullable = false, length = 50)
    private String descripcion;

    @Column(name = "coeficiente", nullable = false, precision = 18, scale = 2)
    private BigDecimal coeficiente;

    @Column(name = "cuotas", nullable = false)
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
    
    

}
