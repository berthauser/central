package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Coeficiente;

public interface CoeficienteRepository {
	List<Coeficiente> findAll();
    Optional<Coeficiente> findById(Integer id);
    Coeficiente save(Coeficiente coeficiente);
    void deleteById(Integer id);
}
