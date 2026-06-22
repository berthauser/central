package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Caja;

public interface CajaRepository {

	Caja save(Caja caja);

	Optional<Caja> findById(Integer id);

	Optional<Caja> findCajaAbierta(); // la única caja abierta (si aplica)

	List<Caja> findAll();

}
