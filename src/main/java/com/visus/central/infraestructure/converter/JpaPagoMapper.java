package com.visus.central.infraestructure.converter;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Pago;
import com.visus.central.infraestructure.persistence.entity.PagoEntity;

@Component
public class JpaPagoMapper {

	// Dominio → Entidad JPA
	public PagoEntity toJpa(Pago domain) {
		if (domain == null)
			return null;

		PagoEntity jpa = new PagoEntity();
		jpa.setIdPago(domain.getIdPago());
		jpa.setIdCliente(domain.getIdCliente());
		jpa.setIdVenta(domain.getIdVenta());
		jpa.setFecha(domain.getFecha());
		jpa.setMontoTotal(domain.getMontoTotal());
		jpa.setIdTipoPago(domain.getIdTipoPago());
		jpa.setObservaciones(domain.getObservaciones());
		jpa.setAplicado(domain.getAplicado());

		return jpa;
	}

	public Pago toDomain(PagoEntity jpa) {
		if (jpa == null)
			return null;

		Pago domain = new Pago();
		domain.setIdPago(jpa.getIdPago());
		domain.setIdCliente(jpa.getIdCliente());
		domain.setIdVenta(jpa.getIdVenta());
		domain.setFecha(jpa.getFecha());
		domain.setMontoTotal(jpa.getMontoTotal());
		domain.setIdTipoPago(jpa.getIdTipoPago());
		domain.setObservaciones(jpa.getObservaciones());
		domain.setAplicado(jpa.getAplicado());

		return domain;
	}

}
