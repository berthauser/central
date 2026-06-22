package com.visus.central.infraestructure.persistence.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "unidades")
public class JpaUnidadEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idunidades")
    private Integer id;

	@Column(name = "idpresentacion", nullable = false, insertable = false, updatable = false)
	private Integer idPresentacion;

	@ManyToOne
	@JoinColumn(name = "idpresentacion", referencedColumnName = "idpresentacion")
	private JpaPresentacionEntity presentacion;

	@Column(name = "medida", nullable = false, precision = 9, scale = 1)
    private BigDecimal medida;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdPresentacion() {
		return idPresentacion;
	}

	public void setIdPresentacion(Integer idPresentacion) {
		this.idPresentacion = idPresentacion;
	}

	public JpaPresentacionEntity getPresentacion() {
		return presentacion;
	}

	public void setPresentacion(JpaPresentacionEntity presentacion) {
		this.presentacion = presentacion;
	}

	public BigDecimal getMedida() {
		return medida;
	}

	public void setMedida(BigDecimal medida) {
		this.medida = medida;
	}
}
