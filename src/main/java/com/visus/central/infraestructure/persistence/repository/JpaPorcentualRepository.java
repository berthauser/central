package com.visus.central.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visus.central.infraestructure.persistence.entity.JpaPorcentualEntity;

public interface JpaPorcentualRepository extends JpaRepository<JpaPorcentualEntity, Integer> {
}
