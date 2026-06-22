package com.visus.central.domain.model;

public enum EstadoVenta {
	
	PENDIENTE("Pendiente"),
    PAGADA("Pagada"),
    CANCELADA("Cancelada"),
    COBRADA("Cobrada"),
    DEVUELTA("Devuelta"),
    ERROR("Error"),
    RESERVADA("Reservada");

    private final String label;

    EstadoVenta(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

    public static EstadoVenta fromLabel(String label) {
        for (EstadoVenta e : values()) {
            if (e.label.equalsIgnoreCase(label)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Estado inválido: " + label);
    }

}
