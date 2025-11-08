package com.visus.central.infraestructure.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.visus.central.infraestructure.persistence.entity.JpaProveedorEntity;

public interface JpaProveedorRepository extends JpaRepository<JpaProveedorEntity, Integer> {
    
    List<JpaProveedorEntity> findByNombreFantasiaContainingIgnoreCaseOrNombreRealContainingIgnoreCase(
        String nombreFantasia, String nombreReal);
    
    Page<JpaProveedorEntity> findByNombreFantasiaContainingIgnoreCaseOrNombreRealContainingIgnoreCase(
        String nombreFantasia, String nombreReal, Pageable pageable);
    
    boolean existsByNombreFantasia(String nombreFantasia);
    boolean existsByNombreReal(String nombreReal);
    boolean existsByEmail(String email);
    
    // Opcional: Método adicional para búsqueda más flexible
    List<JpaProveedorEntity> findByNombreFantasiaContainingIgnoreCase(String nombreFantasia);
    List<JpaProveedorEntity> findByNombreRealContainingIgnoreCase(String nombreReal);
}
