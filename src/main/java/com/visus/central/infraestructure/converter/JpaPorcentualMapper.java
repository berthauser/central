package com.visus.central.infraestructure.converter;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Porcentual;
import com.visus.central.infraestructure.persistence.entity.JpaPorcentualEntity;

@Component
public class JpaPorcentualMapper {

    public Porcentual toDomain(JpaPorcentualEntity entity) {
        if (entity == null) return null;

        Porcentual model = new Porcentual();
        model.setId(entity.getId());
        model.setDescripcion(entity.getDescripcion());
        model.setPorcentual(entity.getPorcentual());
        model.setInicioVigencia(entity.getInicioVigencia());
        model.setFinVigencia(entity.getFinVigencia());
        model.setClasificacion(entity.getClasificacion());
        return model;
    }

    public JpaPorcentualEntity toEntity(Porcentual model) {
        if (model == null) return null;

        JpaPorcentualEntity entity = new JpaPorcentualEntity();
        entity.setId(model.getId());
        entity.setDescripcion(model.getDescripcion());
        entity.setPorcentual(model.getPorcentual());
        entity.setInicioVigencia(model.getInicioVigencia());
        entity.setFinVigencia(model.getFinVigencia());
        entity.setClasificacion(model.getClasificacion());
        return entity;
    }
}
