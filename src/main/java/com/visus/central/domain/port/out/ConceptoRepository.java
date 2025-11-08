package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Concepto;

public interface ConceptoRepository {
	List<Concepto> findAll();
	Optional<Concepto> findById(Integer id);
	Concepto save(Concepto concepto);
	void deleteById(Integer id);
}
