package com.visus.central.domain.port.in;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Departamento;

public interface DepartamentoUseCase {
	List<Departamento> listar();
    Optional<Departamento> buscarPorId(Integer id);
    void guardar(Departamento departamento);
    void eliminar(Integer id);
    boolean tieneLocalidadesAsociadas(Integer idDepartamento);

}
