package com.visus.central.domain.model;

import java.util.Objects;

import com.visus.central.infraestructure.persistence.entity.JpaGrupoFamEntity.Estado;
import com.visus.central.infraestructure.persistence.entity.JpaGrupoFamEntity.Parentesco;
import com.visus.central.infraestructure.persistence.entity.JpaGrupoFamEntity.TipoDocumento;

public class GrupoFam {
	private Integer id;
    private String nombre;
    private Integer idCliente;
    private TipoDocumento documento;
    private Long numero;
    private Parentesco parentesco;
    private Estado estado;
	
    public Integer getId() {
		return id;
	}
	
    public void setId(Integer id) {
		this.id = id;
	}
	
    public String getNombre() {
		return nombre;
	}
	
    public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
    public Integer getIdCliente() {
		return idCliente;
	}
	
    public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
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
	
    public Parentesco getParentesco() {
		return parentesco;
	}
	
    public void setParentesco(Parentesco parentesco) {
		this.parentesco = parentesco;
	}
	
    public Estado getEstado() {
		return estado;
	}
	
    public void setEstado(Estado estado) {
		this.estado = estado;
	}

	@Override
	public int hashCode() {
		return Objects.hash(documento, estado, id, idCliente, nombre, numero, parentesco);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GrupoFam other = (GrupoFam) obj;
		return documento == other.documento && estado == other.estado && Objects.equals(id, other.id)
				&& Objects.equals(idCliente, other.idCliente) && Objects.equals(nombre, other.nombre)
				&& Objects.equals(numero, other.numero) && parentesco == other.parentesco;
	}

	@Override
	public String toString() {
		return "GrupoFam [id=" + id + ", nombre=" + nombre + ", idCliente=" + idCliente + ", documento=" + documento
				+ ", numero=" + numero + ", parentesco=" + parentesco + ", estado=" + estado + "]";
	}

    
}
