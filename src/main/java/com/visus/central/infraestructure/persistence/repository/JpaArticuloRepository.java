package com.visus.central.infraestructure.persistence.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.visus.central.domain.model.EstadoArticulo;
import com.visus.central.infraestructure.persistence.entity.JpaArticuloEntity;

@Repository
public interface JpaArticuloRepository extends JpaRepository<JpaArticuloEntity, Integer> {
	
	// MÉTODO PAGINADO (para la UI)
    Page<JpaArticuloEntity> findByDescripcionContainingIgnoreCase(String descripcion, Pageable pageable);
//    Optional<JpaArticuloEntity> findByCodigoBarra(String codigoBarra);
    List<JpaArticuloEntity> findByLineaIdLinea(Integer idLinea);
    List<JpaArticuloEntity> findByProveedorId(Integer idProveedor);
    
    @Query("SELECT a FROM JpaArticuloEntity a " +
           "LEFT JOIN FETCH a.linea l " +
           "LEFT JOIN FETCH l.rubro " +
           "WHERE a.codigoBarra = :codigoBarra")
    Optional<JpaArticuloEntity> findByCodigoBarraWithRelations(@Param("codigoBarra") String codigoBarra);
    
    // Nuevos métodos para filtro por estado
    Page<JpaArticuloEntity> findByEstado(EstadoArticulo estado, Pageable pageable);
    
    Page<JpaArticuloEntity> findByDescripcionContainingIgnoreCaseAndEstado(
        @Param("descripcion") String descripcion, 
        @Param("estado") EstadoArticulo estado, 
        Pageable pageable);
    
    // Métodos para excluir un estado específico
    Page<JpaArticuloEntity> findByEstadoNot(EstadoArticulo estado, Pageable pageable);
    
    List<JpaArticuloEntity> findByEstadoNot(EstadoArticulo estado);
    
 // Método flexible para múltiples filtros (opcional)
    @Query("SELECT a FROM JpaArticuloEntity a WHERE " +
           "(:descripcion IS NULL OR LOWER(a.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%'))) AND " +
           "(:estado IS NULL OR a.estado = :estado)")
    Page<JpaArticuloEntity> buscarConFiltros(
        @Param("descripcion") String descripcion,
        @Param("estado") EstadoArticulo estado,
        Pageable pageable);
    
 // En tu repositorio JpaArticuloRepository
    @Query("SELECT a FROM JpaArticuloEntity a " +
           "LEFT JOIN FETCH a.linea l " +
           "LEFT JOIN FETCH l.rubro " +  // ¡Cargar el rubro también!
           "WHERE a.descripcion LIKE %:descripcion%")
    Page<JpaArticuloEntity> buscarPorDescripcionConRubro(
        @Param("descripcion") String descripcion, 
        Pageable pageable);
    
 // En tu repositorio, agrega estas consultas:
    @Query("SELECT a FROM JpaArticuloEntity a " +
           "LEFT JOIN FETCH a.linea l " +
           "LEFT JOIN FETCH l.rubro")
    Page<JpaArticuloEntity> findAllWithRubro(Pageable pageable);

    @Query("SELECT a FROM JpaArticuloEntity a " +
           "LEFT JOIN FETCH a.linea l " +
           "LEFT JOIN FETCH l.rubro r " +
           "WHERE r.descripcion LIKE %:rubro%")
    Page<JpaArticuloEntity> buscarPorRubro(
        @Param("rubro") String rubro, 
        Pageable pageable);
    
    List<JpaArticuloEntity> findByLineaIdLineaIn(List<Integer> lineasIds);

    @Query("SELECT a FROM JpaArticuloEntity a LEFT JOIN FETCH a.linea l LEFT JOIN FETCH l.rubro WHERE a.linea.rubro.id = :rubroId")
    Page<JpaArticuloEntity> findByRubroId(@Param("rubroId") Integer rubroId, Pageable pageable);

    Page<JpaArticuloEntity> findByLineaIdLinea(Integer idLinea, Pageable pageable);

    @Query("SELECT a FROM JpaArticuloEntity a LEFT JOIN FETCH a.linea l LEFT JOIN FETCH l.rubro WHERE l.idLinea IN :lineasIds")
    List<JpaArticuloEntity> findByLineaIdLineaInWithRubro(@Param("lineasIds") List<Integer> lineasIds);

    @Query("SELECT a FROM JpaArticuloEntity a LEFT JOIN FETCH a.linea l LEFT JOIN FETCH l.rubro r WHERE r.id = :rubroId AND l.idLinea IN :lineasIds")
    List<JpaArticuloEntity> findByRubroIdAndLineaIdIn(@Param("rubroId") Integer rubroId, @Param("lineasIds") List<Integer> lineasIds);

    @Query("SELECT COUNT(a) FROM JpaArticuloEntity a LEFT JOIN a.linea l LEFT JOIN l.rubro r WHERE r.id = :rubroId AND l.idLinea IN :lineasIds")
    int countByRubroIdAndLineaIdIn(@Param("rubroId") Integer rubroId, @Param("lineasIds") List<Integer> lineasIds);
    
}