package com.visus.central.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Cliente {
	
	private Integer id;
    private String nombreFantasia;
    private String nombreCliente;
    private Sexo sexo;
    private String telefonoFijo;
    private String telefonoMovil;
    private TipoDocumento tipoDocumento;
    private Long numero;
    private String email;
    private LocalDate fechaIngreso;
    private LocalDate fechaActualizacion;
    private LocalDate fechaBaja;
    private LocalDate fechaUltimaCompra;
    private Short limiteFacturasVencidas;
    private BigDecimal limiteCredito;
    private BigDecimal pagoMinimo;
    private SituacionFiscal situacionFiscal;
    private BigDecimal saldoCtaCte;
    private String observaciones;
    private Estado estado;
    private List<Domicilio> domicilios;
    private List<GrupoFam> grupoFam;
	
    public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getNombreFantasia() {
		return nombreFantasia;
	}
	
	public void setNombreFantasia(String nombreFantasia) {
		this.nombreFantasia = nombreFantasia;
	}
	
	public String getNombreCliente() {
		return nombreCliente;
	}
	
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	
	public Sexo getSexo() {
		return sexo;
	}
	
	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}
	
	public String getTelefonoFijo() {
		return telefonoFijo;
	}
	
	public void setTelefonoFijo(String telefonoFijo) {
		this.telefonoFijo = telefonoFijo;
	}
	
	public String getTelefonoMovil() {
		return telefonoMovil;
	}
	
	public void setTelefonoMovil(String telefonoMovil) {
		this.telefonoMovil = telefonoMovil;
	}
	
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}
	
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	
	public Long getNumero() {
		return numero;
	}
	
	public void setNumero(Long numero) {
		this.numero = numero;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public LocalDate getFechaIngreso() {
		return fechaIngreso;
	}
	
	public void setFechaIngreso(LocalDate fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	
	public LocalDate getFechaActualizacion() {
		return fechaActualizacion;
	}
	
	public void setFechaActualizacion(LocalDate fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}
	
	public LocalDate getFechaBaja() {
		return fechaBaja;
	}
	
	public void setFechaBaja(LocalDate fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
	
	public LocalDate getFechaUltimaCompra() {
		return fechaUltimaCompra;
	}
	
	public void setFechaUltimaCompra(LocalDate fechaUltimaCompra) {
		this.fechaUltimaCompra = fechaUltimaCompra;
	}
	
	public Short getLimiteFacturasVencidas() {
		return limiteFacturasVencidas;
	}
	
	public void setLimiteFacturasVencidas(Short limiteFacturasVencidas) {
		this.limiteFacturasVencidas = limiteFacturasVencidas;
	}
	
	public BigDecimal getLimiteCredito() {
		return limiteCredito;
	}
	
	public void setLimiteCredito(BigDecimal limiteCredito) {
		this.limiteCredito = limiteCredito;
	}
	
	public BigDecimal getPagoMinimo() {
		return pagoMinimo;
	}
	
	public void setPagoMinimo(BigDecimal pagoMinimo) {
		this.pagoMinimo = pagoMinimo;
	}
	
	public SituacionFiscal getSituacionFiscal() {
		return situacionFiscal;
	}
	
	public void setSituacionFiscal(SituacionFiscal situacionFiscal) {
		this.situacionFiscal = situacionFiscal;
	}
	
	public BigDecimal getSaldoCtaCte() {
		return saldoCtaCte;
	}
	
	public void setSaldoCtaCte(BigDecimal saldoCtaCte) {
		this.saldoCtaCte = saldoCtaCte;
	}
	
	public String getObservaciones() {
		return observaciones;
	}
	
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public Estado getEstado() {
		return estado;
	}
	
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	
	public List<Domicilio> getDomicilios() {
		return domicilios;
	}
	
	public void setDomicilios(List<Domicilio> domicilios) {
		this.domicilios = domicilios;
	}
	
	public List<GrupoFam> getGrupoFam() {
		return grupoFam;
	}
	
	public void setGrupoFam(List<GrupoFam> grupoFam) {
		this.grupoFam = grupoFam;
	}
	
	public boolean isHabilitado() {
	    return this.estado == Estado.Habilitado;
	}

	@Override
	public int hashCode() {
		return Objects.hash(tipoDocumento, domicilios, email, estado, fechaActualizacion, fechaBaja, fechaIngreso,
				fechaUltimaCompra, grupoFam, id, limiteCredito, limiteFacturasVencidas, nombreCliente, nombreFantasia,
				numero, observaciones, pagoMinimo, saldoCtaCte, sexo, situacionFiscal, telefonoFijo, telefonoMovil);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		return tipoDocumento == other.tipoDocumento && Objects.equals(domicilios, other.domicilios)
				&& Objects.equals(email, other.email) && estado == other.estado
				&& Objects.equals(fechaActualizacion, other.fechaActualizacion)
				&& Objects.equals(fechaBaja, other.fechaBaja) && Objects.equals(fechaIngreso, other.fechaIngreso)
				&& Objects.equals(fechaUltimaCompra, other.fechaUltimaCompra)
				&& Objects.equals(grupoFam, other.grupoFam) && Objects.equals(id, other.id)
				&& Objects.equals(limiteCredito, other.limiteCredito)
				&& Objects.equals(limiteFacturasVencidas, other.limiteFacturasVencidas)
				&& Objects.equals(nombreCliente, other.nombreCliente)
				&& Objects.equals(nombreFantasia, other.nombreFantasia) && Objects.equals(numero, other.numero)
				&& Objects.equals(observaciones, other.observaciones) && Objects.equals(pagoMinimo, other.pagoMinimo)
				&& Objects.equals(saldoCtaCte, other.saldoCtaCte) && sexo == other.sexo
				&& situacionFiscal == other.situacionFiscal && Objects.equals(telefonoFijo, other.telefonoFijo)
				&& Objects.equals(telefonoMovil, other.telefonoMovil);
	}

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", nombreFantasia=" + nombreFantasia + ", nombreCliente=" + nombreCliente
				+ ", sexo=" + sexo + ", telefonoFijo=" + telefonoFijo + ", telefonoMovil=" + telefonoMovil
				+ ", documento=" + tipoDocumento + ", numero=" + numero + ", email=" + email + ", fechaIngreso="
				+ fechaIngreso + ", fechaActualizacion=" + fechaActualizacion + ", fechaBaja=" + fechaBaja
				+ ", fechaUltimaCompra=" + fechaUltimaCompra + ", limiteFacturasVencidas=" + limiteFacturasVencidas
				+ ", limiteCredito=" + limiteCredito + ", pagoMinimo=" + pagoMinimo + ", situacionFiscal="
				+ situacionFiscal + ", saldoCtaCte=" + saldoCtaCte + ", observaciones=" + observaciones + ", estado="
				+ estado + ", domicilios=" + domicilios + ", grupoFam=" + grupoFam + "]";
	}
    

}
