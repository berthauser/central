package com.visus.central.infraestructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visus.central.domain.model.EstadoCaja;
import com.visus.central.infraestructure.persistence.entity.CajaEntity;

public interface JpaCajaRepository extends JpaRepository<CajaEntity, Integer> {
	
	 Optional<CajaEntity> findByEstado(EstadoCaja estado);

}
