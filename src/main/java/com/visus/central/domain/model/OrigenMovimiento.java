package com.visus.central.domain.model;

public enum OrigenMovimiento {
	
	MANUAL("Manual"), AUTOMATICO("Automático");
	
	private final String origen;
	
	private OrigenMovimiento(String origen) {
		this.origen = origen;
	}

	public String getOrigen() {
		return origen;
	}
	
	@Override
	public String toString() {
		return origen;
	}

}
