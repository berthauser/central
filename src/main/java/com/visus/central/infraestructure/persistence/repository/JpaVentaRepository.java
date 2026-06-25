package com.visus.central.infraestructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import com.visus.central.domain.model.EstadoVenta;
import com.visus.central.infraestructure.persistence.entity.JpaVentaEntity;

import jakarta.persistence.QueryHint;
import jakarta.transaction.Transactional;

public interface JpaVentaRepository extends JpaRepository<JpaVentaEntity, Integer> {

	List<JpaVentaEntity> findByCliente_Id(Integer idCliente);

	@Query("SELECT DISTINCT v FROM JpaVentaEntity v LEFT JOIN FETCH v.items i LEFT JOIN FETCH i.articulo a WHERE a.codigoBarra = :codigoBarra")
	@QueryHints(@QueryHint(name = "hibernate.query.passDistinctThrough", value = "false"))
	List<JpaVentaEntity> findByArticuloCodigoBarra(@Param("codigoBarra") String codigoBarra);

	@Modifying
	@Transactional
	@Query("UPDATE JpaVentaEntity v SET v.estado = :estado WHERE v.id = :idVenta")
	int actualizarEstadoVenta(@Param("idVenta") Integer idVenta,
			@Param("estado") EstadoVenta estado);

}
