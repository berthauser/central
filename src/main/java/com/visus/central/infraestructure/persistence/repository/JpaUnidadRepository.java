package com.visus.central.infraestructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visus.central.infraestructure.persistence.entity.JpaUnidadEntity;

public interface JpaUnidadRepository extends JpaRepository<JpaUnidadEntity, Integer> {

	List<JpaUnidadEntity> findByIdPresentacionOrderByMedida(Integer idPresentacion);
}
