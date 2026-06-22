package com.visus.central.infraestructure.converter;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.PermisoVista;
import com.visus.central.infraestructure.persistence.entity.JpaPermisoVistaEntity;

@Component
public class JpaPermisoVistaMapper {

	public PermisoVista toDomain(JpaPermisoVistaEntity entity) {
		PermisoVista permiso = new PermisoVista();
		permiso.setId(entity.getId());
		permiso.setUsuarioId(entity.getUsuarioId());
		permiso.setVistaClase(entity.getVistaClase());
		permiso.setPuedeVer(entity.isPuedeVer());
		return permiso;
	}

	public JpaPermisoVistaEntity toEntity(PermisoVista model) {
		JpaPermisoVistaEntity entity = new JpaPermisoVistaEntity();
		entity.setId(model.getId());
		entity.setUsuarioId(model.getUsuarioId());
		entity.setVistaClase(model.getVistaClase());
		entity.setPuedeVer(model.isPuedeVer());
		return entity;
	}
}
