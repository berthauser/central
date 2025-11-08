package com.visus.central.domain.port.in;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Localidad;

public interface LocalidadUseCase {
	List<Localidad> listar();
    Optional<Localidad> buscarPorId(Integer id);
    void guardar(Localidad localidad);
    void eliminar(Integer id);
    boolean tieneLocalidadesAsociadas(Integer idDepartamento);

}
