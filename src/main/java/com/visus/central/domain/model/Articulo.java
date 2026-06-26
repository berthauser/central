package com.visus.central.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Articulo {
	
	private Integer id;
    private String codigo_interno;
    private String codigo_barra;
    private String descripcion;
    private String nrolote;
    private Integer stock;
    private Integer stock_minimo;
    private Integer stock_maximo;
    private Linea linea;
    private Medida medida;
    private Presentacion presentacion;
    private Proveedor proveedor;
    private LocalDate fechaCompra;
    private LocalDate fechaVencimiento;
    private LocalDate fechaBaja;
    private LocalDate fechaActualizPrecios;
    private Integer fila;
    private Integer columna;
    private BigDecimal precioCosto;
    private BigDecimal margenUtilidad;
    private Alicuota alicuota;
    private Boolean esBonificado;
    private BigDecimal bonificacion;
    private EstadoArticulo estado;

    private List<UnidadConCantidad> unidades = new ArrayList<>();

    public Integer getId() {
		return id;
	}
	
    public void setId(Integer id) {
		this.id = id;
	}
	
    public String getCodigo_interno() {
		return codigo_interno;
	}
	
    public void setCodigo_interno(String codigo_interno) {
		this.codigo_interno = codigo_interno;
	}
	
    public String getCodigo_barra() {
		return codigo_barra;
	}
	
    public void setCodigo_barra(String codigo_barra) {
		this.codigo_barra = codigo_barra;
	}
	
    public String getDescripcion() {
		return descripcion;
	}
	
    public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
    public String getNrolote() {
		return nrolote;
	}
	
    public void setNrolote(String nrolote) {
		this.nrolote = nrolote;
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
	
    public Linea getLinea() {
		return linea;
	}
	
    public void setLinea(Linea linea) {
		this.linea = linea;
	}
	
    public Medida getMedida() {
		return medida;
	}
	
    public void setMedida(Medida medida) {
		this.medida = medida;
	}
	
    public Presentacion getPresentacion() {
		return presentacion;
	}
	
    public void setPresentacion(Presentacion presentacion) {
		this.presentacion = presentacion;
	}
	
    public Proveedor getProveedor() {
		return proveedor;
	}
	
    public void setProveedor(Proveedor proveedor) {
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
	
    public BigDecimal getPrecioCosto() {
		return precioCosto;
	}
	
    public void setPrecioCosto(BigDecimal precioCosto) {
		this.precioCosto = precioCosto;
	}
	
    public BigDecimal getMargenUtilidad() {
		return margenUtilidad;
	}
	
    public void setMargenUtilidad(BigDecimal margenUtilidad) {
		this.margenUtilidad = margenUtilidad;
	}
	
    public Alicuota getAlicuota() {
		return alicuota;
	}
	
    public void setAlicuota(Alicuota alicuota) {
		this.alicuota = alicuota;
	}
	
    public Boolean getEsBonificado() {
		return esBonificado;
	}
	
    public void setEsBonificado(Boolean esBonificado) {
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
    
    public List<UnidadConCantidad> getUnidades() {
		return unidades;
	}

	public void setUnidades(List<UnidadConCantidad> unidades) {
		this.unidades = unidades != null ? unidades : new ArrayList<>();
	}

    public boolean isDisponible() {
        return this.estado == EstadoArticulo.Disponible;
    }
    
	@Override
	public int hashCode() {
		return Objects.hash(alicuota, bonificacion, codigo_barra, codigo_interno, columna, descripcion, esBonificado,
				estado, fechaActualizPrecios, fechaBaja, fechaCompra, fechaVencimiento, fila, id, linea, margenUtilidad,
				medida, nrolote, precioCosto, presentacion, proveedor, stock, stock_maximo, stock_minimo, unidades);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Articulo other = (Articulo) obj;
		return Objects.equals(alicuota, other.alicuota) && Objects.equals(bonificacion, other.bonificacion)
				&& Objects.equals(codigo_barra, other.codigo_barra)
				&& Objects.equals(codigo_interno, other.codigo_interno) && Objects.equals(columna, other.columna)
				&& Objects.equals(descripcion, other.descripcion) && Objects.equals(esBonificado, other.esBonificado)
				&& estado == other.estado && Objects.equals(fechaActualizPrecios, other.fechaActualizPrecios)
				&& Objects.equals(fechaBaja, other.fechaBaja) && Objects.equals(fechaCompra, other.fechaCompra)
				&& Objects.equals(fechaVencimiento, other.fechaVencimiento) && Objects.equals(fila, other.fila)
				&& Objects.equals(id, other.id) && Objects.equals(linea, other.linea)
				&& Objects.equals(margenUtilidad, other.margenUtilidad) && Objects.equals(medida, other.medida)
				&& Objects.equals(nrolote, other.nrolote) && Objects.equals(precioCosto, other.precioCosto)
				&& Objects.equals(presentacion, other.presentacion) && Objects.equals(proveedor, other.proveedor)
				&& Objects.equals(stock, other.stock) && Objects.equals(stock_maximo, other.stock_maximo) 
				&& Objects.equals(stock_minimo, other.stock_minimo)
				&& Objects.equals(unidades, other.unidades);
	}

	@Override
	public String toString() {
		return "Articulo [id=" + id + ", codigo_interno=" + codigo_interno + ", codigo_barra=" + codigo_barra
				+ ", descripcion=" + descripcion + ", nrolote=" + nrolote + ", stock=" + stock + ", stock_minimo="
				+ stock_minimo + ", stock_maximo=" + stock_maximo + ", linea=" + linea + ", rubro="
				+ ", medida=" + medida + ", presentacion=" + presentacion + ", proveedor=" + proveedor
				+ ", fechaCompra=" + fechaCompra + ", fechaVencimiento=" + fechaVencimiento + ", fechaBaja=" + fechaBaja
				+ ", fechaActualizPrecios=" + fechaActualizPrecios + ", fila=" + fila + ", columna=" + columna
				+ ", precioCosto=" + precioCosto + ", margenUtilidad=" + margenUtilidad + ", alicuota=" + alicuota
				+ ", esBonificado=" + esBonificado + ", bonificacion=" + bonificacion + ", estado=" + estado
				+ ", unidades=" + unidades + "]";
	}
	
}
