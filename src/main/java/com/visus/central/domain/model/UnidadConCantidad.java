package com.visus.central.domain.model;

import java.util.Objects;

public class UnidadConCantidad {

    private Unidad unidad;
    private Integer cantidad;

    public UnidadConCantidad() {
        this.cantidad = 1;
    }

    public UnidadConCantidad(Unidad unidad, Integer cantidad) {
        this.unidad = unidad;
        this.cantidad = cantidad != null ? cantidad : 1;
    }

    public Unidad getUnidad() {
        return unidad;
    }

    public void setUnidad(Unidad unidad) {
        this.unidad = unidad;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnidadConCantidad that = (UnidadConCantidad) o;
        return Objects.equals(unidad, that.unidad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unidad);
    }
}
