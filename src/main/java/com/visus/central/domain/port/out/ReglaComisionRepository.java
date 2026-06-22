package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.ReglaComision;

public interface ReglaComisionRepository {

	ReglaComision save(ReglaComision regla);

	Optional<ReglaComision> findById(Long id);

	List<ReglaComision> findAll();

	List<ReglaComision> findByVendedorId(Integer idVendedor);

	List<ReglaComision> findActivas();

	Optional<ReglaComision> findActivaByVendedorId(Integer idVendedor);

	Optional<ReglaComision> findReglaGlobal();

	void deleteById(Long id);
}
