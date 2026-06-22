package com.visus.central.infraestructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.visus.central.domain.model.EstadoVenta;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ventas")
public class JpaVentaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idventa")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "idcomprobante", referencedColumnName = "idcomprobante", foreignKey = @ForeignKey(name = "fk_ventas_comprobante"))
    private JpaComprobanteEntity comprobante;
    
    @ManyToOne
    @JoinColumn(name = "idcliente", referencedColumnName = "idcliente", foreignKey = @ForeignKey(name = "fk_ventas_cliente"))
    private JpaClienteEntity cliente;
    
    @ManyToOne
    @JoinColumn(name = "idvendedor", referencedColumnName = "idvendedor", foreignKey = @ForeignKey(name = "fk_ventas_vendedor"))
    private JpaVendedorEntity vendedor;
    
    @Column(name = "fechaventa")
    private LocalDate fechaVenta;
    
    @Column(name = "numerocomprobante", nullable = false)
    private Integer numeroComprobante;
    
    // Solo el ID del grupo familiar (NULL si es titular)
    @Column(name = "idgrupofam")
    private Integer grupoFamiliarId;
    
    @Column(name = "esbonificado")
    private Boolean esBonificado;
    
    @Column(name = "bonificacion", precision = 12, scale = 2)
    private BigDecimal bonificacion;
    
    @Column(name = "observaciones", nullable = true, length = 250)
    private String observaciones;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20)
    private EstadoVenta estado;  // Usa el enum del dominio (model) directamente
    
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JpaItemEntity> items = new ArrayList<>();
    
    // Getters y Setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public JpaComprobanteEntity getComprobante() {
		return comprobante;
	}

	public void setComprobante(JpaComprobanteEntity comprobante) {
		this.comprobante = comprobante;
	}

	public JpaClienteEntity getCliente() {
		return cliente;
	}

	public void setCliente(JpaClienteEntity cliente) {
		this.cliente = cliente;
	}

	public JpaVendedorEntity getVendedor() {
		return vendedor;
	}

	public void setVendedor(JpaVendedorEntity vendedor) {
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

	public List<JpaItemEntity> getItems() {
		return items;
	}

	public void setItems(List<JpaItemEntity> items) {
		this.items = items;
	}
}