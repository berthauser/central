package com.visus.central.infraestructure.persistence.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "medidas")
public class JpaMedidaEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idmedida")
    private Integer id;

	@Column(name = "descripcion", nullable = false, length = 60)
    private String descripcion;

	@Column(name = "abreviatura", nullable = false, length = 2)
    private String abreviatura;

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

	public String getAbreviatura() {
		return abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	@Override
	public int hashCode() {
		return Objects.hash(abreviatura, descripcion, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JpaMedidaEntity other = (JpaMedidaEntity) obj;
		return Objects.equals(abreviatura, other.abreviatura) && Objects.equals(descripcion, other.descripcion)
				&& Objects.equals(id, other.id);
	}
	
}