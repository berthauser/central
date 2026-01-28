package com.visus.central.infraestructure.converter;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Rubro;
import com.visus.central.infraestructure.persistence.entity.JpaRubroEntity;

@Component
public class JpaRubroMapper {
	
	public Rubro toDomain(JpaRubroEntity entity) {
        Rubro model = new Rubro();
        model.setId(entity.getId());
        model.setDescripcion(entity.getDescripcion());
        return model;
    }

    public JpaRubroEntity toEntity(Rubro model) {
        JpaRubroEntity entity = new JpaRubroEntity();
        entity.setId(model.getId());
        entity.setDescripcion(model.getDescripcion());
        return entity;
    }

}
