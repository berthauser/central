package com.visus.central.infraestructure.converter;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Pago;
import com.visus.central.infraestructure.persistence.entity.PagoEntity;

@Component
public class PagoMapper {

	public PagoMapper() {

	}

	// Convierte de entidad JPA a dominio (model)
	public Pago toModel(PagoEntity pagoEntity) {
		if (pagoEntity == null)
			return null;

		Pago model = new Pago();
		model.setIdPago(pagoEntity.getIdPago());
		model.setIdCliente(pagoEntity.getIdCliente());
		model.setFecha(pagoEntity.getFecha());
		model.setIdTipoPago(pagoEntity.getIdTipoPago());
		model.setIdVenta(pagoEntity.getIdVenta());
		model.setMontoTotal(pagoEntity.getMontoTotal());
		model.setObservaciones(pagoEntity.getObservaciones());

		return model;
	}

	// Convierte de dominio a entidad JPA
	public PagoEntity toEntity(Pago model) {
		if (model == null)
			return null;

		PagoEntity entity = new PagoEntity();
		entity.setIdPago(model.getIdPago());
		entity.setIdCliente(model.getIdCliente());
		entity.setFecha(model.getFecha());
		entity.setIdTipoPago(model.getIdTipoPago());
		entity.setIdVenta(model.getIdVenta());
		entity.setMontoTotal(model.getMontoTotal());
		entity.setObservaciones(model.getObservaciones());

		return entity;
	}

}
