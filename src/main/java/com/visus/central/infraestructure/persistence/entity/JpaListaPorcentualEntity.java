package com.visus.central.infraestructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "listas_porcentuales")
public class JpaListaPorcentualEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idlisporc")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idlista", referencedColumnName = "idlista",
        foreignKey = @ForeignKey(name = "fk_listas_porcentuales_lista"))
    private JpaListaEntity lista;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idporcentual", referencedColumnName = "idporcentual",
        foreignKey = @ForeignKey(name = "fk_listas_porcentuales_porcentual"))
    private JpaPorcentualEntity porcentual;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public JpaListaEntity getLista() {
        return lista;
    }

    public void setLista(JpaListaEntity lista) {
        this.lista = lista;
    }

    public JpaPorcentualEntity getPorcentual() {
        return porcentual;
    }

    public void setPorcentual(JpaPorcentualEntity porcentual) {
        this.porcentual = porcentual;
    }
}
