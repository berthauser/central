package com.visus.central.infraestructure.converter;

import com.visus.central.infraestructure.persistence.entity.JpaGrupoFamEntity;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EstadoGrupoFamConverter extends EnumLabelConverter<JpaGrupoFamEntity.Estado> {
	
	protected EstadoGrupoFamConverter() {
		super(JpaGrupoFamEntity.Estado.class);
	}

}
