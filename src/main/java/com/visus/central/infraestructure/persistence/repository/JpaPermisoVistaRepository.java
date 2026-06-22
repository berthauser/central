package com.visus.central.infraestructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visus.central.infraestructure.persistence.entity.JpaPermisoVistaEntity;

@Repository
public interface JpaPermisoVistaRepository extends JpaRepository<JpaPermisoVistaEntity, Integer> {

	Optional<JpaPermisoVistaEntity> findByUsuarioIdAndVistaClase(Integer usuarioId, String vistaClase);

	List<JpaPermisoVistaEntity> findByUsuarioId(Integer usuarioId);

	List<JpaPermisoVistaEntity> findByVistaClase(String vistaClase);

	void deleteByUsuarioId(Integer usuarioId);

	boolean existsByUsuarioIdAndVistaClase(Integer usuarioId, String vistaClase);
}
