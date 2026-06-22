package com.visus.central.domain.model;

public class ClienteComboDTO {

	private Integer id;
	private String nombre;
	private String tipo;
	private Integer idClienteAsociado;
	private Integer idGrupoFam;

	public ClienteComboDTO(Integer id, String nombre, String tipo, Integer idClienteAsociado, Integer idGrupoFam) {
		this.id = id;
		this.nombre = nombre;
		this.tipo = tipo;
		this.idClienteAsociado = idClienteAsociado;
		this.idGrupoFam = idGrupoFam;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getIdClienteAsociado() {
		return idClienteAsociado;
	}

	public void setIdClienteAsociado(Integer idClienteAsociado) {
		this.idClienteAsociado = idClienteAsociado;
	}

	public Integer getIdGrupoFam() {
		return idGrupoFam;
	}

	public void setIdGrupoFam(Integer idGrupoFam) {
		this.idGrupoFam = idGrupoFam;
	}

}
