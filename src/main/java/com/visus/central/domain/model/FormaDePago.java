package com.visus.central.domain.model;

import java.math.BigDecimal;

import com.visus.central.infraestructure.persistence.entity.JpaFormaDePagoEntity.ModalidadPago;

public class FormaDePago {
	
	private Integer id;
    private ModalidadPago modalidad;
    private Coeficiente coeficiente; 
    private Boolean esDtoProntoPago;
    private BigDecimal dtoProntoPago;
    private Boolean esMesesCompletos;
    
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public ModalidadPago getModalidad() {
		return modalidad;
	}
	
	public void setModalidad(ModalidadPago modalidad) {
		this.modalidad = modalidad;
	}
	
	public Coeficiente getCoeficiente() {
		return coeficiente;
	}
	
	public void setCoeficiente(Coeficiente coeficiente) {
		this.coeficiente = coeficiente;
	}
	
	public Boolean getEsDtoProntoPago() {
		return esDtoProntoPago;
	}
	
	public void setEsDtoProntoPago(Boolean esDtoProntoPago) {
		this.esDtoProntoPago = esDtoProntoPago;
	}
	
	public BigDecimal getDtoProntoPago() {
		return dtoProntoPago;
	}
	
	public void setDtoProntoPago(BigDecimal dtoProntoPago) {
		this.dtoProntoPago = dtoProntoPago;
	}
	
	public Boolean getEsMesesCompletos() {
		return esMesesCompletos;
	}
	
	public void setEsMesesCompletos(Boolean esMesesCompletos) {
		this.esMesesCompletos = esMesesCompletos;
	}
    

}
