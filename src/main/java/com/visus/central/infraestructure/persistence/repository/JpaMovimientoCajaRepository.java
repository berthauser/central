package com.visus.central.infraestructure.persistence.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visus.central.infraestructure.persistence.entity.MovimientoCajaEntity;

public interface JpaMovimientoCajaRepository extends JpaRepository<MovimientoCajaEntity, Integer> {

	List<MovimientoCajaEntity> findByCaja_Idcaja(Integer idCaja);

	List<MovimientoCajaEntity> findByCaja_IdcajaAndFecha(Integer idCaja, LocalDate fecha);

}
