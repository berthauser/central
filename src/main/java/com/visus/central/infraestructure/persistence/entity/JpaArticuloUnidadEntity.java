package com.visus.central.infraestructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "articulos_unidades")
@IdClass(ArticuloUnidadId.class)
public class JpaArticuloUnidadEntity {

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idarticulo", referencedColumnName = "idarticulo",
		foreignKey = @ForeignKey(name = "fk_au_articulo"))
	private JpaArticuloEntity articulo;

	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idunidad", referencedColumnName = "idunidades",
		foreignKey = @ForeignKey(name = "fk_au_unidad"))
	private JpaUnidadEntity unidad;

	@Column(name = "cantidad", nullable = false)
	private Integer cantidad = 1;

	public JpaArticuloEntity getArticulo() {
		return articulo;
	}

	public void setArticulo(JpaArticuloEntity articulo) {
		this.articulo = articulo;
	}

	public JpaUnidadEntity getUnidad() {
		return unidad;
	}

	public void setUnidad(JpaUnidadEntity unidad) {
		this.unidad = unidad;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad != null ? cantidad : 1;
	}
}
