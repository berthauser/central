package com.visus.central.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visus.central.infraestructure.persistence.entity.JpaLocalidadEntity;

@Repository
public interface JpaLocalidadRepository extends JpaRepository<JpaLocalidadEntity, Integer> {
	boolean existsByDepartamento_Id(Integer idDepartamento);

}
