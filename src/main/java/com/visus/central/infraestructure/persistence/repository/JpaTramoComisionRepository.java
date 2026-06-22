package com.visus.central.infraestructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visus.central.infraestructure.persistence.entity.JpaTramoComisionEntity;

@Repository
public interface JpaTramoComisionRepository extends JpaRepository<JpaTramoComisionEntity, Long> {

	List<JpaTramoComisionEntity> findByReglaId(Long idRegla);

	void deleteByReglaId(Long idRegla);
}
