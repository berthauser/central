package com.visus.central.infraestructure.converter;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Lista;
import com.visus.central.domain.model.ListaPorcentual;
import com.visus.central.infraestructure.persistence.entity.JpaListaEntity;
import com.visus.central.infraestructure.persistence.entity.JpaListaPorcentualEntity;
import com.visus.central.infraestructure.persistence.entity.JpaPorcentualEntity;

@Component
public class JpaListaMapper {

    private final JpaPorcentualMapper porcentualMapper;

    public JpaListaMapper(JpaPorcentualMapper porcentualMapper) {
        this.porcentualMapper = porcentualMapper;
    }

    public Lista toDomain(JpaListaEntity entity) {
        if (entity == null) return null;

        Lista model = new Lista();
        model.setId(entity.getId());
        model.setDescripcion(entity.getDescripcion());

        if (entity.getItems() != null) {
            model.setItems(entity.getItems().stream()
                .map(this::toListaPorcentualDomain)
                .collect(Collectors.toList()));
        }

        return model;
    }

    public JpaListaEntity toEntity(Lista model) {
        if (model == null) return null;

        JpaListaEntity entity = new JpaListaEntity();
        entity.setId(model.getId());
        entity.setDescripcion(model.getDescripcion());

        if (model.getItems() != null) {
            entity.setItems(model.getItems().stream()
                .map(item -> toListaPorcentualEntity(item, entity))
                .collect(Collectors.toList()));
        }

        return entity;
    }

    private ListaPorcentual toListaPorcentualDomain(JpaListaPorcentualEntity entity) {
        ListaPorcentual model = new ListaPorcentual();
        model.setId(entity.getId());
        model.setPorcentual(porcentualMapper.toDomain(entity.getPorcentual()));
        model.setPorcentualId(entity.getPorcentual() != null ? entity.getPorcentual().getId() : null);
        return model;
    }

    private JpaListaPorcentualEntity toListaPorcentualEntity(ListaPorcentual model, JpaListaEntity parent) {
        JpaListaPorcentualEntity entity = new JpaListaPorcentualEntity();
        entity.setId(model.getId());
        entity.setLista(parent);

        JpaPorcentualEntity pEntity = new JpaPorcentualEntity();
        pEntity.setId(model.getPorcentualId());
        entity.setPorcentual(pEntity);

        return entity;
    }
}
