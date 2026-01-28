package com.visus.central.infraestructure.converter;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Linea;
import com.visus.central.domain.model.Rubro;
import com.visus.central.infraestructure.persistence.entity.JpaLineaEntity;
import com.visus.central.infraestructure.persistence.entity.JpaRubroEntity;

@Component
public class JpaLineaMapper {
	
	public Linea toDomain(JpaLineaEntity entity) {
        if (entity == null) return null;

        Linea model = new Linea();
        model.setIdLinea(entity.getIdLinea());
        model.setDescripcion(entity.getDescripcion());

        if (entity.getRubro() != null) {
            Rubro d = new Rubro();
            d.setId(entity.getRubro().getId());
            d.setDescripcion(entity.getRubro().getDescripcion());
            model.setRubro(d);
        }

        return model;
    }

	public JpaLineaEntity toEntity(Linea model) {
        if (model == null) return null;

        JpaLineaEntity entity = new JpaLineaEntity();
        entity.setIdLinea(model.getIdLinea());
        entity.setDescripcion(model.getDescripcion());

        if (model.getRubro() != null) {
            JpaRubroEntity d = new JpaRubroEntity();
            d.setId(model.getRubro().getId());
            entity.setRubro(d);
        }

        return entity;
    }

}
