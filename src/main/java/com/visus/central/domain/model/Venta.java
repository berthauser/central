package com.visus.central.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Venta {

	private Integer id;
	private Comprobante comprobante; // idcomprobante
	private Cliente cliente; // idcliente (titular)
	private Vendedor vendedor; // idvendedor
	private LocalDate fechaVenta; // fechaventa
	private Integer numeroComprobante; // numerocomprobante
	private Integer grupoFamiliarId; // idgrupofam (NULL si es titular)
	private Boolean esBonificado; // esbonificado
	private BigDecimal bonificacion; // bonificacion
	private String observaciones; // observaciones
	private EstadoVenta estado; // ENUM
	private List<Item> items = new ArrayList<>(); // items de la venta (no está en DDL, es relación)
	private List<ItemPago> mediosPago = new ArrayList<>(); // ← NUEVO
	private List<PlanPago> planesPago = new ArrayList<>(); // ← NUEVO

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Comprobante getComprobante() {
		return comprobante;
	}

	public void setComprobante(Comprobante comprobante) {
		this.comprobante = comprobante;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Vendedor getVendedor() {
		return vendedor;
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	public LocalDate getFechaVenta() {
		return fechaVenta;
	}

	public void setFechaVenta(LocalDate fechaVenta) {
		this.fechaVenta = fechaVenta;
	}

	public Integer getNumeroComprobante() {
		return numeroComprobante;
	}

	public void setNumeroComprobante(Integer numeroComprobante) {
		this.numeroComprobante = numeroComprobante;
	}

	public Integer getGrupoFamiliarId() {
		return grupoFamiliarId;
	}

	public void setGrupoFamiliarId(Integer grupoFamiliarId) {
		this.grupoFamiliarId = grupoFamiliarId;
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

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public EstadoVenta getEstado() {
		return estado;
	}

	public void setEstado(EstadoVenta estado) {
		this.estado = estado;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public List<ItemPago> getMediosPago() {
		return mediosPago;
	}

	public void setMediosPago(List<ItemPago> mediosPago) {
		this.mediosPago = mediosPago;
	}

	public List<PlanPago> getPlanesPago() {
		return planesPago;
	}

	public void setPlanesPago(List<PlanPago> planesPago) {
		this.planesPago = planesPago;
	}

	/** Subtotal: suma de cantidad * precioUnitario de cada ítem */
	public BigDecimal getSubtotal() {
		return items.stream().map(item -> item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	// --- Getters auxiliares para Jasper ---

	/** Descuento total aplicado a la venta */
	public BigDecimal getSubtotalPorcentaje() {
		if (bonificacion != null) {
			return getSubtotal().multiply(bonificacion).divide(BigDecimal.valueOf(100));
		}
		return BigDecimal.ZERO;
	}

	/** Porcentaje de bonificación */
	public Integer getPorcentaje() {
		return bonificacion != null ? bonificacion.intValue() : 0;
	}

	/** Total final = subtotal - descuento */
	public BigDecimal getTotal() {
		return getSubtotal().subtract(getSubtotalPorcentaje());
	}

	@Override
	public int hashCode() {
		return Objects.hash(bonificacion, cliente, comprobante, esBonificado, estado, fechaVenta,
				grupoFamiliarId, id, items, mediosPago, numeroComprobante, observaciones, planesPago, vendedor);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Venta other = (Venta) obj;
		return Objects.equals(bonificacion, other.bonificacion) && Objects.equals(cliente, other.cliente)
				&& Objects.equals(esBonificado, other.esBonificado) && estado == other.estado
				&& Objects.equals(fechaVenta, other.fechaVenta)
				&& Objects.equals(grupoFamiliarId, other.grupoFamiliarId) && Objects.equals(id, other.id)
				&& Objects.equals(items, other.items) && Objects.equals(mediosPago, other.mediosPago)
				&& Objects.equals(numeroComprobante, other.numeroComprobante)
				&& Objects.equals(observaciones, other.observaciones) && Objects.equals(planesPago, other.planesPago)
				&& Objects.equals(vendedor, other.vendedor);
	}

	@Override
	public String toString() {
		return "Venta [id=" + id + ", comprobante=" + comprobante + ", cliente=" + cliente + ", vendedor=" + vendedor
				+ ", fechaVenta=" + fechaVenta + ", numeroComprobante=" + numeroComprobante + ", grupoFamiliarId="
				+ grupoFamiliarId + ", esBonificado=" + esBonificado + ", bonificacion=" + bonificacion
				+ ", observaciones=" + observaciones + ", estado=" + estado + ", items=" + items + ", mediosPago="
				+ mediosPago + ", planesPago=" + planesPago + "]";
	}

}
