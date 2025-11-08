package com.visus.central.domain.port.in;

import java.util.List;

import com.visus.central.domain.model.Cliente;

public interface ClienteUseCase extends CrudUseCase<Cliente> {
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
    boolean existsByEmail(String email);
}
