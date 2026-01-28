package com.visus.central.infraestructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visus.central.infraestructure.persistence.entity.JpaPresentacionEntity;

public interface JpaPresentacionRepository extends JpaRepository<JpaPresentacionEntity, Integer> {
	// Métodos CRUD heredados de JpaRepository
	List<JpaPresentacionEntity> findByDescripcionContainingIgnoreCase(String descripcion);

}
