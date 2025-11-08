package com.visus.central.infraestructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visus.central.infraestructure.persistence.entity.JpaGrupoFamEntity;

public interface JpaGrupoFamRepository extends JpaRepository<JpaGrupoFamEntity, Integer> {
    
    // Búsqueda BÁSICA por nombre (contiene, ignorando mayúsculas/minúsculas)
	// findByNombreContainingIgnoreCase("juan") 
	// → SELECT * FROM grupofam WHERE LOWER(nombre) LIKE '%juan%'
    List<JpaGrupoFamEntity> findByNombreContainingIgnoreCase(String nombre);
    
    // Búsqueda por ID de cliente (POR RELACIÓN)
    // findByClienteId(123)
    // → SELECT * FROM grupofam WHERE idcliente = 123
    List<JpaGrupoFamEntity> findByClienteId(Integer idCliente);
    
    // Verificación de existencia (UNICIDAD) por documento
    // existsByDocumento("12345678")
    // → SELECT COUNT(*) FROM grupofam WHERE numero = '12345678' > 0
    boolean existsByNumero(Integer numero);
    
    // Búsqueda por estado
    List<JpaGrupoFamEntity> findByEstado(String estado);
    
    // Búsqueda por parentesco
    List<JpaGrupoFamEntity> findByParentesco(String parentesco);
    
    // Búsqueda combinada
    // findByNombreContainingIgnoreCaseAndClienteId("juan", 123)
    // → SELECT * FROM grupofam WHERE LOWER(nombre) LIKE '%juan%' AND idcliente = 123
    List<JpaGrupoFamEntity> findByNombreContainingIgnoreCaseAndClienteId(String nombre, Integer idCliente);
    List<JpaGrupoFamEntity> findByEstadoAndClienteId(String estado, Integer idCliente);
    List<JpaGrupoFamEntity> findByParentescoAndClienteId(String parentesco, Integer idCliente);
    
    // Búsqueda por número
    List<JpaGrupoFamEntity> findByNumero(Long numero);
    

}
