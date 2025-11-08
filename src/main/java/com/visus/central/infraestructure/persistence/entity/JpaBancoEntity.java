package com.visus.central.infraestructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "bancos", uniqueConstraints = {
    @UniqueConstraint(columnNames = "idbcocen")
})
public class JpaBancoEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idbanco")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 60)
    private String nombre;

    @Column(name = "idbcocen", nullable = false)
    private Integer idBcoCen = 1;

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