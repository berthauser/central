package com.visus.central.infraestructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.visus.central.infraestructure.persistence.entity.JpaReglaComisionEntity;

@Repository
public interface JpaReglaComisionRepository extends JpaRepository<JpaReglaComisionEntity, Long> {

	List<JpaReglaComisionEntity> findByVendedorId(Integer idVendedor);

	@Query("SELECT r FROM JpaReglaComisionEntity r WHERE r.activo = true")
	List<JpaReglaComisionEntity> findActivas();

	@Query("SELECT r FROM JpaReglaComisionEntity r WHERE r.vendedor.id = :idVendedor AND r.activo = true")
	Optional<JpaReglaComisionEntity> findActivaByVendedorId(Integer idVendedor);

	@Query("SELECT r FROM JpaReglaComisionEntity r WHERE r.vendedor IS NULL AND r.activo = true")
	Optional<JpaReglaComisionEntity> findReglaGlobal();
}
