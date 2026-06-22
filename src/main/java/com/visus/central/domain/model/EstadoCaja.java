package com.visus.central.domain.model;

public enum EstadoCaja {
	
	ABIERTA("Abierta"), CERRADA("Cerrada");

	private final String estado;

	EstadoCaja(String estado) {
		this.estado = estado;
	}

	public String getEstado() {
		return estado;
	}

	@Override
	public String toString() {
		return estado;
	}

}
