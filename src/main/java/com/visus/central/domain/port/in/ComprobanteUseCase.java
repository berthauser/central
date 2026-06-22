package com.visus.central.domain.port.in;

import com.visus.central.domain.model.Comprobante;

public interface ComprobanteUseCase extends CrudUseCase<Comprobante> {
    // Los métodos CRUD están heredados de CrudUseCase
	
	void activarComprobante(Integer id);
}
