package com.visus.central.infraestructure.converter;

import com.visus.central.infraestructure.persistence.entity.JpaVendedorEntity;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SFiscalVendedorConverter extends EnumLabelConverter<JpaVendedorEntity.SituacionFiscal> {

	protected SFiscalVendedorConverter() {
		super(JpaVendedorEntity.SituacionFiscal.class);
	}

}
