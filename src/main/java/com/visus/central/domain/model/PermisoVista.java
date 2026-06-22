package com.visus.central.domain.model;

import java.util.Objects;

public class PermisoVista {

	private Integer id;
	private Integer usuarioId;
	private String vistaClase;
	private boolean puedeVer;

	public PermisoVista() {
	}

	public PermisoVista(Integer usuarioId, String vistaClase, boolean puedeVer) {
		this.usuarioId = usuarioId;
		this.vistaClase = vistaClase;
		this.puedeVer = puedeVer;
	}

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PermisoVista that)) return false;
		return Objects.equals(usuarioId, that.usuarioId) && Objects.equals(vistaClase, that.vistaClase);
	}

	@Override
	public int hashCode() {
		return Objects.hash(usuarioId, vistaClase);
	}
}
