package com.visus.central.domain.model;

import com.visus.central.infraestructure.persistence.entity.JpaProveedorEntity.Estado;
import com.visus.central.infraestructure.persistence.entity.JpaProveedorEntity.SituacionFiscal;
import com.visus.central.infraestructure.persistence.entity.JpaProveedorEntity.TipoDocumento;

public class Proveedor {
	
	private Integer id;
    private String nombreFantasia;
    private String nombreReal;
    private TipoDocumento documento;
    private Long numero;
    private String telefonoUno;
    private String telefonoDos;
    private String telefonoTres;
    private SituacionFiscal situacionFiscal;
    private Banco banco; // RELACIÓN COMPLETA CON BANCO (no solo ID)
    private String email;
    private Estado estado;
	
    public Integer getId() {
		return id;
	}
	
    public void setId(Integer id) {
		this.id = id;
	}
	
    public String getNombreFantasia() {
		return nombreFantasia;
	}
	
    public void setNombreFantasia(String nombreFantasia) {
		this.nombreFantasia = nombreFantasia;
	}
	
    public String getNombreReal() {
		return nombreReal;
	}
	
    public void setNombreReal(String nombreReal) {
		this.nombreReal = nombreReal;
	}
	
    public TipoDocumento getDocumento() {
		return documento;
	}
	
    public void setDocumento(TipoDocumento documento) {
		this.documento = documento;
	}
	
    public Long getNumero() {
		return numero;
	}
	
    public void setNumero(Long numero) {
		this.numero = numero;
	}
	
    public String getTelefonoUno() {
		return telefonoUno;
	}
	
    public void setTelefonoUno(String telefonoUno) {
		this.telefonoUno = telefonoUno;
	}
	
    public String getTelefonoDos() {
		return telefonoDos;
	}
	
    public void setTelefonoDos(String telefonoDos) {
		this.telefonoDos = telefonoDos;
	}
	
    public String getTelefonoTres() {
		return telefonoTres;
	}
	
    public void setTelefonoTres(String telefonoTres) {
		this.telefonoTres = telefonoTres;
	}
	
    public SituacionFiscal getSituacionFiscal() {
		return situacionFiscal;
	}
	
    public void setSituacionFiscal(SituacionFiscal situacionFiscal) {
		this.situacionFiscal = situacionFiscal;
	}
	
    public Banco getBanco() {
		return banco;
	}
	
    public void setBanco(Banco banco) {
		this.banco = banco;
	}
    
//    // MÉTODO AUXILIAR PARA OBTENER ID DEL BANCO (para compatibilidad)
//    public Integer getIdBanco() {
//        return banco != null ? banco.getId() : null;
//    }
//    
//    // AUXILIAR PARA ESTABLECER BANCO POR ID (opcional)
//    public void setIdBanco(Integer idBanco) {
//        if (idBanco != null) {
//            Banco b = new Banco();
//            b.setId(idBanco);
//            this.banco = b;
//        } else {
//            this.banco = null;
//        }
//    }
	
    public String getEmail() {
		return email;
	}
	
    public void setEmail(String email) {
		this.email = email;
	}
	
    public Estado getEstado() {
		return estado;
	}
	
    public void setEstado(Estado estado) {
		this.estado = estado;
	}

}
