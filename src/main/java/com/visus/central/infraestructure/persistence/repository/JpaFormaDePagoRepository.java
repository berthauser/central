package com.visus.central.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visus.central.infraestructure.persistence.entity.JpaFormaDePagoEntity;

public interface JpaFormaDePagoRepository extends JpaRepository<JpaFormaDePagoEntity, Integer> {
	// Métodos CRUD heredados de JpaRepository
}
