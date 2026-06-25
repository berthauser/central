package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Devolucion;

public interface DevolucionRepository {
    Devolucion save(Devolucion devolucion);
    Optional<Devolucion> findById(Long id);
    List<Devolucion> findByVentaId(Integer idVenta);
    List<Devolucion> findAll();
}
