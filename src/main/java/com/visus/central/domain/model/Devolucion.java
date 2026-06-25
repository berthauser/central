package com.visus.central.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Devolucion {

    private Long id;
    private Integer idVenta;
    private Integer idArticulo;
    private Integer cantidad;
    private BigDecimal montoDevuelto;
    private BigDecimal montoCredito;
    private TipoOperacionDevolucion tipoOperacion;
    private Long idNotaCredito;
    private Long idPago;
    private Integer idMovimientoCaja;
    private LocalDate fecha;
    private String observaciones;
    private boolean malEstado;
    private Integer idUsuario;

    public Devolucion() {
    }

    public Devolucion(Integer idVenta, Integer idArticulo, Integer cantidad, BigDecimal montoDevuelto,
                      TipoOperacionDevolucion tipoOperacion, boolean malEstado, String observaciones, Integer idUsuario) {
        this.idVenta = idVenta;
        this.idArticulo = idArticulo;
        this.cantidad = cantidad;
        this.montoDevuelto = montoDevuelto;
        this.tipoOperacion = tipoOperacion;
        this.malEstado = malEstado;
        this.fecha = LocalDate.now();
        this.observaciones = observaciones;
        this.idUsuario = idUsuario;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getIdVenta() { return idVenta; }
    public void setIdVenta(Integer idVenta) { this.idVenta = idVenta; }
    public Integer getIdArticulo() { return idArticulo; }
    public void setIdArticulo(Integer idArticulo) { this.idArticulo = idArticulo; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public BigDecimal getMontoDevuelto() { return montoDevuelto; }
    public void setMontoDevuelto(BigDecimal montoDevuelto) { this.montoDevuelto = montoDevuelto; }
    public BigDecimal getMontoCredito() { return montoCredito; }
    public void setMontoCredito(BigDecimal montoCredito) { this.montoCredito = montoCredito; }
    public TipoOperacionDevolucion getTipoOperacion() { return tipoOperacion; }
    public void setTipoOperacion(TipoOperacionDevolucion tipoOperacion) { this.tipoOperacion = tipoOperacion; }
    public Long getIdNotaCredito() { return idNotaCredito; }
    public void setIdNotaCredito(Long idNotaCredito) { this.idNotaCredito = idNotaCredito; }
    public Long getIdPago() { return idPago; }
    public void setIdPago(Long idPago) { this.idPago = idPago; }
    public Integer getIdMovimientoCaja() { return idMovimientoCaja; }
    public void setIdMovimientoCaja(Integer idMovimientoCaja) { this.idMovimientoCaja = idMovimientoCaja; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public boolean isMalEstado() { return malEstado; }
    public void setMalEstado(boolean malEstado) { this.malEstado = malEstado; }
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
}
