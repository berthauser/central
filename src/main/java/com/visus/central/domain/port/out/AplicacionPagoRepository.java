package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.AplicacionPago;

public interface AplicacionPagoRepository {

	AplicacionPago save(AplicacionPago aplicacion);

    Optional<AplicacionPago> findById(Long id);

    void deleteById(Long id);
    boolean existsById(Long id);

    List<AplicacionPago> findByPlanPagoId(Long idPlanPago);
    
    List<AplicacionPago> findByVentaId(Integer idVenta);

}
