package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Localidad;

public interface LocalidadRepository {
    List<Localidad> listar();
//    List<Localidad> buscar(String filtro);
    Optional<Localidad> buscarPorId(Integer id);
    void guardar(Localidad localidad);
    void eliminar(Integer id);
    boolean existePorDepartamentoId(Integer idDepartamento);

}
