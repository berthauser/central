package com.visus.central.domain.port.out;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Coeficiente;
import com.visus.central.domain.model.TipoPago;

public interface CoeficienteRepository {
	List<Coeficiente> findAll();

	Optional<Coeficiente> findById(Integer id);

	Coeficiente save(Coeficiente coeficiente);

	void deleteById(Integer id);

	// Nuevo método para obtener todos los coeficientes
	List<TipoPago> findAllTiposPago();

	List<Coeficiente> findByTipoPagoId(Integer idTipoPago);
	
	Optional<Coeficiente> findByTipoPagoIdAndCoeficiente(Integer idTipoPago, BigDecimal coeficiente);

}
