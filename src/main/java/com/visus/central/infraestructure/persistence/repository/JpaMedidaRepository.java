package com.visus.central.infraestructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visus.central.infraestructure.persistence.entity.JpaMedidaEntity;

@Repository
public interface JpaMedidaRepository extends JpaRepository<JpaMedidaEntity, Integer> {
	// Métodos CRUD heredados de JpaRepository
	List<JpaMedidaEntity> findByDescripcionContainingIgnoreCase(String descripcion);
}
