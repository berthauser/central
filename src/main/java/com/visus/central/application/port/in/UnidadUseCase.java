package com.visus.central.application.port.in;

import java.util.List;

import com.visus.central.domain.model.Unidad;

public interface UnidadUseCase {

	List<Unidad> buscarPorPresentacion(Integer idPresentacion);
}
