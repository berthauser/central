package com.visus.central.domain.model;

public enum TipoDomicilio {
	Fiscal_Pcial_Jurisdicción_Sede("Fiscal Pcial Jurisdicción Sede"), Principal_de_Actividades("Principal de Actividades"),
	Fiscal_Jurisdiccional("Fiscal Jurisdiccional"), Sin_Declarar("Sin Declarar");

	private final String label;

	TipoDomicilio(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}

	public String getLabel() {
		return label;
	}
}
