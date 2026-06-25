package com.visus.central.domain.model;

public enum TipoOperacionDevolucion {
    EGRESO_CAJA("Egreso de Caja"),
    NOTA_CREDITO("Nota de Crédito");

    private final String label;

    TipoOperacionDevolucion(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
