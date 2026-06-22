package com.visus.central.infraestructure.converter;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.ItemPago;
import com.visus.central.infraestructure.persistence.entity.ItemPagoEntity;

@Component
public class ItemPagoMapper {

	public ItemPagoEntity toEntity(ItemPago model) {
		if (model == null)
			return null;

		ItemPagoEntity entity = new ItemPagoEntity();

		entity.setId(model.getId());
		entity.setIdVenta(model.getIdVenta());
		entity.setIdTipoPago(model.getIdTipoPago());
		entity.setIdCoeficiente(model.getIdCoeficiente());
		entity.setMonto(model.getMonto());
		entity.setCoeficienteAplicado(model.getCoeficienteAplicado());
		entity.setCantidadCuotas(model.getCantidadCuotas());

		return entity;
	}

	public ItemPago toModel(ItemPagoEntity entity) {
		if (entity == null)
			return null;

		ItemPago model = new ItemPago();

		model.setId(entity.getId());
		model.setIdVenta(entity.getIdVenta());
		model.setIdTipoPago(entity.getIdTipoPago());
		model.setIdCoeficiente(entity.getIdCoeficiente());
		model.setMonto(entity.getMonto());
		model.setCoeficienteAplicado(entity.getCoeficienteAplicado());
		model.setCantidadCuotas(entity.getCantidadCuotas());

		return model;
	}

}
