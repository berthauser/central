package com.visus.central.domain.port.in;

import java.util.List;

import com.visus.central.domain.model.Vendedor;

public interface VendedorUseCase extends CrudUseCase<Vendedor> {
	List<Vendedor> findByNombreContainingIgnoreCase(String nombre);
    boolean existsByEmail(String email);
}
