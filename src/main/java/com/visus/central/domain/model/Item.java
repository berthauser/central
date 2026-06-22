package com.visus.central.domain.model;

import java.math.BigDecimal;

public class Item {

	private Integer idItem;
	private Venta venta; // relación bidireccional opcional
	private Articulo articulo;
	private Integer cantidad;
	private BigDecimal precioUnitario;
	private BigDecimal descuentoArticulo; // opcional, puede ser null
	private BigDecimal saldoArticulo; // opcional

	// Constructor para nuevos items (sin id, sin venta asociada aún)
    public Item(Articulo articulo, Integer cantidad, BigDecimal precioUnitario) {
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.descuentoArticulo = BigDecimal.ZERO;
        this.saldoArticulo = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    // Constructor completo (para reconstruir desde BD)
    public Item(Integer idItem, Venta venta, Articulo articulo,
                Integer cantidad, BigDecimal precioUnitario, BigDecimal descuentoArticulo, BigDecimal saldoArticulo) {
        this.idItem = idItem;
        this.venta = venta;
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.descuentoArticulo = descuentoArticulo;
        this.saldoArticulo = saldoArticulo;
    }

	public Integer getIdItem() {
		return idItem;
	}

	public void setIdItem(Integer idItem) {
		this.idItem = idItem;
	}

	public Venta getVenta() {
		return venta;
	}

	public void setVenta(Venta venta) {
		this.venta = venta;
	}

	public Articulo getArticulo() {
		return articulo;
	}

	public void setArticulo(Articulo articulo) {
		this.articulo = articulo;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public BigDecimal getDescuentoArticulo() {
		return descuentoArticulo;
	}

	public void setDescuentoArticulo(BigDecimal descuentoArticulo) {
		this.descuentoArticulo = descuentoArticulo;
	}

	public BigDecimal getSaldoArticulo() {
		return saldoArticulo;
	}

	public void setSaldoArticulo(BigDecimal saldoArticulo) {
		this.saldoArticulo = saldoArticulo;
	}
	/*
	 * Getters auxiliares para Jasper
	 * Estos nombres deben coincidir exactamente con los 
	 * nombres en Jasper
	 * 
	 */
    public String getArticuloDescripcion() {
        return articulo != null ? articulo.getDescripcion() : "";
    }

    public BigDecimal getSubtotal() {
        if (cantidad != null && precioUnitario != null) {
            return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }
        return BigDecimal.ZERO;
    }

	@Override
	public String toString() {
		return "Item [idItem=" + idItem + ", venta=" + venta + ", articulo=" + articulo + ", cantidad=" + cantidad
				+ ", precioUnitario=" + precioUnitario + ", descuentoArticulo=" + descuentoArticulo + ", saldoArticulo="
				+ saldoArticulo + "]";
	} 
	
	
	    
}
