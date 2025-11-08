package com.visus.central.infraestructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visus.central.infraestructure.persistence.entity.JpaVendedorEntity;

public interface JpaVendedorRepository extends JpaRepository<JpaVendedorEntity, Integer> {
	List<JpaVendedorEntity> findByNombreContainingIgnoreCase(String nombre);
    JpaVendedorEntity findByNumero(Long numero);
    boolean existsByEmail(String email);
}
