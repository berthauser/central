package com.visus.central.infraestructure.converter;

import com.visus.central.infraestructure.persistence.entity.JpaGrupoFamEntity;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TDocumentoGrupoFamConverter extends EnumLabelConverter<JpaGrupoFamEntity.TipoDocumento> {
	
	protected TDocumentoGrupoFamConverter() {
		super(JpaGrupoFamEntity.TipoDocumento.class);
	}
}
