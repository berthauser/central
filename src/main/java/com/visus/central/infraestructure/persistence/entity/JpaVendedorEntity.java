package com.visus.central.infraestructure.persistence.entity;

import java.util.Objects;

import com.visus.central.domain.model.SituacionFiscal;
import com.visus.central.domain.model.TipoDocumento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vendedores")
public class JpaVendedorEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idvendedor")
    private Integer id;

    @Column(name = "numero")
    private Long numero;

    @Column(name = "nombre", nullable = false, length = 60)
    private String nombre;

    @Column(name = "telefono", length = 15)
    private String telefono;

    @Column(name = "email", length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacion_fiscal", nullable = false, length = 30)
    private SituacionFiscal situacionFiscal;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "documento", nullable = false, length = 20)
    private TipoDocumento tipoDocumento;
    
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
		JpaVendedorEntity other = (JpaVendedorEntity) obj;
		return Objects.equals(email, other.email) && Objects.equals(id, other.id)
				&& Objects.equals(nombre, other.nombre) && Objects.equals(numero, other.numero)
				&& situacionFiscal == other.situacionFiscal && Objects.equals(telefono, other.telefono)
				&& tipoDocumento == other.tipoDocumento;
	}

	@Override
	public String toString() {
		return "JpaVendedorEntity [id=" + id + ", numero=" + numero + ", nombre=" + nombre + ", telefono=" + telefono
				+ ", email=" + email + ", situacionFiscal=" + situacionFiscal + ", tipoDocumento=" + tipoDocumento
				+ "]";
	}
	
}
