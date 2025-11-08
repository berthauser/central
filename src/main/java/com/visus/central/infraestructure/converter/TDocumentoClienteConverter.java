package com.visus.central.infraestructure.converter;

import com.visus.central.infraestructure.persistence.entity.JpaClienteEntity;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TDocumentoClienteConverter extends EnumLabelConverter<JpaClienteEntity.TipoDocumento> {
	
	protected TDocumentoClienteConverter() {
		super(JpaClienteEntity.TipoDocumento.class);
	}

}
