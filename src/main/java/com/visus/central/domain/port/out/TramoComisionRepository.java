package com.visus.central.domain.port.out;

import java.util.List;

import com.visus.central.domain.model.TramoComision;

public interface TramoComisionRepository {

	List<TramoComision> findByReglaId(Long idRegla);

	TramoComision save(TramoComision tramo);

	void deleteByReglaId(Long idRegla);

	void deleteById(Long id);
}
