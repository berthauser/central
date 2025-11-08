package com.visus.central.infraestructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visus.central.infraestructure.persistence.entity.JpaUserEntity;

@Repository
public interface JpaUserRepository extends JpaRepository<JpaUserEntity, Integer> {
    Optional<JpaUserEntity> findByUsername(String username);
    void deleteByUsername(String username);

}
