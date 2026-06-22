package com.visus.central.domain.port.out;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Pago;

public interface PagoRepository {

	// CRUD básico
	Pago save(Pago pago);

	Optional<Pago> findById(Long id);

	void deleteById(Long id);

	boolean existsById(Long id);

	// Búsquedas específicas
	List<Pago> findAll();

	List<Pago> findByIdCliente(Integer idCliente);

	List<Pago> findByIdClienteAndFechaBetween(Integer idCliente, LocalDate fechaInicio, LocalDate fechaFin);

	List<Pago> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);
	
	BigDecimal sumMontoTotalByVentaId(Integer idVenta);
}
