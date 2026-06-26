package com.visus.central.infraestructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.visus.central.domain.model.TipoPorcentual;

@Entity
@Table(name = "porcentuales")
public class JpaPorcentualEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idporcentual")
    private Integer id;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @Column(name = "porcentual", precision = 18, scale = 2)
    private BigDecimal porcentual;

    @Column(name = "inicio_vigencia")
    private LocalDate inicioVigencia;

    @Column(name = "fin_vigencia")
    private LocalDate finVigencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "clasificacion", length = 15)
    private TipoPorcentual clasificacion;

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

    public BigDecimal getPorcentual() {
        return porcentual;
    }

    public void setPorcentual(BigDecimal porcentual) {
        this.porcentual = porcentual;
    }

    public LocalDate getInicioVigencia() {
        return inicioVigencia;
    }

    public void setInicioVigencia(LocalDate inicioVigencia) {
        this.inicioVigencia = inicioVigencia;
    }

    public LocalDate getFinVigencia() {
        return finVigencia;
    }

    public void setFinVigencia(LocalDate finVigencia) {
        this.finVigencia = finVigencia;
    }

    public TipoPorcentual getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(TipoPorcentual clasificacion) {
        this.clasificacion = clasificacion;
    }
}
