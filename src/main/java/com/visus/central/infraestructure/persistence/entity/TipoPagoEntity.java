package com.visus.central.infraestructure.persistence.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tipos_pago")
public class TipoPagoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idtipo_pago")
	private Integer id;

	@Column(name = "descripcion", nullable = false, length = 50)
	private String descripcion;

	@Column(name = "requiere_coeficiente")
	private Boolean requiere_coeficiente = Boolean.FALSE;

	@Column(name = "es_pronto_pago")
	private Boolean es_pronto_pago = Boolean.FALSE;

	@Column(name = "dto_pronto_pago", precision = 15, scale = 2)
	private BigDecimal dto_pronto_pago;

	@Column(name = "genera_deuda", nullable = false)
	private Boolean genera_deuda = Boolean.FALSE;

	@Column(name = "afecta_caja", nullable = false)
	private Boolean afecta_caja = Boolean.FALSE;

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

	public Boolean getRequiere_coeficiente() {
		return requiere_coeficiente;
	}

	public void setRequiere_coeficiente(Boolean requiere_coeficiente) {
		this.requiere_coeficiente = requiere_coeficiente;
	}

	public Boolean getEs_pronto_pago() {
		return es_pronto_pago;
	}

	public void setEs_pronto_pago(Boolean es_pronto_pago) {
		this.es_pronto_pago = es_pronto_pago;
	}

	public BigDecimal getDto_pronto_pago() {
		return dto_pronto_pago;
	}

	public void setDto_pronto_pago(BigDecimal dto_pronto_pago) {
		this.dto_pronto_pago = dto_pronto_pago;
	}

	public Boolean getGenera_deuda() {
		return genera_deuda;
	}

	public void setGenera_deuda(Boolean genera_deuda) {
		this.genera_deuda = genera_deuda;
	}

	public Boolean getAfecta_caja() {
		return afecta_caja;
	}

	public void setAfecta_caja(Boolean afecta_caja) {
		this.afecta_caja = afecta_caja;
	}

}
