package com.visus.central.infraestructure.persistence.entity;

import com.visus.central.domain.model.Columna;
import com.visus.central.domain.model.NombreCorto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "comprobantes")
public class JpaComprobanteEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idcomprobante")
	private Integer id;

	@Column(name = "nombre_largo", nullable = false, length = 60)
	private String nombreLargo;

	@Enumerated(EnumType.STRING)
	@Column(name = "nombre_corto", length = 3)
	private NombreCorto nombreCorto; // ← JPA guardará el nombre del enum (REM, FAC, etc.)

	@Column(name = "numero_inicial", nullable = false)
	private Integer numeroInicial = 1;

	@Column(name = "numero_final", nullable = false)
	private Integer numeroFinal = 1;

	@Column(name = "numero_actual", nullable = false)
	private Integer numeroActual;

	@Column(name = "activo")
	private Boolean activo;

	@Enumerated(EnumType.STRING)
	@Column(name = "columna", length = 2, nullable = false)
	private Columna columna;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombreLargo() {
		return nombreLargo;
	}

	public void setNombreLargo(String nombreLargo) {
		this.nombreLargo = nombreLargo;
	}

	public NombreCorto getNombreCorto() {
		return nombreCorto;
	}

	public void setNombreCorto(NombreCorto nombreCorto) {
		this.nombreCorto = nombreCorto;
	}

	public Integer getNumeroInicial() {
		return numeroInicial;
	}

	public void setNumeroInicial(Integer numeroInicial) {
		this.numeroInicial = numeroInicial;
	}

	public Integer getNumeroFinal() {
		return numeroFinal;
	}

	public void setNumeroFinal(Integer numeroFinal) {
		this.numeroFinal = numeroFinal;
	}

	public Integer getNumeroActual() {
		return numeroActual;
	}

	public void setNumeroActual(Integer numeroActual) {
		this.numeroActual = numeroActual;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public Columna getColumna() {
		return columna;
	}

	public void setColumna(Columna columna) {
		this.columna = columna;
	}

}
