package com.visus.central.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visus.central.infraestructure.persistence.entity.JpaComprobanteEntity;

public interface JpaComprobanteRepository extends JpaRepository<JpaComprobanteEntity, Integer> {
    // Métodos CRUD heredados de JpaRepository
}
