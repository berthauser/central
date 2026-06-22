package com.visus.central.infraestructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.visus.central.domain.model.EstadoCaja;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "caja")
public class CajaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idcaja;

	@Column(name = "fecha_apertura", nullable = false)
	private LocalDate fechaApertura;

	@PrePersist
	protected void onCreate() {
		this.fechaApertura = LocalDate.now(); // puede ser también LocalDate.now(ZoneId.of("TuZona"))
	}

	@Column(name = "hora_apertura", nullable = false)
	private LocalTime horaApertura;

	@Column(name = "saldo_inicial", nullable = false, precision = 15, scale = 2)
	private BigDecimal saldoInicial;

	@Column(name = "saldo_real_cierre", nullable = false, precision = 15, scale = 2)
	private BigDecimal saldoRealCierre;

	@Column(name = "fecha_cierre")
	private LocalDate fechaCierre;

	@Column(name = "hora_cierre")
	private LocalTime horaCierre;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado", length = 20, nullable = false)
	private EstadoCaja estado; // usa el ENUM del dominio

	@Column(name = "idusuario_apertura", nullable = false, updatable = false)
	private Integer idusuarioApertura;

	@Column(name = "idusuario_cierre", nullable = false, updatable = false)
	private Integer idusuarioCierre;

	@Column(name = "observaciones", nullable = true, length = 250)
	private String observaciones;

	public Integer getIdcaja() {
		return idcaja;
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

	public Integer getIdusuarioApertura() {
		return idusuarioApertura;
	}

	public void setIdusuarioApertura(Integer idusuarioApertura) {
		this.idusuarioApertura = idusuarioApertura;
	}

	public Integer getIdusuarioCierre() {
		return idusuarioCierre;
	}

	public void setIdusuarioCierre(Integer idusuarioCierre) {
		this.idusuarioCierre = idusuarioCierre;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public void setIdcaja(Integer idcaja) {
		this.idcaja = idcaja;
	}

	

}
