package com.visus.central.infraestructure.persistence.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "listas")
public class JpaListaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idlista")
    private Integer id;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @OneToMany(mappedBy = "lista", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<JpaListaPorcentualEntity> items = new ArrayList<>();

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

    public List<JpaListaPorcentualEntity> getItems() {
        return items;
    }

    public void setItems(List<JpaListaPorcentualEntity> items) {
        this.items = items;
    }
}
