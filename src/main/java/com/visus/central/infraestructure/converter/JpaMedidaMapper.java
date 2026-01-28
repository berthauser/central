package com.visus.central.infraestructure.converter;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Medida;
import com.visus.central.infraestructure.persistence.entity.JpaMedidaEntity;

@Component
public class JpaMedidaMapper {

	public Medida toDomain(JpaMedidaEntity entity) {
        if (entity == null) return null;

        Medida model = new Medida();
        model.setId(entity.getId());
        model.setDescripcion(entity.getDescripcion());
        model.setAbreviatura(entity.getAbreviatura());

        return model;
    }

	public JpaMedidaEntity toEntity(Medida model) {
        if (model == null) return null;

        JpaMedidaEntity entity = new JpaMedidaEntity();
        entity.setId(model.getId());
        entity.setDescripcion(model.getDescripcion());
        entity.setAbreviatura(model.getAbreviatura());

        return entity;
    }
	
}