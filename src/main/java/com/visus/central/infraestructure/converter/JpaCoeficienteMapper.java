package com.visus.central.infraestructure.converter;

import com.visus.central.domain.model.Coeficiente;
import com.visus.central.infraestructure.persistence.entity.JpaCoeficienteEntity;

public class JpaCoeficienteMapper {

	public static Coeficiente toModel(JpaCoeficienteEntity entity) {
        if (entity == null) return null;
        
        Coeficiente model = new Coeficiente();
        model.setId(entity.getId());
        model.setDescripcion(entity.getDescripcion());
        model.setCoeficiente(entity.getCoeficiente());
        model.setCuotas(entity.getCuotas());
        return model;
    }
    
    public static JpaCoeficienteEntity toEntity(Coeficiente model) {
        if (model == null) return null;
        
        JpaCoeficienteEntity entity = new JpaCoeficienteEntity();
        entity.setId(model.getId());
        entity.setDescripcion(model.getDescripcion());
        entity.setCoeficiente(model.getCoeficiente());
        entity.setCuotas(model.getCuotas());
        return entity;
    }
    
}
