package com.visus.central.infraestructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.visus.central.infraestructure.persistence.entity.ArticuloUnidadId;
import com.visus.central.infraestructure.persistence.entity.JpaArticuloUnidadEntity;

public interface JpaArticuloUnidadRepository extends JpaRepository<JpaArticuloUnidadEntity, ArticuloUnidadId> {

	@Query("SELECT au FROM JpaArticuloUnidadEntity au WHERE au.articulo.id = :idArticulo")
	List<JpaArticuloUnidadEntity> findByArticuloId(@Param("idArticulo") Integer idArticulo);

	@Modifying
	@Query("DELETE FROM JpaArticuloUnidadEntity au WHERE au.articulo.id = :idArticulo")
	void deleteByArticuloId(@Param("idArticulo") Integer idArticulo);
}
