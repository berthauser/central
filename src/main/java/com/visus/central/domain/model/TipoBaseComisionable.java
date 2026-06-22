package com.visus.central.domain.model;

public enum TipoBaseComisionable {
	MONTO_PAGO("Monto del Pago"),
	TOTAL_VENTA("Total de Venta"),
	NETO_VENTA("Neto sin Descuentos"),
	SUBTOTAL_VENTA("Subtotal de Venta");

	private final String label;

	TipoBaseComisionable(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return label;
	}
}
