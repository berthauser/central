package com.visus.central.domain.model;

public enum NombreCorto {
	REM("Remito"), FAC("Factura"), TKT("Ticket"), NCR("Nota Crédito"), NDB("Nota Débito"), REC("Recibo"), VAL("Vale");
	private final String descripcion;

	NombreCorto(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getCodigo() {
		return this.name(); // REM, FAC, etc.
	}

	public static NombreCorto fromCodigo(String codigo) {
		for (NombreCorto n : values()) {
			if (n.name().equalsIgnoreCase(codigo)) {
				return n;
			}
		}
		throw new IllegalArgumentException("Código inválido para nombre corto: " + codigo);
	}

}
