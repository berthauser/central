package com.visus.central.infraestructure.converter;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.NotaCredito;
import com.visus.central.infraestructure.persistence.entity.JpaNotaCreditoEntity;

@Component
public class JpaNotaCreditoMapper {

	public NotaCredito toDomain(JpaNotaCreditoEntity entity) {
		if (entity == null)
			return null;
		NotaCredito domain = new NotaCredito();
		domain.setId(entity.getId());
		domain.setIdCliente(entity.getClienteId());
		domain.setMonto(entity.getMonto());
		domain.setFecha(entity.getFecha());
		domain.setObservaciones(entity.getObservaciones());
		domain.setConsumido(entity.isConsumido());
		return domain;
	}

	public JpaNotaCreditoEntity toEntity(NotaCredito domain) {
		if (domain == null)
			return null;
		JpaNotaCreditoEntity entity = new JpaNotaCreditoEntity();
		entity.setId(domain.getId());
		entity.setClienteId(domain.getIdCliente());
		entity.setMonto(domain.getMonto());
		entity.setFecha(domain.getFecha());
		entity.setObservaciones(domain.getObservaciones());
		entity.setConsumido(domain.isConsumido());
		return entity;
	}
}
