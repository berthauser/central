package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Comprobante;

public interface ComprobanteRepository {
	List<Comprobante> findAll();

	Optional<Comprobante> findActivo();

	Optional<Comprobante> findById(Integer id);
	
	Optional<Comprobante> findByNombreCorto(String nombreCorto);

	Comprobante save(Comprobante comprobante);

	void deleteById(Integer id);
}
