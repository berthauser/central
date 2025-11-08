package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Banco;
import com.visus.central.domain.model.Proveedor;

public interface ProveedorRepository {
	List<Proveedor> findAll();
    List<Proveedor> findByNombreContainingIgnoreCase(String nombre);
    Optional<Proveedor> findById(Integer id);
    Proveedor save(Proveedor proveedor);
    void deleteById(Integer id);
    boolean existsByEmail(String email);
    List<Banco> findAllBancos(); // NUEVO MÉTODO
}
