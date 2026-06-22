package com.visus.central.infraestructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.visus.central.infraestructure.persistence.entity.ItemPagoEntity;

import jakarta.transaction.Transactional;

@Repository
public interface JpaItemPagoRepository extends JpaRepository<ItemPagoEntity, Long> {
	
	List<ItemPagoEntity> findByIdVenta(Long idVenta);

    @Modifying
    @Transactional
    @Query("DELETE FROM ItemPagoEntity i WHERE i.idVenta = :idVenta")
    void eliminarPorIdVenta(@Param("idVenta") Long idVenta);

}
