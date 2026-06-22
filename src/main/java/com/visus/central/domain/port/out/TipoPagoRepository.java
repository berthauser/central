package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Coeficiente;
import com.visus.central.domain.model.TipoPago;

public interface TipoPagoRepository {
	List<TipoPago> findAll();

	Optional<TipoPago> findById(Integer id);

	TipoPago save(TipoPago tipoPago);

	void deleteById(Integer id);

	List<TipoPago> findByRequiereCoeficienteTrue();

	List<Coeficiente> findAllCoeficientes();

	Optional<TipoPago> findByDescripcion(String descripcion);

}
