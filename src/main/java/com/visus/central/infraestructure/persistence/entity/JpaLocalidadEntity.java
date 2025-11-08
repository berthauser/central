package com.visus.central.infraestructure.persistence.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "localidades")
public class JpaLocalidadEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idlocalidad")
    private Integer idlocalidad;

	@Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

	@Column(name = "codigo_postal", nullable = false)
    private Integer codigoPostal;

    @ManyToOne
    @JoinColumn(name = "iddepartamento", referencedColumnName = "iddepartamento", foreignKey = @ForeignKey(name = "fk_localidades_departamento"))
    private JpaDepartamentoEntity departamento;

	public Integer getIdlocalidad() {
		return idlocalidad;
	}

	public void setIdlocalidad(Integer idlocalidad) {
		this.idlocalidad = idlocalidad;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(Integer codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public JpaDepartamentoEntity getDepartamento() {
		return departamento;
	}

	public void setDepartamento(JpaDepartamentoEntity departamento) {
		this.departamento = departamento;
	}

	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    JpaLocalidadEntity that = (JpaLocalidadEntity) o;
	    return Objects.equals(idlocalidad, that.idlocalidad);
	}

	@Override
	public int hashCode() {
	    return Objects.hash(idlocalidad);
	}

}
