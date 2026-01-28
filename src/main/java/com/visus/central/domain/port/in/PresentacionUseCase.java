package com.visus.central.domain.port.in;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Presentacion;

public interface PresentacionUseCase {
	List<Presentacion> listar();
    Optional<Presentacion> buscarPorId(Integer id);
    void guardar(Presentacion presentacion);
    void eliminar(Integer id);

}
