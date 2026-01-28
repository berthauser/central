package com.visus.central.infraestructure.converter;

import com.visus.central.infraestructure.persistence.entity.JpaProveedorEntity;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TDocumentoProveedorConverter extends EnumLabelConverter<JpaProveedorEntity.TipoDocumento> {
	
	protected TDocumentoProveedorConverter() {
		super(JpaProveedorEntity.TipoDocumento.class);
	}

}
