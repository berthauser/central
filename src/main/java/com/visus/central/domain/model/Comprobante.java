package com.visus.central.domain.model;

import java.util.Objects;

public class Comprobante {
	private Integer id;
	private String nombreLargo;
	private NombreCorto nombreCorto;
	private Integer numeroInicial;
	private Integer numeroFinal;
	private Integer numeroActual;
	private Boolean activo;
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

	@Override
	public int hashCode() {
		return Objects.hash(activo, columna, id, nombreCorto, nombreLargo, numeroActual, numeroFinal, numeroInicial);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comprobante other = (Comprobante) obj;
		return Objects.equals(activo, other.activo) && columna == other.columna && Objects.equals(id, other.id)
				&& nombreCorto == other.nombreCorto && Objects.equals(nombreLargo, other.nombreLargo)
				&& Objects.equals(numeroActual, other.numeroActual) && Objects.equals(numeroFinal, other.numeroFinal)
				&& Objects.equals(numeroInicial, other.numeroInicial);
	}

	@Override
	public String toString() {
		return "Comprobante [id=" + id + ", nombreLargo=" + nombreLargo + ", nombreCorto=" + nombreCorto
				+ ", numeroInicial=" + numeroInicial + ", numeroFinal=" + numeroFinal + ", numeroActual=" + numeroActual
				+ ", activo=" + activo + ", columna=" + columna + "]";
	}

}
