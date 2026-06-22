package com.visus.central.infraestructure.converter;

import com.visus.central.infraestructure.persistence.entity.JpaArticuloEntity;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class FormaDePagoConverter extends EnumLabelConverter<JpaArticuloEntity.Estado> {

	protected FormaDePagoConverter() {
		super(JpaArticuloEntity.Estado.class);
	}

	/*
	 * EFECTIVO("Efectivo"), TRANSFERENCIA("Transferencia"), CHEQUE("Cheque"),
	 * TARJETA_CREDITO("Tarjeta de Crédito"), TARJETA_DEBITO("Tarjeta de Débito");
	 * 
	 */

}
