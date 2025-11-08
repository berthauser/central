package com.visus.central.infraestructure.converter;

import com.visus.central.infraestructure.persistence.entity.JpaClienteEntity;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SFiscalClienteConverter extends EnumLabelConverter<JpaClienteEntity.SituacionFiscal> {
	
	protected SFiscalClienteConverter() {
		super(JpaClienteEntity.SituacionFiscal.class);
	}
}
