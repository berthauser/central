package com.visus.central.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visus.central.infraestructure.persistence.entity.JpaAlicuotaEntity;

public interface JpaAlicuotaRepository extends JpaRepository<JpaAlicuotaEntity, Integer> {
	// Métodos CRUD heredados de JpaRepository

}
