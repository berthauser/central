package com.visus.central.infraestructure.converter;

import com.visus.central.infraestructure.persistence.entity.JpaArticuloEntity;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EstadoArticuloConverter extends EnumLabelConverter<JpaArticuloEntity.Estado> {

	protected EstadoArticuloConverter() {
		super(JpaArticuloEntity.Estado.class);
	}
	
}
