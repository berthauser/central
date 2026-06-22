package com.visus.central.infraestructure.persistence.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.visus.central.infraestructure.persistence.entity.PagoEntity;

@Repository
public interface JpaPagoRepository extends JpaRepository<PagoEntity, Long> {

	// Búsquedas básicas
	List<PagoEntity> findByIdCliente(Integer idCliente);

	List<PagoEntity> findByIdClienteAndFechaBetween(Integer idCliente, LocalDate fechaInicio, LocalDate fechaFin);

	List<PagoEntity> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

	// Pagos no aplicados
	@Query("SELECT p FROM PagoEntity p WHERE p.aplicado = false OR p.aplicado IS NULL")
	List<PagoEntity> findPagosNoAplicados();

	@Query("SELECT p FROM PagoEntity p WHERE p.idCliente = :idCliente AND (p.aplicado = false OR p.aplicado IS NULL)")
	List<PagoEntity> findPagosNoAplicadosByCliente(@Param("idCliente") Integer idCliente);

	// Suma de montos no aplicados
	@Query("SELECT COALESCE(SUM(p.montoTotal), 0) FROM PagoEntity p WHERE p.idCliente = :idCliente AND (p.aplicado = false OR p.aplicado IS NULL)")
	BigDecimal sumPagosNoAplicadosByCliente(@Param("idCliente") Integer idCliente);
	
	@Query("SELECT COALESCE(SUM(p.montoTotal), 0) FROM PagoEntity p WHERE p.idVenta = :idVenta")
	BigDecimal sumMontoTotalByIdVenta(@Param("idVenta") Integer idVenta);

}
