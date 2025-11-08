package com.visus.central.infraestructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visus.central.infraestructure.persistence.entity.JpaDepartamentoEntity;

public interface JpaDepartamentoRepository extends JpaRepository<JpaDepartamentoEntity, Integer> {
    List<JpaDepartamentoEntity> findByNombreContainingIgnoreCase(String nombre);

}