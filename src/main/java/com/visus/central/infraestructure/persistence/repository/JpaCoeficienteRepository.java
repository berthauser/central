package com.visus.central.infraestructure.persistence.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.visus.central.infraestructure.persistence.entity.CoeficienteEntity;

public interface JpaCoeficienteRepository extends JpaRepository<CoeficienteEntity, Integer> {
	// Métodos CRUD heredados de JpaRepository
	// Filtrar por tipo de pago que requiere coeficiente
	@Query("SELECT DISTINCT co FROM CoeficienteEntity co " + "LEFT JOIN FETCH co.tipoPago tp "
			+ "WHERE tp.requiere_coeficiente = true")
	List<CoeficienteEntity> findAllWithTipoPago();

	List<CoeficienteEntity> findByTipoPagoId(Integer idTipoPago);

	// En JpaCoeficienteRepository
	@Query("SELECT c FROM CoeficienteEntity c WHERE c.tipoPago.id = :idTipoPago AND c.coeficiente = :coeficiente")
	Optional<CoeficienteEntity> findByTipoPagoIdAndCoeficiente(@Param("idTipoPago") Integer idTipoPago,
			@Param("coeficiente") BigDecimal coeficiente);
}
