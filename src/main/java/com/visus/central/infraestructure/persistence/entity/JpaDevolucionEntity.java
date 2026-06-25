package com.visus.central.infraestructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "devoluciones")
public class JpaDevolucionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddevolucion")
    private Long id;

    @Column(name = "idventa", nullable = false)
    private Integer idVenta;

    @Column(name = "idarticulo", nullable = false)
    private Integer idArticulo;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "monto_devuelto", nullable = false, precision = 15, scale = 2)
    private BigDecimal montoDevuelto;

    @Column(name = "tipo_operacion", nullable = false, length = 20)
    private String tipoOperacion;

    @Column(name = "idnota_credito")
    private Long idNotaCredito;

    @Column(name = "idpago")
    private Long idPago;

    @Column(name = "idmovimiento_caja")
    private Integer idMovimientoCaja;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "mal_estado", nullable = false)
    private boolean malEstado;

    @Column(name = "usuario_id", nullable = false)
    private Integer idUsuario;

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
    public String getTipoOperacion() { return tipoOperacion; }
    public void setTipoOperacion(String tipoOperacion) { this.tipoOperacion = tipoOperacion; }
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
