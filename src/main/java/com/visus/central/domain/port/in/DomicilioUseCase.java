package com.visus.central.domain.port.in;

import java.util.List;

import com.visus.central.domain.model.Domicilio;

public interface DomicilioUseCase {
	List<Domicilio> findAll();
    Domicilio findById(Integer id);
    Domicilio save(Domicilio model);
    void deleteById(Integer id);
    List<Domicilio> findByVendedorId(Integer idVendedor);
    List<Domicilio> findByClienteId(Integer idCliente);
}
