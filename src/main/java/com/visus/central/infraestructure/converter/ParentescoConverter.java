package com.visus.central.infraestructure.converter;

import com.visus.central.infraestructure.persistence.entity.JpaGrupoFamEntity;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ParentescoConverter extends EnumLabelConverter<JpaGrupoFamEntity.Parentesco> {
	
	protected ParentescoConverter() {
		super(JpaGrupoFamEntity.Parentesco.class);
	}

}
