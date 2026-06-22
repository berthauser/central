package com.visus.central.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.visus.central.domain.model.EstadoVenta;
import com.visus.central.infraestructure.persistence.entity.JpaVentaEntity;

import jakarta.transaction.Transactional;

public interface JpaVentaRepository extends JpaRepository<JpaVentaEntity, Integer> {
	
	@Modifying
	@Transactional
	@Query("UPDATE JpaVentaEntity v SET v.estado = :estado WHERE v.id = :idVenta")
	int actualizarEstadoVenta(@Param("idVenta") Integer idVenta, 
            @Param("estado") EstadoVenta estado);  

}
