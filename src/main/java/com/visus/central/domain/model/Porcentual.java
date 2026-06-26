package com.visus.central.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Porcentual {

    private Integer id;
    private String descripcion;
    private BigDecimal porcentual;
    private LocalDate inicioVigencia;
    private LocalDate finVigencia;
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

    @Override
    public int hashCode() {
        return Objects.hash(clasificacion, descripcion, finVigencia, id, inicioVigencia, porcentual);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Porcentual other = (Porcentual) obj;
        return clasificacion == other.clasificacion
            && Objects.equals(descripcion, other.descripcion)
            && Objects.equals(finVigencia, other.finVigencia)
            && Objects.equals(id, other.id)
            && Objects.equals(inicioVigencia, other.inicioVigencia)
            && Objects.equals(porcentual, other.porcentual);
    }

    @Override
    public String toString() {
        return descripcion != null ? descripcion : "";
    }
}
