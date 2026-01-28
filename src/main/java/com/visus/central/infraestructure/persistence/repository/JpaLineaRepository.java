package com.visus.central.infraestructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.visus.central.infraestructure.persistence.entity.JpaLineaEntity;

@Repository
public interface JpaLineaRepository extends JpaRepository<JpaLineaEntity, Integer> {
	boolean existsByRubroId(Integer idRubro);
	// Usando convención de nombres de Spring Data JPA
    // Busca líneas donde el rubro tenga el id especificado
    // Asumiendo que en JpaLineaEntity tienes: @ManyToOne private JpaRubroEntity rubro;
    List<JpaLineaEntity> findByRubroId(Integer idRubro);
    
    @Query("SELECT l FROM JpaLineaEntity l WHERE l.rubro.id = :rubroId ORDER BY l.descripcion ASC")
    List<JpaLineaEntity> findLineasByRubroId(@Param("rubroId") Integer rubroId);
}
