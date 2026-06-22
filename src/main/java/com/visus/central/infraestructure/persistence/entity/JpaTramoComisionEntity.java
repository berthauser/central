package com.visus.central.infraestructure.persistence.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tramos_comision")
public class JpaTramoComisionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idtramo")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idregla", nullable = false)
	private JpaReglaComisionEntity regla;

	@Column(name = "desde", nullable = false, precision = 15, scale = 2)
	private BigDecimal desde;

	@Column(name = "hasta", precision = 15, scale = 2)
	private BigDecimal hasta;

	@Column(name = "porcentaje", nullable = false, precision = 5, scale = 2)
	private BigDecimal porcentaje;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public JpaReglaComisionEntity getRegla() {
		return regla;
	}

	public void setRegla(JpaReglaComisionEntity regla) {
		this.regla = regla;
	}

	public BigDecimal getDesde() {
		return desde;
	}

	public void setDesde(BigDecimal desde) {
		this.desde = desde;
	}

	public BigDecimal getHasta() {
		return hasta;
	}

	public void setHasta(BigDecimal hasta) {
		this.hasta = hasta;
	}

	public BigDecimal getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(BigDecimal porcentaje) {
		this.porcentaje = porcentaje;
	}
}
