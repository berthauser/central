package com.visus.central.infraestructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visus.central.infraestructure.persistence.entity.JpaDevolucionEntity;

@Repository
public interface JpaDevolucionRepository extends JpaRepository<JpaDevolucionEntity, Long> {

    List<JpaDevolucionEntity> findByIdVenta(Integer idVenta);
}
