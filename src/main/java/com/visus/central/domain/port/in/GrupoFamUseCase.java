package com.visus.central.domain.port.in;

import java.util.List;

import com.visus.central.domain.model.GrupoFam;

public interface GrupoFamUseCase extends CrudUseCase<GrupoFam> {
    List<GrupoFam> findByNombreContainingIgnoreCase(String nombre);
    List<GrupoFam> findByClienteId(Integer idCliente);
    boolean existsByNumero(Integer numero);
}
