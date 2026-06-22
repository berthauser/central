package com.visus.central.infraestructure.converter;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Comprobante;
import com.visus.central.infraestructure.persistence.entity.JpaComprobanteEntity;


@Component
public class JpaComprobanteMapper {

	public Comprobante toModel(JpaComprobanteEntity entity) {
        if (entity == null) return null;
        Comprobante model = new Comprobante();
        model.setId(entity.getId());
        model.setNombreLargo(entity.getNombreLargo());
        model.setNombreCorto(entity.getNombreCorto());
        model.setNumeroInicial(entity.getNumeroInicial());
        model.setNumeroFinal(entity.getNumeroFinal());
        model.setNumeroActual(entity.getNumeroActual());   // ← agregado
        model.setActivo(entity.getActivo());               // ← agregado
        model.setColumna(entity.getColumna());
        return model;
    }

    public JpaComprobanteEntity toEntity(Comprobante model) {
        if (model == null) return null;
        JpaComprobanteEntity entity = new JpaComprobanteEntity();
        entity.setId(model.getId());
        entity.setNombreLargo(model.getNombreLargo());
        entity.setNombreCorto(model.getNombreCorto());
        entity.setNumeroInicial(model.getNumeroInicial());
        entity.setNumeroFinal(model.getNumeroFinal());
        entity.setNumeroActual(model.getNumeroActual());   // ← agregado
        entity.setActivo(model.getActivo());               // ← agregado
        entity.setColumna(model.getColumna());
        return entity;
    }

}
