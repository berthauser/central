package com.visus.central.infraestructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.visus.central.domain.model.EstadoComision;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "comisiones_ventas")
public class JpaComisionVentaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idcomision")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idvendedor", nullable = false)
	private JpaVendedorEntity vendedor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idventa", nullable = false)
	private JpaVentaEntity venta;

	@Column(name = "idpago", nullable = false)
	private Long idPago;

	@Column(name = "base_comisionable", nullable = false, precision = 15, scale = 2)
	private BigDecimal baseComisionable;

	@Column(name = "porcentaje", nullable = false, precision = 5, scale = 2)
	private BigDecimal porcentaje;

	@Column(name = "comision_bruta", nullable = false, precision = 15, scale = 2)
	private BigDecimal comisionBruta;

	@Column(name = "ajustes", nullable = false, precision = 15, scale = 2)
	private BigDecimal ajustes;

	@Column(name = "comision_final", nullable = false, precision = 15, scale = 2)
	private BigDecimal comisionFinal;

	@Column(name = "fecha_calculo", nullable = false)
	private LocalDate fechaCalculo;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado", nullable = false, length = 20)
	private EstadoComision estado;

	@Column(name = "observaciones", columnDefinition = "TEXT")
	private String observaciones;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public JpaVendedorEntity getVendedor() {
		return vendedor;
	}

	public void setVendedor(JpaVendedorEntity vendedor) {
		this.vendedor = vendedor;
	}

	public JpaVentaEntity getVenta() {
		return venta;
	}

	public void setVenta(JpaVentaEntity venta) {
		this.venta = venta;
	}

	public Long getIdPago() {
		return idPago;
	}

	public void setIdPago(Long idPago) {
		this.idPago = idPago;
	}

	public BigDecimal getBaseComisionable() {
		return baseComisionable;
	}

	public void setBaseComisionable(BigDecimal baseComisionable) {
		this.baseComisionable = baseComisionable;
	}

	public BigDecimal getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(BigDecimal porcentaje) {
		this.porcentaje = porcentaje;
	}

	public BigDecimal getComisionBruta() {
		return comisionBruta;
	}

	public void setComisionBruta(BigDecimal comisionBruta) {
		this.comisionBruta = comisionBruta;
	}

	public BigDecimal getAjustes() {
		return ajustes;
	}

	public void setAjustes(BigDecimal ajustes) {
		this.ajustes = ajustes;
	}

	public BigDecimal getComisionFinal() {
		return comisionFinal;
	}

	public void setComisionFinal(BigDecimal comisionFinal) {
		this.comisionFinal = comisionFinal;
	}

	public LocalDate getFechaCalculo() {
		return fechaCalculo;
	}

	public void setFechaCalculo(LocalDate fechaCalculo) {
		this.fechaCalculo = fechaCalculo;
	}

	public EstadoComision getEstado() {
		return estado;
	}

	public void setEstado(EstadoComision estado) {
		this.estado = estado;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
}
