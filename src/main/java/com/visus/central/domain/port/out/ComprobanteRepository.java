package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Comprobante;

public interface ComprobanteRepository {
	List<Comprobante> findAll();
    Optional<Comprobante> findById(Integer id);
    Comprobante save(Comprobante comprobante);
    void deleteById(Integer id);
}
