package com.visus.central.infraestructure.converter;

import com.visus.central.infraestructure.persistence.entity.JpaClienteEntity;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SexoClienteConverter extends EnumLabelConverter<JpaClienteEntity.Sexo> {
	
	protected SexoClienteConverter() {
		super(JpaClienteEntity.Sexo.class);
	}

}
