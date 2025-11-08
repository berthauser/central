package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Vendedor;

public interface VendedorRepository {
	List<Vendedor> findAll();
	List<Vendedor> buscarPorNombre(String nombre);
	Optional<Vendedor> findById(Integer id);
    Vendedor save(Vendedor model); // DEBE DEVOLVER Vendedor
    void deleteById(Integer id);
    boolean existsByEmail(String email);
}
