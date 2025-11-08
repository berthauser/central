package com.visus.central.infraestructure.converter;

import com.visus.central.infraestructure.persistence.entity.JpaProveedorEntity;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EstadoProveedorConverter extends EnumLabelConverter<JpaProveedorEntity.Estado> {
	
	protected EstadoProveedorConverter() {
		super(JpaProveedorEntity.Estado.class);
	}

}
