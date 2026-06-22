package com.visus.central.infraestructure.converter;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.PlanPago;
import com.visus.central.infraestructure.persistence.entity.PlanPagoEntity;

@Component
public class PlanPagoMapper {

	// Sin relación (para uso general)
	public PlanPagoEntity toEntity(PlanPago model) {
		if (model == null) {
			return null;
		}

		PlanPagoEntity entity = new PlanPagoEntity();

		entity.setId(model.getIdPlanPago());
		entity.setIdVenta(model.getIdVenta());
		entity.setIdCliente(model.getIdCliente());
		entity.setIdTipo_pago(model.getIdTipoPago());
		entity.setIdCoeficiente(model.getIdCoeficiente());
		entity.setMontoOriginal(model.getMontoOriginal());
		entity.setNroCuota(model.getNroCuota());
		entity.setFechaVencimiento(model.getFechaVencimiento());
		entity.setMontoPagado(model.getMontoPagado());
		entity.setMontoDescuento(model.getMontoDescuentoTotal() != null ? model.getMontoDescuentoTotal() : BigDecimal.ZERO);
		entity.setFechaPago(model.getFechaPago());
		entity.setEstado(model.getEstado());

		return entity;
	}

	public PlanPago toModel(PlanPagoEntity entity) {
		if (entity == null) {
			return null;
		}

		PlanPago model = new PlanPago();

		model.setIdPlanPago(entity.getId());
		model.setIdVenta(entity.getIdVenta() != null ? entity.getIdVenta() : null);
		model.setIdCliente(entity.getIdCliente() != null ? entity.getIdCliente() : null);
		model.setIdTipoPago(entity.getIdTipo_pago() != null ? entity.getIdTipo_pago() : null);
		model.setIdCoeficiente(entity.getIdCoeficiente() != null ? entity.getIdCoeficiente() : null);
		model.setMontoOriginal(entity.getMontoOriginal() != null ? entity.getMontoOriginal() : null);
		model.setNroCuota(entity.getNroCuota() != null ? entity.getNroCuota() : null);
		model.setFechaVencimiento(entity.getFechaVencimiento());
		model.setMontoPagado(entity.getMontoPagado());
		model.setMontoDescuentoTotal(entity.getMontoDescuento() != null ? entity.getMontoDescuento() : BigDecimal.ZERO);
		model.setFechaPago(entity.getFechaPago());
		model.setEstado(entity.getEstado());

		return model;
	}

}
