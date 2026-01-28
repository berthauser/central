package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Medida;

public interface MedidaRepository {
	List<Medida> listar();
	Optional<Medida> buscarPorId(Integer id);
	void guardar(Medida medida);
	void eliminar(Integer id);

}
