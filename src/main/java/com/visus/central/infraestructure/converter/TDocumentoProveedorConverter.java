package com.visus.central.infraestructure.converter;

import com.visus.central.infraestructure.persistence.entity.JpaProveedorEntity;

public class TDocumentoProveedorConverter extends EnumLabelConverter<JpaProveedorEntity.TipoDocumento> {
	
	protected TDocumentoProveedorConverter() {
		super(JpaProveedorEntity.TipoDocumento.class);
	}

}
