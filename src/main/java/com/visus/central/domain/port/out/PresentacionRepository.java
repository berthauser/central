package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Presentacion;

public interface PresentacionRepository {
	List<Presentacion> listar();

	List<Presentacion> buscarPorDescripcion(String descripcion);

	Optional<Presentacion> buscarPorId(Integer id);

	void guardar(Presentacion presentacion);

	void eliminar(Integer Id);

}
