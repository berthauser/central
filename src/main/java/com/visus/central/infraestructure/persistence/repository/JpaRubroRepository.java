package com.visus.central.infraestructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visus.central.infraestructure.persistence.entity.JpaRubroEntity;

public interface JpaRubroRepository extends JpaRepository<JpaRubroEntity , Integer>{
	// Métodos CRUD heredados de JpaRepository
	List<JpaRubroEntity> findByDescripcionContainingIgnoreCase(String descripcion);

}
