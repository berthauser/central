package com.visus.central.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class Caja {
	private Integer id;
	private LocalDate fechaApertura;
	private LocalTime horaApertura;
	private BigDecimal saldoInicial;
	private BigDecimal saldoRealCierre;
	private LocalDate fechaCierre;
	private LocalTime horaCierre;
	private EstadoCaja estado; // "abierta" o "cerrada"
	private Integer idUsuarioApertura;
	private Integer idUsuarioCierre;
	private String observaciones;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getFechaApertura() {
		return fechaApertura;
	}

	public void setFechaApertura(LocalDate fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	public LocalTime getHoraApertura() {
		return horaApertura;
	}

	public void setHoraApertura(LocalTime horaApertura) {
		this.horaApertura = horaApertura;
	}

	public BigDecimal getSaldoInicial() {
		return saldoInicial;
	}

	public void setSaldoInicial(BigDecimal saldoInicial) {
		this.saldoInicial = saldoInicial;
	}

	public BigDecimal getSaldoRealCierre() {
		return saldoRealCierre;
	}

	public void setSaldoRealCierre(BigDecimal saldoRealCierre) {
		this.saldoRealCierre = saldoRealCierre;
	}

	public LocalDate getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(LocalDate fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public LocalTime getHoraCierre() {
		return horaCierre;
	}

	public void setHoraCierre(LocalTime horaCierre) {
		this.horaCierre = horaCierre;
	}

	public EstadoCaja getEstado() {
		return estado;
	}

	public void setEstado(EstadoCaja estado) {
		this.estado = estado;
	}

	public Integer getIdUsuarioApertura() {
		return idUsuarioApertura;
	}

	public void setIdUsuarioApertura(Integer idUsuarioApertura) {
		this.idUsuarioApertura = idUsuarioApertura;
	}

	public Integer getIdUsuarioCierre() {
		return idUsuarioCierre;
	}

	public void setIdUsuarioCierre(Integer idUsuarioCierre) {
		this.idUsuarioCierre = idUsuarioCierre;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	// Métodos de negocio:
	public boolean estaAbierta() {
		return EstadoCaja.ABIERTA.equals(estado);
	}

	public void cerrar(BigDecimal saldoRealCierre, Integer idUsuarioCierre, String observaciones) {
		if (!estaAbierta())
			throw new IllegalStateException("La caja ya está cerrada");
		this.fechaCierre = LocalDate.now();
		this.horaCierre = LocalTime.now();
		this.saldoRealCierre = saldoRealCierre;
		this.idUsuarioCierre = idUsuarioCierre;
		this.observaciones = observaciones;
		this.estado = EstadoCaja.CERRADA;
	}

}
