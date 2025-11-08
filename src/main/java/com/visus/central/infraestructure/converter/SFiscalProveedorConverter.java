package com.visus.central.infraestructure.converter;

import com.visus.central.infraestructure.persistence.entity.JpaProveedorEntity;

public class SFiscalProveedorConverter extends EnumLabelConverter<JpaProveedorEntity.SituacionFiscal> {
	
	protected SFiscalProveedorConverter() {
		super(JpaProveedorEntity.SituacionFiscal.class);
	}

}
