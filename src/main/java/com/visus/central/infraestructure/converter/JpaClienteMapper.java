package com.visus.central.infraestructure.converter;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Cliente;
import com.visus.central.infraestructure.persistence.entity.JpaClienteEntity;

@Component
public class JpaClienteMapper {
	
	public Cliente toModel(JpaClienteEntity entity) {
		
		if (entity == null) return null;
		
		Cliente model = new Cliente();
        model.setId(entity.getId());
        model.setNombreFantasia(entity.getNombreFantasia());
        model.setNombreCliente(entity.getNombreCliente());
        model.setSexo(entity.getSexo() != null ? entity.getSexo() : null);
        model.setTelefonoFijo(entity.getTelefonoFijo());
        model.setTelefonoMovil(entity.getTelefonoMovil());
        model.setNumero(entity.getNumero());
        model.setEmail(entity.getEmail());
        model.setFechaIngreso(entity.getFechaIngreso());
        model.setFechaActualizacion(entity.getFechaActualizacion());
        model.setFechaBaja(entity.getFechaBaja());
        model.setFechaUltimaCompra(entity.getFechaUltimaCompra());
        model.setLimiteFacturasVencidas(entity.getLimiteFacturasVencidas());
        model.setLimiteCredito(entity.getLimiteCredito());
        model.setPagoMinimo(entity.getPagoMinimo());
        model.setSituacionFiscal(entity.getSituacionFiscal());
        model.setSaldoCtaCte(entity.getSaldoCtaCte());
        model.setObservaciones(entity.getObservaciones());
        model.setTipoDocumento(entity.getTipoDocumento());
        model.setEstado(entity.getEstado());
        return model;
    }
    
    public JpaClienteEntity toEntity(Cliente model) {
    	
    	if (model == null) return null;
    	
    	JpaClienteEntity entity = new JpaClienteEntity();
        entity.setId(model.getId());
        entity.setNombreFantasia(model.getNombreFantasia());
        entity.setNombreCliente(model.getNombreCliente());
        entity.setSexo(model.getSexo() != null ? model.getSexo(): null);
        entity.setTelefonoFijo(model.getTelefonoFijo());
        entity.setTelefonoMovil(model.getTelefonoMovil());
        entity.setNumero(model.getNumero());
        entity.setEmail(model.getEmail());
        entity.setFechaIngreso(model.getFechaIngreso());
        entity.setFechaActualizacion(model.getFechaActualizacion());
        entity.setFechaBaja(model.getFechaBaja());
        entity.setFechaUltimaCompra(model.getFechaUltimaCompra());
        entity.setLimiteFacturasVencidas(model.getLimiteFacturasVencidas());
        entity.setLimiteCredito(model.getLimiteCredito());
        entity.setPagoMinimo(model.getPagoMinimo());
        entity.setSituacionFiscal(model.getSituacionFiscal());
        entity.setSaldoCtaCte(model.getSaldoCtaCte());
        entity.setObservaciones(model.getObservaciones());
        entity.setTipoDocumento(model.getTipoDocumento());
        entity.setEstado(model.getEstado());
        return entity;
    }

}
