package com.visus.central.infraestructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.visus.central.domain.model.OrigenMovimiento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "movimientos_caja")
public class MovimientoCajaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idmovimiento;

	@ManyToOne
	@JoinColumn(name = "idcaja")
	private CajaEntity caja;

	@Column(name = "fecha", nullable = false, updatable = false)
	private LocalDate fecha;

	@Column(name = "hora", nullable = false, updatable = false)
	private LocalTime hora;

	@ManyToOne
	@JoinColumn(name = "idcomprobante")
	private JpaComprobanteEntity comprobante;

	@Column(name = "numero_comprobante", nullable = false)
	private Integer numeroComprobante;

	@ManyToOne
	@JoinColumn(name = "idventa")
	private JpaVentaEntity venta;

	@ManyToOne
	@JoinColumn(name = "idplan_pago")
	private PlanPagoEntity planPago;

	@ManyToOne
	@JoinColumn(name = "idtipo_pago")
	private TipoPagoEntity tipoPago;

	@Column(name = "debe", nullable = false, precision = 12, scale = 2)
	private BigDecimal debe;

	@Column(name = "haber", nullable = false, precision = 12, scale = 2)
	private BigDecimal haber;

	@Column(name = "descripcion", nullable = true, length = 250)
	private String descripcion;

	@Enumerated(EnumType.STRING)
	@Column(name = "origen", nullable = false, length = 20)
	private OrigenMovimiento origen;

	public Integer getIdmovimiento() {
		return idmovimiento;
	}

	public void setIdmovimiento(Integer idmovimiento) {
		this.idmovimiento = idmovimiento;
	}

	public CajaEntity getCaja() {
		return caja;
	}

	public void setCaja(CajaEntity caja) {
		this.caja = caja;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public LocalTime getHora() {
		return hora;
	}

	public void setHora(LocalTime hora) {
		this.hora = hora;
	}

	public JpaComprobanteEntity getComprobante() {
		return comprobante;
	}

	public void setComprobante(JpaComprobanteEntity comprobante) {
		this.comprobante = comprobante;
	}

	public Integer getNumeroComprobante() {
		return numeroComprobante;
	}

	public void setNumeroComprobante(Integer numeroComprobante) {
		this.numeroComprobante = numeroComprobante;
	}

	public JpaVentaEntity getVenta() {
		return venta;
	}

	public void setVenta(JpaVentaEntity venta) {
		this.venta = venta;
	}

	public PlanPagoEntity getPlanPago() {
		return planPago;
	}

	public void setPlanPago(PlanPagoEntity planPago) {
		this.planPago = planPago;
	}

	public TipoPagoEntity getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(TipoPagoEntity tipoPago) {
		this.tipoPago = tipoPago;
	}

	public BigDecimal getDebe() {
		return debe;
	}

	public void setDebe(BigDecimal debe) {
		this.debe = debe;
	}

	public BigDecimal getHaber() {
		return haber;
	}

	public void setHaber(BigDecimal haber) {
		this.haber = haber;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public OrigenMovimiento getOrigen() {
		return origen;
	}

	public void setOrigen(OrigenMovimiento origen) {
		this.origen = origen;
	}

}
