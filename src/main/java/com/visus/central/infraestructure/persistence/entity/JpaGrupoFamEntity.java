package com.visus.central.infraestructure.persistence.entity;

import com.visus.central.domain.model.Estado;
import com.visus.central.domain.model.Parentesco;
import com.visus.central.domain.model.TipoDocumento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "grupofam")
public class JpaGrupoFamEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idgrupofam")
    private Integer idgrupofam;

	@Column(name = "nombre", nullable = false, length = 100)
	private String nombre;
	
	@ManyToOne
    @JoinColumn(
        name = "idcliente",
        nullable = true
    )
    private JpaClienteEntity cliente;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "documento", nullable = false, length = 20)
	private TipoDocumento tipoDocumento;
	
	@Column(name = "numero")
    private Long numero;

    @Enumerated(EnumType.STRING)
    @Column(name = "parentesco", nullable = false, length = 20)
    private Parentesco parentesco;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 15)
    private Estado estado;

	public Integer getIdgrupofam() {
		return idgrupofam;
	}

	public void setIdgrupofam(Integer idgrupofam) {
		this.idgrupofam = idgrupofam;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public JpaClienteEntity getCliente() {
		return cliente;
	}

	public void setCliente(JpaClienteEntity cliente) {
		this.cliente = cliente;
	}

	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
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
    

}
