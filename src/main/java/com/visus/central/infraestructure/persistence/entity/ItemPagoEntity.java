package com.visus.central.infraestructure.persistence.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "items_pago")
public class ItemPagoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "iditem_pago")
	private Long id;

	@Column(name = "idventa", nullable = false)
	private Long idVenta;

	@Column(name = "idtipo_pago", nullable = false)
	private Integer idTipoPago;

	@Column(name = "idcoeficiente")
	private Long idCoeficiente;

	@Column(name = "monto", nullable = false, precision = 15, scale = 2)
	private BigDecimal monto;

	@Column(name = "coeficiente_aplicado", precision = 18, scale = 2)
	private BigDecimal coeficienteAplicado;

	@Column(name = "cantidad_cuotas")
	private Short cantidadCuotas;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdVenta() {
		return idVenta;
	}

	public void setIdVenta(Long idVenta) {
		this.idVenta = idVenta;
	}

	public Integer getIdTipoPago() {
		return idTipoPago;
	}

	public void setIdTipoPago(Integer idTipoPago) {
		this.idTipoPago = idTipoPago;
	}

	public Long getIdCoeficiente() {
		return idCoeficiente;
	}

	public void setIdCoeficiente(Long idCoeficiente) {
		this.idCoeficiente = idCoeficiente;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public BigDecimal getCoeficienteAplicado() {
		return coeficienteAplicado;
	}

	public void setCoeficienteAplicado(BigDecimal coeficienteAplicado) {
		this.coeficienteAplicado = coeficienteAplicado;
	}

	public Short getCantidadCuotas() {
		return cantidadCuotas;
	}

	public void setCantidadCuotas(Short cantidadCuotas) {
		this.cantidadCuotas = cantidadCuotas;
	}

}
