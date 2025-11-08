package com.visus.central.infraestructure.converter;

import com.visus.central.infraestructure.persistence.entity.JpaDepartamentoEntity;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ProvinciaConverter extends EnumLabelConverter<JpaDepartamentoEntity.Provincia> {
	
	public ProvinciaConverter() {
        super(JpaDepartamentoEntity.Provincia.class);
    }

}
