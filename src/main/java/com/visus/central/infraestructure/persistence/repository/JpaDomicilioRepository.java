package com.visus.central.infraestructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visus.central.infraestructure.persistence.entity.JpaClienteEntity;
import com.visus.central.infraestructure.persistence.entity.JpaDomicilioEntity;
import com.visus.central.infraestructure.persistence.entity.JpaVendedorEntity;

@Repository
public interface JpaDomicilioRepository extends JpaRepository<JpaDomicilioEntity, Integer> {
    List<JpaDomicilioEntity> findByVendedor(JpaVendedorEntity vendedor);
    List<JpaDomicilioEntity> findByCliente(JpaClienteEntity cliente);
    List<JpaDomicilioEntity> findByIdLocalidad(Integer idLocalidad);
}
