package com.visus.central.infraestructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visus.central.infraestructure.persistence.entity.JpaListaPorcentualEntity;

public interface JpaListaPorcentualRepository extends JpaRepository<JpaListaPorcentualEntity, Integer> {
    List<JpaListaPorcentualEntity> findByListaId(Integer listaId);
}
