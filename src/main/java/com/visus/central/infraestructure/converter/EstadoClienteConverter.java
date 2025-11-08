package com.visus.central.infraestructure.converter;

import com.visus.central.infraestructure.persistence.entity.JpaClienteEntity;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EstadoClienteConverter extends EnumLabelConverter<JpaClienteEntity.Estado> {
	
	protected EstadoClienteConverter() {
		super(JpaClienteEntity.Estado.class);
	}
}
