package com.visus.central.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public class TipoPago {

	private Integer id;
	private String descripcion;
	private Boolean requiere_coeficiente;
	private Boolean es_pronto_pago;
	private BigDecimal dto_pronto_pago;
	private Boolean genera_deuda;
	private Boolean afecta_caja;

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

	@Override
	public int hashCode() {
		return Objects.hash(afecta_caja, descripcion, dto_pronto_pago, es_pronto_pago, genera_deuda, id,
				requiere_coeficiente);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoPago other = (TipoPago) obj;
		return Objects.equals(afecta_caja, other.afecta_caja) && Objects.equals(descripcion, other.descripcion)
				&& Objects.equals(dto_pronto_pago, other.dto_pronto_pago)
				&& Objects.equals(es_pronto_pago, other.es_pronto_pago)
				&& Objects.equals(genera_deuda, other.genera_deuda) && Objects.equals(id, other.id)
				&& Objects.equals(requiere_coeficiente, other.requiere_coeficiente);
	}

	@Override
	public String toString() {
		return "TipoPago [id=" + id + ", descripcion=" + descripcion + ", requiere_coeficiente=" + requiere_coeficiente
				+ ", es_pronto_pago=" + es_pronto_pago + ", dto_pronto_pago=" + dto_pronto_pago + ", genera_deuda="
				+ genera_deuda + ", afecta_caja=" + afecta_caja + "]";
	}

}
