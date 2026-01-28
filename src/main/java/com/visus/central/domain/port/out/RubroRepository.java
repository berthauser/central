package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Rubro;

public interface RubroRepository {
	List<Rubro> listar();
	List<Rubro> buscarPorNombre(String nombre);
	Optional<Rubro> buscarPorId(Integer id);
	void guardar(Rubro rubro);
	void eliminar(Integer Id);

}
