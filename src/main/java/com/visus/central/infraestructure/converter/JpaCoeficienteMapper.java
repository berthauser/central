package com.visus.central.infraestructure.converter;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Coeficiente;
import com.visus.central.infraestructure.persistence.entity.CoeficienteEntity;
import com.visus.central.infraestructure.persistence.entity.TipoPagoEntity;

@Component
public class JpaCoeficienteMapper {

	private final TipoPagoMapper tipoPagoMapper;

	public JpaCoeficienteMapper(TipoPagoMapper tipoPagoMapper) {
		this.tipoPagoMapper = tipoPagoMapper;
	}

	public Coeficiente toModel(CoeficienteEntity entity) {
		if (entity == null)
			return null;

		Coeficiente model = new Coeficiente();
		model.setId(entity.getId());
		model.setIdTipo_pago(entity.getTipoPago() != null ? entity.getTipoPago().getId() : null);
		// Mapear el objeto tipode pago completo
		if (entity.getTipoPago() != null) {
			model.setTipo_pago(tipoPagoMapper.toModel(entity.getTipoPago()));
		}
		model.setDescripcion(entity.getDescripcion());
		model.setCoeficiente(entity.getCoeficiente());
		model.setCuotas(entity.getCuotas());
		return model;
	}

	public CoeficienteEntity toEntity(Coeficiente model) {
		if (model == null)
			return null;

		CoeficienteEntity entity = new CoeficienteEntity();
		entity.setId(model.getId());
		// Asociación con el tipo de pago
		if (model.getTipo_pago() != null) {
			entity.setTipoPago(tipoPagoMapper.toEntity(model.getTipo_pago()));
		} else if (model.getIdTipo_pago() != null) {
			TipoPagoEntity tipoPago = new TipoPagoEntity();
			tipoPago.setId(model.getIdTipo_pago());
			entity.setTipoPago(tipoPago);
		}

		entity.setDescripcion(model.getDescripcion());
		entity.setCoeficiente(model.getCoeficiente());
		entity.setCuotas(model.getCuotas());
		return entity;
	}

}
