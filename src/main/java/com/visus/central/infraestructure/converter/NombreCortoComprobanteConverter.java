package com.visus.central.infraestructure.converter;

import com.visus.central.infraestructure.persistence.entity.JpaComprobanteEntity;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class NombreCortoComprobanteConverter extends EnumLabelConverter<JpaComprobanteEntity.NombreCorto> {

	protected NombreCortoComprobanteConverter() {
		super(JpaComprobanteEntity.NombreCorto.class);
	}

}
