package com.visus.central.domain.port.out;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.ComisionVenta;

public interface ComisionVentaRepository {

	ComisionVenta save(ComisionVenta comision);

	Optional<ComisionVenta> findById(Long id);

	List<ComisionVenta> findAll();

	List<ComisionVenta> findByVendedorId(Integer idVendedor);

	List<ComisionVenta> findByVentaId(Integer idVenta);

	List<ComisionVenta> findByFechaCalculoBetween(LocalDate desde, LocalDate hasta);

	List<ComisionVenta> findByEstado(String estado);

	boolean existsByPagoId(Long idPago);

	void deleteById(Long id);
}
