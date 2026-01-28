package com.visus.central.domain.port.in;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Medida;

public interface MedidaUseCase {

	List<Medida> listar();
    Optional<Medida> buscarPorId(Integer id);
    void guardar(Medida medida);
    void eliminar(Integer id);
}