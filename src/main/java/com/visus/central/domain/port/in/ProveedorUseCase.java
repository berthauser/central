package com.visus.central.domain.port.in;

import java.util.List;

import com.visus.central.domain.model.Banco;
import com.visus.central.domain.model.Proveedor;

public interface ProveedorUseCase extends CrudUseCase<Proveedor> {
    List<Proveedor> findByNombreContainingIgnoreCase(String nombre);
    boolean existsByEmail(String email);
    List<Banco> findAllBancos(); // MÉTODO PARA OBTENER BANCOS
}
