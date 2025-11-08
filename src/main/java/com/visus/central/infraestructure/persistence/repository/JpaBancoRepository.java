package com.visus.central.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visus.central.infraestructure.persistence.entity.JpaBancoEntity;

public interface JpaBancoRepository extends JpaRepository<JpaBancoEntity, Integer> {
    boolean existsByIdBcoCen(Integer idBcoCen);

}
