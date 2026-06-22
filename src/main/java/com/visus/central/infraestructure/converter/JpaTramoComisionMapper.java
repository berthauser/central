package com.visus.central.infraestructure.converter;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.TramoComision;
import com.visus.central.infraestructure.persistence.entity.JpaTramoComisionEntity;

@Component
public class JpaTramoComisionMapper {

	public JpaTramoComisionEntity toEntity(TramoComision domain) {
		if (domain == null)
			return null;

		JpaTramoComisionEntity entity = new JpaTramoComisionEntity();
		entity.setId(domain.getId());
		entity.setDesde(domain.getDesde());
		entity.setHasta(domain.getHasta());
		entity.setPorcentaje(domain.getPorcentaje());
		return entity;
	}

	public TramoComision toDomain(JpaTramoComisionEntity entity) {
		if (entity == null)
			return null;

		TramoComision domain = new TramoComision();
		domain.setId(entity.getId());
		domain.setDesde(entity.getDesde());
		domain.setHasta(entity.getHasta());
		domain.setPorcentaje(entity.getPorcentaje());
		return domain;
	}
}
