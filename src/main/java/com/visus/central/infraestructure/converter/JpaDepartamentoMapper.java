package com.visus.central.infraestructure.converter;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Departamento;
import com.visus.central.infraestructure.persistence.entity.JpaDepartamentoEntity;

@Component
public class JpaDepartamentoMapper {
	
	public Departamento toDomain(JpaDepartamentoEntity entity) {
        Departamento model = new Departamento();
        model.setId(entity.getId());
        model.setNombre(entity.getNombre());
        model.setProvincia(entity.getProvincia());
        return model;
    }

    public JpaDepartamentoEntity toEntity(Departamento model) {
        JpaDepartamentoEntity entity = new JpaDepartamentoEntity();
        entity.setId(model.getId());
        entity.setNombre(model.getNombre());
        entity.setProvincia(model.getProvincia());
        return entity;
    }
	
}