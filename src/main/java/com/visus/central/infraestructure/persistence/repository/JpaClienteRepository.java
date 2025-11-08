package com.visus.central.infraestructure.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.visus.central.infraestructure.persistence.entity.JpaClienteEntity;

public interface JpaClienteRepository extends JpaRepository<JpaClienteEntity, Integer> {
    
    // Busca clientes cuyo nombreFantasia contenga el texto (ignorando mayúsculas/minúsculas)
    List<JpaClienteEntity> findByNombreFantasiaContainingIgnoreCase(String nombreFantasia);
    List<JpaClienteEntity> findByNombreClienteContainingIgnoreCase(String nombreCliente);
    
    // Busca clientes que coincidan con cualquiera de los dos nombres
    List<JpaClienteEntity> findByNombreFantasiaContainingIgnoreCaseOrNombreClienteContainingIgnoreCase(
        String nombreFantasia, String nombreCliente);
    
    // Versión paginada
    Page<JpaClienteEntity> findByNombreFantasiaContainingIgnoreCaseOrNombreClienteContainingIgnoreCase(
        String nombreFantasia, String nombreCliente, Pageable pageable);
    
    // Verifica si ya existe un cliente con ese email (unicidad)
    boolean existsByEmail(String email);
    boolean existsByNombreFantasia(String nombreFantasia);
    boolean existsByNombreCliente(String nombreCliente);
    
    // Búsqueda por otros campos
    List<JpaClienteEntity> findBySexo(String sexo);
    List<JpaClienteEntity> findByEstado(String estado);
    List<JpaClienteEntity> findBySituacionFiscal(String situacionFiscal);
    List<JpaClienteEntity> findByEmailContainingIgnoreCase(String email);
    List<JpaClienteEntity> findByNumero(Long numero);

}
