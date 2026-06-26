package com.visus.central.infraestructure.persistence.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.visus.central.domain.model.EstadoCaja;
import com.visus.central.infraestructure.persistence.entity.CajaEntity;

public interface JpaCajaRepository extends JpaRepository<CajaEntity, Integer> {
	
	 Optional<CajaEntity> findByEstado(EstadoCaja estado);

	 @Query("SELECT c FROM CajaEntity c WHERE c.estado = 'CERRADA' AND c.fechaApertura >= :fechaInicio AND c.fechaCierre <= :fechaFin ORDER BY c.fechaCierre DESC")
	 List<CajaEntity> findCerradasPorRango(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);

}
