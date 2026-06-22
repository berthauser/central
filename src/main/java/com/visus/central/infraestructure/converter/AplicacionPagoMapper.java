package com.visus.central.infraestructure.converter;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.AplicacionPago;
import com.visus.central.infraestructure.persistence.entity.AplicacionPagoEntity;
import com.visus.central.infraestructure.persistence.entity.PagoEntity;

@Component
public class AplicacionPagoMapper {

	public AplicacionPagoEntity toEntity(AplicacionPago domain, PagoEntity pago) {
		if (domain == null)
			return null;
		AplicacionPagoEntity entity = new AplicacionPagoEntity();
		entity.setIdAplicacion(domain.getIdAplicacion());
		entity.setIdPago(pago.getIdPago());
		entity.setIdPlanPago(domain.getIdPlanPago());
		entity.setMontoAplicado(domain.getMontoAplicado());
		entity.setPctProntoPago(domain.getPctProntoPago());
		entity.setMontoDescuentoAplicado(domain.getMontoDescuentoAplicado());
		entity.setMontoNetoPagado(domain.getMontoNetoPagado());
		return entity;
	}

	public AplicacionPago toModel(AplicacionPagoEntity entity) {
		if (entity == null)
			return null;
		AplicacionPago model = new AplicacionPago();
		model.setIdAplicacion(entity.getIdAplicacion());
		model.setIdPago(entity.getIdPago());
		model.setIdPlanPago(entity.getIdPlanPago());
		model.setMontoAplicado(entity.getMontoAplicado());
		model.setPctProntoPago(entity.getPctProntoPago());
		model.setMontoDescuentoAplicado(entity.getMontoDescuentoAplicado());
		model.setMontoNetoPagado(entity.getMontoNetoPagado());
		return model;
	}

}
