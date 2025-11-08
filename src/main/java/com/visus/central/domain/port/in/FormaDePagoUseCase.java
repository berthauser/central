package com.visus.central.domain.port.in;


import java.util.List;

import com.visus.central.domain.model.Coeficiente;
import com.visus.central.domain.model.FormaDePago;

public interface FormaDePagoUseCase extends CrudUseCase<FormaDePago> {
	// Los métodos CRUD están heredados de CrudUseCase
	// Nuevo método para obtener todos los coeficientes
    List<Coeficiente> findAllCoeficientes();
}
