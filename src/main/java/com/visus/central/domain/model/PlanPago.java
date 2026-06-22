package com.visus.central.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Plan de Pagos (Cuotas generadas por la venta) author: eazi date: 08/05/2026
 * 
 * @montoOriginal: valor nominal de la cuota
 * @nroCuota: NULL para pagos únicos (efectivo/cheque en mixtas)
 * @montoPagado: Total neto recibido para esta cuota
 * @montoDescuentoTotal: Suma de descuentos aplicados (pronto pago)
 * @fechaPago: se completa cuando el saldo sea 0
 * @fechaVencimiento: vencimiento de la cuota
 * @estado: ENUM desde el modelo
 * 
 */

public class PlanPago {
	private Long idPlanPago;        // serial → Long (o Integer, según tu preferencia)
    private Integer idVenta;        // int4 → Integer
    private Integer idCliente;      // int4 → Integer
    private Integer idTipoPago;     // int4 → Integer
    private Integer idCoeficiente;  // int4 → Integer (nullable)
    private BigDecimal montoOriginal;
    private Short nroCuota;
    private LocalDate fechaVencimiento;
    private BigDecimal montoPagado;
    private BigDecimal montoDescuentoTotal = BigDecimal.ZERO;
    private LocalDate fechaPago;
	private EstadoPlanPago estado;

	public PlanPago() {
		this.montoDescuentoTotal = BigDecimal.ZERO;
	}

	public PlanPago(Long idPlanPago, Integer idVenta, Integer idCliente, Integer idTipoPago, Integer idCoeficiente,
			BigDecimal montoOriginal, Short nroCuota, LocalDate fechaVencimiento, BigDecimal montoPagado,
			BigDecimal montoDescuentoTotal, LocalDate fechaPago, EstadoPlanPago estado) {
		super();
		this.idPlanPago = idPlanPago;
		this.idVenta = idVenta;
		this.idCliente = idCliente;
		this.idTipoPago = idTipoPago;
		this.idCoeficiente = idCoeficiente;
		this.montoOriginal = montoOriginal;
		this.nroCuota = nroCuota;
		this.fechaVencimiento = fechaVencimiento;
		this.montoPagado = montoPagado;
		this.montoDescuentoTotal = montoDescuentoTotal;
		this.fechaPago = fechaPago;
		this.estado = estado;
	}

	public Long getIdPlanPago() {
		return idPlanPago;
	}

	public void setIdPlanPago(Long idPlanPago) {
		this.idPlanPago = idPlanPago;
	}

	public Integer getIdVenta() {
		return idVenta;
	}

	public void setIdVenta(Integer idVenta) {
		this.idVenta = idVenta;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public Integer getIdTipoPago() {
		return idTipoPago;
	}

	public void setIdTipoPago(Integer idTipoPago) {
		this.idTipoPago = idTipoPago;
	}

	public Integer getIdCoeficiente() {
		return idCoeficiente;
	}

	public void setIdCoeficiente(Integer idCoeficiente) {
		this.idCoeficiente = idCoeficiente;
	}

	public BigDecimal getMontoOriginal() {
		return montoOriginal;
	}

	public void setMontoOriginal(BigDecimal montoOriginal) {
		this.montoOriginal = montoOriginal;
	}

	public Short getNroCuota() {
		return nroCuota;
	}

	public void setNroCuota(Short nroCuota) {
		this.nroCuota = nroCuota;
	}

	public LocalDate getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(LocalDate fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public BigDecimal getMontoPagado() {
		return montoPagado;
	}

	public void setMontoPagado(BigDecimal montoPagado) {
		this.montoPagado = montoPagado;
	}

	public BigDecimal getMontoDescuentoTotal() {
		return montoDescuentoTotal;
	}

	public void setMontoDescuentoTotal(BigDecimal montoDescuentoTotal) {
		this.montoDescuentoTotal = montoDescuentoTotal;
	}

	public LocalDate getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(LocalDate fechaPago) {
		this.fechaPago = fechaPago;
	}

	public EstadoPlanPago getEstado() {
		return estado;
	}

	public void setEstado(EstadoPlanPago estado) {
		this.estado = estado;
	}

	// Reglas de Negocio
	public boolean isVencida() {
		return LocalDate.now().isAfter(fechaVencimiento)
				&& (estado == EstadoPlanPago.PENDIENTE || estado == EstadoPlanPago.PARCIAL);
	}

	public void registrarPago(BigDecimal monto, LocalDate fecha) {
	    if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
	        throw new IllegalArgumentException("Monto inválido");
	    }
	    this.montoPagado = this.montoPagado.add(monto);
	    this.fechaPago = fecha;
	    if (this.montoPagado.compareTo(this.montoOriginal) >= 0) {
	        this.estado = EstadoPlanPago.PAGADA;
	        // Opcional: ajustar el montoPagado exactamente al montoOriginal si hubo excedente
	    } else {
	        this.estado = EstadoPlanPago.PARCIAL;
	    }
	}

	public void aplicarDescuento(BigDecimal descuento) {
	    if (descuento == null || descuento.compareTo(BigDecimal.ZERO) < 0) {
	        throw new IllegalArgumentException("Descuento inválido");
	    }
	    this.montoDescuentoTotal = descuento;
	    // Si el descuento se aplica al monto original, podrías recalcular el saldo pendiente
	    // Pero normalmente el descuento se refleja en el pago, no en la deuda original.
	}
	
	/**
	 * Marca la cuota como vencida si la fecha actual supera la fecha de vencimiento
	 * y el estado actual es PENDIENTE o PARCIAL. No hace nada si ya está pagada o ya vencida.
	 * 
	 * @return true si se cambió el estado a VENCIDA, false en caso contrario.
	 */
	public boolean marcarComoVencida() {
	    if (isVencida()) {
	        this.estado = EstadoPlanPago.VENCIDA;
	        // Opcional: registrar la fecha en que se marcó como vencida
	        // this.fechaEstado = LocalDate.now();
	        return true;
	    }
	    return false;
	}

}
