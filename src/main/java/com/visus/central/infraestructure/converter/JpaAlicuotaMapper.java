package com.visus.central.infraestructure.converter;

import com.visus.central.domain.model.Alicuota;
import com.visus.central.infraestructure.persistence.entity.JpaAlicuotaEntity;

public class JpaAlicuotaMapper {
	
	public static Alicuota toModel(JpaAlicuotaEntity entity) {
        if (entity == null) return null;
        
        Alicuota model = new Alicuota();
        model.setId(entity.getId());
        model.setDescripcion(entity.getDescripcion());
        model.setGravamen(entity.getGravamen());
        return model;
    }
    
    public static JpaAlicuotaEntity toEntity(Alicuota model) {
        if (model == null) return null;
        
        JpaAlicuotaEntity entity = new JpaAlicuotaEntity();
        entity.setId(model.getId());
        entity.setDescripcion(model.getDescripcion());
        entity.setGravamen(model.getGravamen());
        return entity;
    }

}
