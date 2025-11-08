package com.visus.central.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visus.central.infraestructure.persistence.entity.JpaCoeficienteEntity;

public interface JpaCoeficienteRepository extends JpaRepository<JpaCoeficienteEntity, Integer> {
	// Métodos CRUD heredados de JpaRepository
}
