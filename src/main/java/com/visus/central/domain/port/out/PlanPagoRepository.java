package com.visus.central.domain.port.out;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.EstadoPlanPago;
import com.visus.central.domain.model.PlanPago;

public interface PlanPagoRepository {

	// CRUD Básico
	PlanPago save(PlanPago planPago);

	int actualizarEstadoPlanPago(Long id, EstadoPlanPago estado);

	Optional<PlanPago> findById(Long id);

	void deleteById(Long id);

	boolean existsById(Long id);

	List<PlanPago> findByVentaId(Integer idVenta);
	
	List<PlanPago> findByPendienteByVentaId(Integer IdVenta);
	
	List<PlanPago> findPendientesByClienteId(Integer idCliente);

	List<PlanPago> findVencidos(LocalDate fecha);

	List<PlanPago> findPorVencer(LocalDate fechaInicio, LocalDate fechaFin);

	List<PlanPago> findByEstado(EstadoPlanPago estado);
	
	List<PlanPago> findByEstadoIn(List<EstadoPlanPago> estados);

}
