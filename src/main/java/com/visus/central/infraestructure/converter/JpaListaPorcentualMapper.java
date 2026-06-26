package com.visus.central.infraestructure.converter;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.ListaPorcentual;
import com.visus.central.infraestructure.persistence.entity.JpaListaPorcentualEntity;

@Component
public class JpaListaPorcentualMapper {

    private final JpaPorcentualMapper porcentualMapper;

    public JpaListaPorcentualMapper(JpaPorcentualMapper porcentualMapper) {
        this.porcentualMapper = porcentualMapper;
    }

    public ListaPorcentual toDomain(JpaListaPorcentualEntity entity) {
        if (entity == null) return null;

        ListaPorcentual model = new ListaPorcentual();
        model.setId(entity.getId());
        model.setListaId(entity.getLista() != null ? entity.getLista().getId() : null);
        model.setPorcentual(porcentualMapper.toDomain(entity.getPorcentual()));
        model.setPorcentualId(entity.getPorcentual() != null ? entity.getPorcentual().getId() : null);
        return model;
    }

    public JpaListaPorcentualEntity toEntity(ListaPorcentual model) {
        if (model == null) return null;

        JpaListaPorcentualEntity entity = new JpaListaPorcentualEntity();
        entity.setId(model.getId());
        return entity;
    }
}
