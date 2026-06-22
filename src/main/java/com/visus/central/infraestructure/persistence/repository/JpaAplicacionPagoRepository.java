package com.visus.central.infraestructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.visus.central.infraestructure.persistence.entity.AplicacionPagoEntity;

@Repository
public interface JpaAplicacionPagoRepository extends JpaRepository<AplicacionPagoEntity, Long> {

	List<AplicacionPagoEntity> findByIdPlanPago(Long idPlanPago);

	@Query("SELECT ap FROM AplicacionPagoEntity ap WHERE ap.idPlanPago IN (SELECT pp.id FROM PlanPagoEntity pp WHERE pp.idVenta = :idVenta)")
	List<AplicacionPagoEntity> findByVentaId(@Param("idVenta") Integer idVenta);
}
