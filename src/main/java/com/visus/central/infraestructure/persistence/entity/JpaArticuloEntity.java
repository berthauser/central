package com.visus.central.infraestructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.visus.central.domain.model.EstadoArticulo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "articulos")
public class JpaArticuloEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idarticulo")
    private Integer id;
	
	@Column(name = "codigo_interno", nullable = false, length = 13)
    private String codigoInterno;
    
	@Column(name = "codigo_barra", nullable = false, length = 13)
    private String codigoBarra;

    @Column(name = "nrolote", length = 20)
    private String numeroLote;
    
    @Column(name = "descripcion", nullable = false, length = 60)
    private String descripcion;

    @Column(name = "stock", nullable = false)
    private Integer stock;
    
    @Column(name = "stock_minimo", nullable = false)
    private Integer stock_minimo;
    
    @Column(name = "stock_maximo", nullable = false)
    private Integer stock_maximo;
    
    @ManyToOne
    @JoinColumn(name = "idlinea", referencedColumnName = "idlineas", foreignKey = @ForeignKey(name = "fk_articulos_lineas"))
    private JpaLineaEntity linea;
    
    // Se supone que JPA al tener ya el "idlinea" debiera traerme la "descripcion" del Rubro. El código de abajo es la relación definida
    // en JpaLineaEntity
    // 	 @ManyToOne
    // 	 @JoinColumn(name = "idrubros", referencedColumnName = "idrubro", foreignKey = @ForeignKey(name = "fk_rubro_lineas"))
    // 	 private JpaRubroEntity rubro;
    
    @ManyToOne
    @JoinColumn(name = "idmedida", referencedColumnName = "idmedida", foreignKey = @ForeignKey(name = "fk_articulos_medidas"))
    private JpaMedidaEntity medida;
    
    @ManyToOne
    @JoinColumn(name = "idpresentacion", referencedColumnName = "idpresentacion", foreignKey = @ForeignKey(name = "fk_articulos_presentaciones"))
    private JpaPresentacionEntity presentacion;
    
    @ManyToOne
    @JoinColumn(name = "idproveedor", referencedColumnName = "idproveedor", foreignKey = @ForeignKey(name = "fk_articulos_proveedores"))
    private JpaProveedorEntity proveedor;
    
    @Column(name = "fecha_compra")
    private LocalDate fechaCompra;
    
    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "fecha_baja")
    private LocalDate fechaBaja;
    
    @Column(name = "fecha_actprecios")
    private LocalDate fechaActualizPrecios;;
    
    @Column(name = "fila", nullable = false)
    private Integer fila;
    
    @Column(name = "columna", nullable = false)
    private Integer columna;
    
    @Column(name = "precio_costo", precision = 18, scale = 2)
    private BigDecimal precio_costo;    
    
    @Column(name = "margen_utilidad", precision = 18, scale = 2)
    private BigDecimal margen_utilidad;    
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idalicuota", referencedColumnName = "idalicuota", foreignKey = @ForeignKey(name = "fk_articulos_alicuotas"))
    private JpaAlicuotaEntity alicuota;
    
    @Column(name = "es_bonificado")
    private boolean esBonificado;
    
    @Column(name = "bonificacion", precision = 15, scale = 2)
    private BigDecimal bonificacion;    
    
	@Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 15)
    private EstadoArticulo estado;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCodigoInterno() {
		return codigoInterno;
	}

	public void setCodigoInterno(String codigoInterno) {
		this.codigoInterno = codigoInterno;
	}

	public String getCodigoBarra() {
		return codigoBarra;
	}

	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}

	public String getNumeroLote() {
		return numeroLote;
	}

	public void setNumeroLote(String numeroLote) {
		this.numeroLote = numeroLote;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getStock_minimo() {
		return stock_minimo;
	}

	public void setStock_minimo(Integer stock_minimo) {
		this.stock_minimo = stock_minimo;
	}

	public Integer getStock_maximo() {
		return stock_maximo;
	}

	public void setStock_maximo(Integer stock_maximo) {
		this.stock_maximo = stock_maximo;
	}

	public JpaLineaEntity getLinea() {
		return linea;
	}

	public void setLinea(JpaLineaEntity linea) {
		this.linea = linea;
	}

	public JpaMedidaEntity getMedida() {
		return medida;
	}

	public void setMedida(JpaMedidaEntity medida) {
		this.medida = medida;
	}

	public JpaPresentacionEntity getPresentacion() {
		return presentacion;
	}

	public void setPresentacion(JpaPresentacionEntity presentacion) {
		this.presentacion = presentacion;
	}

	public JpaProveedorEntity getProveedor() {
		return proveedor;
	}

	public void setProveedor(JpaProveedorEntity proveedor) {
		this.proveedor = proveedor;
	}

	public LocalDate getFechaCompra() {
		return fechaCompra;
	}

	public void setFechaCompra(LocalDate fechaCompra) {
		this.fechaCompra = fechaCompra;
	}

	public LocalDate getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(LocalDate fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public LocalDate getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(LocalDate fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	public LocalDate getFechaActualizPrecios() {
		return fechaActualizPrecios;
	}

	public void setFechaActualizPrecios(LocalDate fechaActualizPrecios) {
		this.fechaActualizPrecios = fechaActualizPrecios;
	}

	public Integer getFila() {
		return fila;
	}

	public void setFila(Integer fila) {
		this.fila = fila;
	}

	public Integer getColumna() {
		return columna;
	}

	public void setColumna(Integer columna) {
		this.columna = columna;
	}

	public BigDecimal getPrecio_costo() {
		return precio_costo;
	}

	public void setPrecio_costo(BigDecimal precio_costo) {
		this.precio_costo = precio_costo;
	}

	public BigDecimal getMargen_utilidad() {
		return margen_utilidad;
	}

	public void setMargen_utilidad(BigDecimal margen_utilidad) {
		this.margen_utilidad = margen_utilidad;
	}

	public JpaAlicuotaEntity getAlicuota() {
		return alicuota;
	}

	public void setAlicuota(JpaAlicuotaEntity alicuota) {
		this.alicuota = alicuota;
	}

	public boolean isEsBonificado() {
		return esBonificado;
	}

	public void setEsBonificado(boolean esBonificado) {
		this.esBonificado = esBonificado;
	}

	public BigDecimal getBonificacion() {
		return bonificacion;
	}

	public void setBonificacion(BigDecimal bonificacion) {
		this.bonificacion = bonificacion;
	}

	public EstadoArticulo getEstado() {
		return estado;
	}

	public void setEstado(EstadoArticulo estado) {
		this.estado = estado;
	}

}
