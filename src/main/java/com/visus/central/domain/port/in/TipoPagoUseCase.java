package com.visus.central.domain.port.in;

import java.util.List;

import com.visus.central.domain.model.Coeficiente;
import com.visus.central.domain.model.TipoPago;

public interface TipoPagoUseCase extends CrudUseCase<TipoPago> {
	// Los métodos CRUD están heredados de CrudUseCase
	// Me trae todos los coeficientes asociados al Tipo de Pago
	List<Coeficiente> findAllCoeficientes();
}
