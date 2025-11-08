package com.visus.central.infraestructure.converter;

import com.visus.central.infraestructure.persistence.entity.JpaDomicilioEntity;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoDomicilioConverter extends EnumLabelConverter<JpaDomicilioEntity.TipoDomicilio> {

	public TipoDomicilioConverter() {
		super(JpaDomicilioEntity.TipoDomicilio.class);
	}

}
