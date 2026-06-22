package com.visus.central.infraestructure.persistence.entity;

/**
 * @PrePersist: stándar JPA, se asigna en el momento exacto de la persistencia.
 * updatable = false evita que se sobrescriba en operaciones merge() o update().
 * 
 */

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "pagos")
public class PagoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idpago")
	private Long idPago;

	@Column(name = "idcliente", nullable = false)
	private Integer idCliente;

	@Column(name = "idventa")
	private Integer idVenta;

	@Column(name = "fecha", nullable = false, updatable = false)
	private LocalDate fecha;

	@PrePersist
	protected void onCreate() {
		this.fecha = LocalDate.now(); // puede ser también LocalDate.now(ZoneId.of("TuZona"))
	}

	@Column(name = "monto_total", nullable = false, precision = 15, scale = 2)
	private BigDecimal montoTotal;

	@Column(name = "idtipo_pago", nullable = false)
	private Integer idTipoPago;

	@Column(name = "aplicado")
	private Boolean aplicado;

	@Column(name = "observaciones")
	private String observaciones;

	public Long getIdPago() {
		return idPago;
	}

	public void setIdPago(Long idPago) {
		this.idPago = idPago;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public Integer getIdVenta() {
		return idVenta;
	}

	public void setIdVenta(Integer idVenta) {
		this.idVenta = idVenta;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public BigDecimal getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(BigDecimal montoTotal) {
		this.montoTotal = montoTotal;
	}

	public Integer getIdTipoPago() {
		return idTipoPago;
	}

	public void setIdTipoPago(Integer idTipoPago) {
		this.idTipoPago = idTipoPago;
	}

	public Boolean getAplicado() {
		return aplicado;
	}

	public void setAplicado(Boolean aplicado) {
		this.aplicado = aplicado;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

}
