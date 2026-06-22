package com.visus.central.infraestructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.visus.central.infraestructure.persistence.entity.TipoPagoEntity;

public interface JpaTipoPagoRepository extends JpaRepository<TipoPagoEntity, Integer> {
	// Métodos CRUD heredados de JpaRepository
	
	@Query("SELECT tp FROM TipoPagoEntity tp WHERE tp.requiere_coeficiente = true")
	List<TipoPagoEntity> findByRequiereCoeficienteTrue();

	Optional<TipoPagoEntity> findByDescripcion(String descripcion);

}
