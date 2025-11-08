package com.visus.central.domain.model;

import com.visus.central.infraestructure.persistence.entity.JpaComprobanteEntity.Columna;
import com.visus.central.infraestructure.persistence.entity.JpaComprobanteEntity.NombreCorto;

public class Comprobante {
	private Integer id;
    private String nombreLargo;
    private NombreCorto nombreCorto;
    private Integer numeroInicial;
    private Integer sucursal;
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
	
    public Integer getSucursal() {
		return sucursal;
	}
	
    public void setSucursal(Integer sucursal) {
		this.sucursal = sucursal;
	}
	
    public Columna getColumna() {
		return columna;
	}
	
    public void setColumna(Columna columna) {
		this.columna = columna;
	}

}
