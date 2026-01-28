package com.visus.central.infraestructure.converter;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Presentacion;
import com.visus.central.infraestructure.persistence.entity.JpaPresentacionEntity;

@Component
public class JpaPresentacionMapper {

	public Presentacion toDomain(JpaPresentacionEntity entity) {
		Presentacion model = new Presentacion();
        model.setId(entity.getId());
        model.setDescripcion(entity.getDescripcion());
        return model;
    }

    public JpaPresentacionEntity toEntity(Presentacion model) {
        JpaPresentacionEntity entity = new JpaPresentacionEntity();
        entity.setId(model.getId());
        entity.setDescripcion(model.getDescripcion());
        return entity;
    }
}
