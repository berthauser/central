package com.visus.central.infraestructure.persistence.entity;

import java.util.List;

import com.visus.central.domain.model.Provincia;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;

@Entity
@Table(name = "departamentos")
public class JpaDepartamentoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "iddepartamento")
	private Integer id;

	@Column(length = 60, nullable = false)
	private String nombre;

	@Enumerated(EnumType.STRING)
	@Column(name = "provincia", nullable = false, length = 35)
	private Provincia provincia;
	
	@OneToMany(mappedBy = "departamento")
	private List<JpaLocalidadEntity> localidades;

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

	public Provincia getProvincia() {
		return provincia;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

}