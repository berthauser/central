package com.visus.central.infraestructure.converter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.visus.central.domain.model.Banco;
import com.visus.central.infraestructure.persistence.entity.JpaBancoEntity;

public class JpaBancoMapper {
	
	public static Banco toModel(JpaBancoEntity entity) {
        if (entity == null) return null;
        
        Banco model = new Banco();
        model.setId(entity.getId());
        model.setNombre(entity.getNombre());
        model.setIdBcoCen(entity.getIdBcoCen());
        return model;
    }
    
    public static JpaBancoEntity toEntity(Banco model) {
        if (model == null) return null;
        
        JpaBancoEntity entity = new JpaBancoEntity();
        entity.setId(model.getId());
        entity.setNombre(model.getNombre());
        entity.setIdBcoCen(model.getIdBcoCen());
        return entity;
    }
    
 // Métodos para listas
    public static List<Banco> toModelList(List<JpaBancoEntity> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
            .map(JpaBancoMapper::toModel)
            .collect(Collectors.toList());
    }
    
    public static List<JpaBancoEntity> toEntityList(List<Banco> models) {
        if (models == null) return Collections.emptyList();
        return models.stream()
            .map(JpaBancoMapper::toEntity)
            .collect(Collectors.toList());
    }

}
