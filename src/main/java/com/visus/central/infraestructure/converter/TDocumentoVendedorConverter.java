package com.visus.central.infraestructure.converter;

import com.visus.central.infraestructure.persistence.entity.JpaVendedorEntity;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TDocumentoVendedorConverter extends EnumLabelConverter<JpaVendedorEntity.TipoDocumento> {

	protected TDocumentoVendedorConverter() {
		super(JpaVendedorEntity.TipoDocumento.class);
	}

}
