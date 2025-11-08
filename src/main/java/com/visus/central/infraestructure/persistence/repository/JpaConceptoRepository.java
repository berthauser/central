package com.visus.central.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visus.central.infraestructure.persistence.entity.JpaConceptoEntity;

public interface JpaConceptoRepository extends JpaRepository<JpaConceptoEntity, Integer> {
	// Métodos CRUD heredados de JpaRepository
}
