package com.visus.central.domain.model;

import java.util.Objects;

import com.visus.central.infraestructure.persistence.entity.JpaDomicilioEntity.TipoDomicilio;

public class Domicilio {

	private Integer id;
	private TipoDomicilio tipoDomicilio;
	private String calle;
	private Short numero;
	private String barrio;
	private String manzana;
	private String casa;
	private String sector;
	private String depto;
	private String oficina;
	private String lote;
	private Integer idLocalidad;
	private Integer idCliente;
	private Integer idVendedor;
	private Vendedor vendedor;
	private Localidad localidad;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TipoDomicilio getTipoDomicilio() {
		return tipoDomicilio;
	}

	public void setTipoDomicilio(TipoDomicilio tipoDomicilio) {
		this.tipoDomicilio = tipoDomicilio;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public Short getNumero() {
		return numero;
	}

	public void setNumero(Short numero) {
		this.numero = numero;
	}

	public String getBarrio() {
		return barrio;
	}

	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}

	public String getManzana() {
		return manzana;
	}

	public void setManzana(String manzana) {
		this.manzana = manzana;
	}

	public String getCasa() {
		return casa;
	}

	public void setCasa(String casa) {
		this.casa = casa;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getDepto() {
		return depto;
	}

	public void setDepto(String depto) {
		this.depto = depto;
	}

	public String getOficina() {
		return oficina;
	}

	public void setOficina(String oficina) {
		this.oficina = oficina;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public Integer getIdLocalidad() {
		return idLocalidad;
	}

	public void setIdLocalidad(Integer idLocalidad) {
		this.idLocalidad = idLocalidad;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public Integer getIdVendedor() {
		return idVendedor;
	}

	public void setIdVendedor(Integer idVendedor) {
		this.idVendedor = idVendedor;
	}

	public Vendedor getVendedor() {
		return vendedor;
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	public Localidad getLocalidad() {
		return localidad;
	}

	public void setLocalidad(Localidad localidad) {
		this.localidad = localidad;
	}

	@Override
	public int hashCode() {
		return Objects.hash(barrio, calle, casa, depto, id, idCliente, idLocalidad, idVendedor, localidad, lote,
				manzana, numero, oficina, sector, tipoDomicilio, vendedor);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Domicilio other = (Domicilio) obj;
		return Objects.equals(barrio, other.barrio) && Objects.equals(calle, other.calle)
				&& Objects.equals(casa, other.casa) && Objects.equals(depto, other.depto)
				&& Objects.equals(id, other.id) && Objects.equals(idCliente, other.idCliente)
				&& Objects.equals(idLocalidad, other.idLocalidad) && Objects.equals(idVendedor, other.idVendedor)
				&& Objects.equals(localidad, other.localidad) && Objects.equals(lote, other.lote)
				&& Objects.equals(manzana, other.manzana) && Objects.equals(numero, other.numero)
				&& Objects.equals(oficina, other.oficina) && Objects.equals(sector, other.sector)
				&& tipoDomicilio == other.tipoDomicilio && Objects.equals(vendedor, other.vendedor);
	}

	@Override
	public String toString() {
		return "Domicilio [id=" + id + ", tipoDomicilio=" + tipoDomicilio + ", calle=" + calle + ", numero=" + numero
				+ ", barrio=" + barrio + ", manzana=" + manzana + ", casa=" + casa + ", sector=" + sector + ", depto="
				+ depto + ", oficina=" + oficina + ", lote=" + lote + ", idLocalidad=" + idLocalidad + ", idCliente="
				+ idCliente + ", idVendedor=" + idVendedor + ", vendedor=" + vendedor + ", localidad=" + localidad
				+ "]";
	}

}
