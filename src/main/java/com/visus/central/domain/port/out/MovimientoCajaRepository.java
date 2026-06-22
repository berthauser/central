package com.visus.central.domain.port.out;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Caja;
import com.visus.central.domain.model.MovimientoCaja;

public interface MovimientoCajaRepository {

	MovimientoCaja save(MovimientoCaja movimiento);

	List<MovimientoCaja> findByCaja(Caja caja);

	List<MovimientoCaja> findByCajaAndFecha(Caja caja, LocalDate fecha);

	Optional<MovimientoCaja> findById(Integer id);

	void deleteById(Integer id);

}
