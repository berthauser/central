package com.visus.central.infraestructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.visus.central.domain.model.TipoBaseComisionable;
import com.visus.central.domain.model.TipoCalculoComision;
import com.visus.central.domain.model.TipoEventoComision;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "reglas_comision")
public class JpaReglaComisionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idregla")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idvendedor")
	private JpaVendedorEntity vendedor;

	@Column(name = "nombre", nullable = false, length = 100)
	private String nombre;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_base_comisionable", nullable = false, length = 30)
	private TipoBaseComisionable tipoBaseComisionable;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_calculo", nullable = false, length = 20)
	private TipoCalculoComision tipoCalculo;

	@Column(name = "valor_calculo", nullable = false, precision = 12, scale = 4)
	private BigDecimal valorCalculo;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_evento", nullable = false, length = 20)
	private TipoEventoComision tipoEvento;

	@Column(name = "incluir_descuentos", nullable = false)
	private Boolean incluirDescuentos;

	@Column(name = "ajustar_devoluciones", nullable = false)
	private Boolean ajustarDevoluciones;

	@Column(name = "ventana_ajuste_dias", nullable = false)
	private Integer ventanaAjusteDias;

	@Column(name = "activo", nullable = false)
	private Boolean activo;

	@Column(name = "fecha_creacion", nullable = false)
	private LocalDate fechaCreacion;

	@OneToMany(mappedBy = "regla", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<JpaTramoComisionEntity> tramos = new ArrayList<>();

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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public TipoBaseComisionable getTipoBaseComisionable() {
		return tipoBaseComisionable;
	}

	public void setTipoBaseComisionable(TipoBaseComisionable tipoBaseComisionable) {
		this.tipoBaseComisionable = tipoBaseComisionable;
	}

	public TipoCalculoComision getTipoCalculo() {
		return tipoCalculo;
	}

	public void setTipoCalculo(TipoCalculoComision tipoCalculo) {
		this.tipoCalculo = tipoCalculo;
	}

	public BigDecimal getValorCalculo() {
		return valorCalculo;
	}

	public void setValorCalculo(BigDecimal valorCalculo) {
		this.valorCalculo = valorCalculo;
	}

	public TipoEventoComision getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(TipoEventoComision tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public Boolean getIncluirDescuentos() {
		return incluirDescuentos;
	}

	public void setIncluirDescuentos(Boolean incluirDescuentos) {
		this.incluirDescuentos = incluirDescuentos;
	}

	public Boolean getAjustarDevoluciones() {
		return ajustarDevoluciones;
	}

	public void setAjustarDevoluciones(Boolean ajustarDevoluciones) {
		this.ajustarDevoluciones = ajustarDevoluciones;
	}

	public Integer getVentanaAjusteDias() {
		return ventanaAjusteDias;
	}

	public void setVentanaAjusteDias(Integer ventanaAjusteDias) {
		this.ventanaAjusteDias = ventanaAjusteDias;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public LocalDate getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDate fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public List<JpaTramoComisionEntity> getTramos() {
		return tramos;
	}

	public void setTramos(List<JpaTramoComisionEntity> tramos) {
		this.tramos = tramos;
	}
}
