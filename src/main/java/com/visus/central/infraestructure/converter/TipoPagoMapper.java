package com.visus.central.infraestructure.converter;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.TipoPago;
import com.visus.central.infraestructure.persistence.entity.TipoPagoEntity;

@Component
public class TipoPagoMapper {

	public TipoPago toModel(TipoPagoEntity entity) {

		TipoPago model = new TipoPago();

		model.setId(entity.getId());
		model.setDescripcion(entity.getDescripcion());
		model.setRequiere_coeficiente(entity.getRequiere_coeficiente());
		model.setEs_pronto_pago(entity.getEs_pronto_pago());
		model.setDto_pronto_pago(entity.getDto_pronto_pago());
		model.setGenera_deuda(entity.getGenera_deuda());
		model.setAfecta_caja(entity.getAfecta_caja());

		return model;
	}

	public TipoPagoEntity toEntity(TipoPago model) {

		TipoPagoEntity entity = new TipoPagoEntity();

		entity.setId(model.getId());
		entity.setDescripcion(model.getDescripcion());
		entity.setRequiere_coeficiente(model.getRequiere_coeficiente());
		entity.setEs_pronto_pago(model.getEs_pronto_pago());
		entity.setDto_pronto_pago(model.getDto_pronto_pago());
		entity.setGenera_deuda(model.getGenera_deuda());
		entity.setAfecta_caja(model.getAfecta_caja());

		return entity;
	}

}
