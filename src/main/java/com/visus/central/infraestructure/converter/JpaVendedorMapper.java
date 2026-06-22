package com.visus.central.infraestructure.converter;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Vendedor;
import com.visus.central.infraestructure.persistence.entity.JpaVendedorEntity;

@Component
public class JpaVendedorMapper {
	public Vendedor toModel(JpaVendedorEntity entity) {
        if (entity == null) return null;

        Vendedor model = new Vendedor();
        model.setId(entity.getId());
        model.setNumero(entity.getNumero());
        model.setNombre(entity.getNombre());
        model.setTelefono(entity.getTelefono());
        model.setEmail(entity.getEmail());
        model.setSituacionFiscal(entity.getSituacionFiscal());
        model.setTipoDocumento(entity.getTipoDocumento());

        return model;
    }

    public JpaVendedorEntity toEntity(Vendedor model) {
        if (model == null) return null;

        JpaVendedorEntity entity = new JpaVendedorEntity();
        entity.setId(model.getId());
        entity.setNumero(model.getNumero());
        entity.setNombre(model.getNombre());
        entity.setTelefono(model.getTelefono());
        entity.setEmail(model.getEmail());
        entity.setSituacionFiscal(model.getSituacionFiscal());
        entity.setTipoDocumento(model.getTipoDocumento());

        return entity;
    }

}
