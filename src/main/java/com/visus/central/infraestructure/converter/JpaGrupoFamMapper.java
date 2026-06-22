package com.visus.central.infraestructure.converter;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.GrupoFam;
import com.visus.central.infraestructure.persistence.entity.JpaClienteEntity;
import com.visus.central.infraestructure.persistence.entity.JpaGrupoFamEntity;

@Component
public class JpaGrupoFamMapper {
	
	public GrupoFam toDomain(JpaGrupoFamEntity entity) {
		if (entity == null) return null;
		GrupoFam model = new GrupoFam();
		model.setId(entity.getIdgrupofam());
		model.setNombre(entity.getNombre());
		model.setIdCliente(entity.getCliente() != null ? entity.getCliente().getId() : null);
		model.setDocumento(entity.getTipoDocumento());
		model.setNumero(entity.getNumero());
		model.setParentesco(entity.getParentesco());
		model.setEstado(entity.getEstado());
		return model;
	}

	public JpaGrupoFamEntity toEntity(GrupoFam model) {
		if (model == null) return null;
		JpaGrupoFamEntity entity = new JpaGrupoFamEntity();
		entity.setIdgrupofam(model.getId());
		entity.setNombre(model.getNombre());
		
		if (model.getIdCliente() != null) {
            JpaClienteEntity cliente = new JpaClienteEntity();
            cliente.setId(model.getIdCliente());
            entity.setCliente(cliente);
        }
		
		entity.setTipoDocumento(model.getDocumento());
		entity.setNumero(model.getNumero());
		entity.setParentesco(model.getParentesco());
		entity.setEstado(model.getEstado());
		return entity;
	}
	
}
