package com.visus.central.domain.port.in;

import java.util.List;

import com.visus.central.domain.model.ReglaComision;

public interface ReglaComisionUseCase {

	List<ReglaComision> findAll();

	List<ReglaComision> findByVendedorId(Integer idVendedor);

	ReglaComision findById(Long id);

	ReglaComision save(ReglaComision regla);

	void deleteById(Long id);

	ReglaComision resolverReglaActiva(Integer idVendedor);
}
