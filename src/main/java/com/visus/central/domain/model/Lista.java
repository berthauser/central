package com.visus.central.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lista {

    private Integer id;
    private String descripcion;
    private List<ListaPorcentual> items = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<ListaPorcentual> getItems() {
        return items;
    }

    public void setItems(List<ListaPorcentual> items) {
        this.items = items;
    }

    @Override
    public int hashCode() {
        return Objects.hash(descripcion, id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Lista other = (Lista) obj;
        return Objects.equals(descripcion, other.descripcion) && Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return descripcion != null ? descripcion : "";
    }
}
