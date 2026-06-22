package com.visus.central.infraestructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visus.central.domain.model.NombreCorto;
import com.visus.central.infraestructure.persistence.entity.JpaComprobanteEntity;

public interface JpaComprobanteRepository extends JpaRepository<JpaComprobanteEntity, Integer> {
	Optional<JpaComprobanteEntity> findByNombreLargoIgnoreCase(String nombreLargo);

	Optional<JpaComprobanteEntity> findByActivoTrue(); 
	
	Optional<JpaComprobanteEntity> findByNombreCorto(NombreCorto nombreCorto);
}
