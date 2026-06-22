package com.visus.central.infraestructure.persistence.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.visus.central.domain.model.EstadoPlanPago;
import com.visus.central.infraestructure.persistence.entity.PlanPagoEntity;

import jakarta.transaction.Transactional;

@Repository
public interface JpaPlanPagoRepository extends JpaRepository<PlanPagoEntity, Long> {
	
	List<PlanPagoEntity> findByIdVenta(Integer idVenta);

    @Query("SELECT p FROM PlanPagoEntity p WHERE p.idVenta = :idVenta AND p.estado = 'PENDIENTE'")
    List<PlanPagoEntity> findPendientesByVentaId(@Param("idVenta") Integer idVenta);

    @Query("SELECT p FROM PlanPagoEntity p WHERE p.fechaVencimiento < :fecha AND p.estado IN ('PENDIENTE', 'PARCIAL')")
    List<PlanPagoEntity> findVencidos(@Param("fecha") LocalDate fecha);

    @Query("SELECT p FROM PlanPagoEntity p WHERE p.fechaVencimiento BETWEEN :fechaInicio AND :fechaFin AND p.estado IN ('PENDIENTE', 'PARCIAL')")
    List<PlanPagoEntity> findPorVencer(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);

    List<PlanPagoEntity> findByEstado(EstadoPlanPago estado);

    @Modifying
    @Transactional
    @Query("UPDATE PlanPagoEntity p SET p.estado = :estado, p.fechaPago = CURRENT_DATE WHERE p.id = :id")
    int actualizarEstadoPlanPago(@Param("id") Long id, @Param("estado") EstadoPlanPago estado);
	
    
    List<PlanPagoEntity> findByEstadoIn(List<EstadoPlanPago> estados);
    
    @Query("SELECT p FROM PlanPagoEntity p WHERE p.idCliente = :idCliente AND p.estado IN ('PENDIENTE', 'PARCIAL')")
    List<PlanPagoEntity> findPendientesByClienteId(@Param("idCliente") Integer idCliente);

}
