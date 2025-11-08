package com.visus.central.infraestructure.converter;

import com.visus.central.infraestructure.persistence.entity.JpaComprobanteEntity;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ColumnaComprobanteConverter extends EnumLabelConverter<JpaComprobanteEntity.Columna> {

	protected ColumnaComprobanteConverter() {
		super(JpaComprobanteEntity.Columna.class);
	}

}
