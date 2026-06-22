package com.visus.central.domain.port.in;

import java.math.BigDecimal;

public record MovimientoManualRec(

		Integer idCaja, 
		BigDecimal monto, 
		Boolean esIngreso, 
		String descripcion, 
		Integer idTipoPago,
		Integer idComprobante) {

	public MovimientoManualRec {
		if (idCaja == null || idCaja <= 0) {
			throw new IllegalArgumentException("idCaja inválido");
		}
		if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("El monto debe ser positivo");
		}
		if (descripcion == null || descripcion.isBlank()) {
			throw new IllegalArgumentException("La descripción es obligatoria");
		}
	}

}
