package com.visus.central.infraestructure.converter;

import com.visus.central.infraestructure.persistence.entity.JpaFormaDePagoEntity;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ModalidadPagoConverter extends EnumLabelConverter<JpaFormaDePagoEntity.ModalidadPago> {

	public ModalidadPagoConverter() {
		super(JpaFormaDePagoEntity.ModalidadPago.class);
	}
	

}
