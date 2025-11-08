package com.visus.central.infraestructure.converter;

import com.visus.central.domain.model.Domicilio;
import com.visus.central.infraestructure.persistence.entity.JpaClienteEntity;
import com.visus.central.infraestructure.persistence.entity.JpaDomicilioEntity;
import com.visus.central.infraestructure.persistence.entity.JpaVendedorEntity;

public class JpaDomicilioMapper {
	
	public static Domicilio toModel(JpaDomicilioEntity entity) {
        if (entity == null) return null;

        Domicilio model = new Domicilio();
        model.setId(entity.getId());
        model.setTipoDomicilio(entity.getTipoDomicilio());
        model.setCalle(entity.getCalle());
        model.setNumero(entity.getNumero());
        model.setBarrio(entity.getBarrio());
        model.setManzana(entity.getManzana());
        model.setCasa(entity.getCasa());
        model.setSector(entity.getSector());
        model.setDepto(entity.getDepto());
        model.setOficina(entity.getOficina());
        model.setLote(entity.getLote());
        model.setIdLocalidad(entity.getIdLocalidad());
        model.setIdCliente(entity.getCliente() != null ? entity.getCliente().getId() : null);
        model.setIdVendedor(entity.getVendedor() != null ? entity.getVendedor().getId() : null);

//     // Extraer ID del vendedor
//        if (entity.getVendedor() != null) {
//            model.setIdVendedor(entity.getVendedor().getId());
//        }
        
        return model;
    }

    public static JpaDomicilioEntity toEntity(Domicilio model) {
        if (model == null) return null;

        JpaDomicilioEntity entity = new JpaDomicilioEntity();
        entity.setId(model.getId());
        entity.setTipoDomicilio(model.getTipoDomicilio());
        entity.setCalle(model.getCalle());
        entity.setNumero(model.getNumero());
        entity.setBarrio(model.getBarrio());
        entity.setManzana(model.getManzana());
        entity.setCasa(model.getCasa());
        entity.setSector(model.getSector());
        entity.setDepto(model.getDepto());
        entity.setOficina(model.getOficina());
        entity.setLote(model.getLote());
        entity.setIdLocalidad(model.getIdLocalidad());
        
        if (model.getIdCliente() != null) {
            JpaClienteEntity cliente = new JpaClienteEntity();
            cliente.setId(model.getIdCliente());
            entity.setCliente(cliente);
        }

        // Asociación con vendedor
        if (model.getIdVendedor() != null) {
            JpaVendedorEntity vendedor = new JpaVendedorEntity();
            vendedor.setId(model.getIdVendedor());
            entity.setVendedor(vendedor);
        }

        return entity;
    }


}
