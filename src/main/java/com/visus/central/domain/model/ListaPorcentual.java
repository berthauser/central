package com.visus.central.domain.model;

import java.util.Objects;

public class ListaPorcentual {

    private Integer id;
    private Integer listaId;
    private Integer porcentualId;
    private Porcentual porcentual;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getListaId() {
        return listaId;
    }

    public void setListaId(Integer listaId) {
        this.listaId = listaId;
    }

    public Integer getPorcentualId() {
        return porcentualId;
    }

    public void setPorcentualId(Integer porcentualId) {
        this.porcentualId = porcentualId;
    }

    public Porcentual getPorcentual() {
        return porcentual;
    }

    public void setPorcentual(Porcentual porcentual) {
        this.porcentual = porcentual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, listaId, porcentualId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ListaPorcentual other = (ListaPorcentual) obj;
        return Objects.equals(id, other.id)
            && Objects.equals(listaId, other.listaId)
            && Objects.equals(porcentualId, other.porcentualId);
    }
}
