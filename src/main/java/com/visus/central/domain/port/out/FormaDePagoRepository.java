package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Coeficiente;
import com.visus.central.domain.model.FormaDePago;

public interface FormaDePagoRepository {
	List<FormaDePago> findAll();
    Optional<FormaDePago> findById(Integer id);
    FormaDePago save(FormaDePago formaDePago);
    void deleteById(Integer id);
    // Nuevo método para obtener todos los coeficientes
    List<Coeficiente> findAllCoeficientes();
}
