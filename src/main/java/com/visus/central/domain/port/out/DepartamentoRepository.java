package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Departamento;

public interface DepartamentoRepository {
	List<Departamento> listar();
	List<Departamento> buscarPorNombre(String nombre);
	Optional<Departamento> buscarPorId(Integer id);
	void guardar(Departamento departamento);
	void eliminar(Integer Id);
}
