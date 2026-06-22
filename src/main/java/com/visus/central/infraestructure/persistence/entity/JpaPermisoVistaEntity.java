package com.visus.central.infraestructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "permisos_usuario", uniqueConstraints = {
	@UniqueConstraint(columnNames = { "usuario_id", "vista_clase" })
})
public class JpaPermisoVistaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "usuario_id", nullable = false)
	private Integer usuarioId;

	@Column(name = "vista_clase", nullable = false, length = 255)
	private String vistaClase;

	@Column(name = "puede_ver", nullable = false)
	private boolean puedeVer;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getVistaClase() {
		return vistaClase;
	}

	public void setVistaClase(String vistaClase) {
		this.vistaClase = vistaClase;
	}

	public boolean isPuedeVer() {
		return puedeVer;
	}

	public void setPuedeVer(boolean puedeVer) {
		this.puedeVer = puedeVer;
	}
}
