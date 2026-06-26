package com.visus.central.domain.model;

import java.util.List;
import java.util.Objects;

public class Vendedor {

	private Integer id;
	private Long numero;
	private String nombre;
	private String telefono;
	private String email;
	private SituacionFiscal situacionFiscal;
	private TipoDocumento tipoDocumento;
	private List<Domicilio> domicilios;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public SituacionFiscal getSituacionFiscal() {
		return situacionFiscal;
	}

	public void setSituacionFiscal(SituacionFiscal situacionFiscal) {
		this.situacionFiscal = situacionFiscal;
	}

	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	
	public List<Domicilio> getDomicilios() {
		return domicilios;
	}

	public void setDomicilios(List<Domicilio> domicilios) {
		this.domicilios = domicilios;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, id, nombre, numero, situacionFiscal, telefono, tipoDocumento);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vendedor other = (Vendedor) obj;
		return Objects.equals(email, other.email) && Objects.equals(id, other.id)
				&& Objects.equals(nombre, other.nombre) && Objects.equals(numero, other.numero)
				&& situacionFiscal == other.situacionFiscal && Objects.equals(telefono, other.telefono)
				&& tipoDocumento == other.tipoDocumento;
	}

	@Override
	public String toString() {
		return "Vendedor [id=" + id + ", numero=" + numero + ", nombre=" + nombre + ", telefono=" + telefono
				+ ", email=" + email + ", situacionFiscal=" + situacionFiscal + ", tipoDocumento=" + tipoDocumento
				+ "]";
	}

}
