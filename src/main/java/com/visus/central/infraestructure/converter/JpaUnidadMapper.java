package com.visus.central.infraestructure.converter;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Unidad;
import com.visus.central.infraestructure.persistence.entity.JpaUnidadEntity;

@Component
public class JpaUnidadMapper {

	public Unidad toModel(JpaUnidadEntity entity) {
		if (entity == null) return null;
		Unidad model = new Unidad();
		model.setId(entity.getId());
		model.setIdPresentacion(entity.getIdPresentacion());
		model.setMedida(entity.getMedida());
		return model;
	}
}
