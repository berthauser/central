package com.visus.central.infraestructure.persistence.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visus.central.infraestructure.persistence.entity.JpaComisionVentaEntity;

@Repository
public interface JpaComisionVentaRepository extends JpaRepository<JpaComisionVentaEntity, Long> {

	List<JpaComisionVentaEntity> findByVendedorId(Integer idVendedor);

	List<JpaComisionVentaEntity> findByVentaId(Integer idVenta);

	List<JpaComisionVentaEntity> findByFechaCalculoBetween(LocalDate desde, LocalDate hasta);

	List<JpaComisionVentaEntity> findByEstado(String estado);

	boolean existsByIdPago(Long idPago);
}
