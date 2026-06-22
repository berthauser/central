package com.visus.central.infraestructure.persistence.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "items")
public class JpaItemEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iditem")
    private Integer idItem;

    @ManyToOne
    @JoinColumn(name = "idventa", nullable = false,
            foreignKey = @ForeignKey(name = "fk_ventas01"))
    private JpaVentaEntity venta;

    @ManyToOne
    @JoinColumn(name = "idarticulo", nullable = false,
            foreignKey = @ForeignKey(name = "fk_articulo"))
    private JpaArticuloEntity articulo;   // Producto

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 18, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "descuento_articulo", precision = 18, scale = 2)
    private BigDecimal descuentoArticulo;

    @Column(name = "saldo_articulo", precision = 18, scale = 2)
    private BigDecimal saldoArticulo;

	public Integer getIdItem() {
		return idItem;
	}

	public void setIdItem(Integer idItem) {
		this.idItem = idItem;
	}

	public JpaVentaEntity getVenta() {
		return venta;
	}

	public void setVenta(JpaVentaEntity venta) {
		this.venta = venta;
	}

	public JpaArticuloEntity getArticulo() {
		return articulo;
	}

	public void setArticulo(JpaArticuloEntity articulo) {
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
	
	

}
