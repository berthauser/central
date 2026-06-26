package com.visus.central.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visus.central.infraestructure.persistence.entity.JpaListaEntity;

public interface JpaListaRepository extends JpaRepository<JpaListaEntity, Integer> {
}
