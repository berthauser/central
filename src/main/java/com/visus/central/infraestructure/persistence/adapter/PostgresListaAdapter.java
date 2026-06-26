package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Lista;
import com.visus.central.domain.port.out.ListaRepository;
import com.visus.central.infraestructure.converter.JpaListaMapper;
import com.visus.central.infraestructure.persistence.repository.JpaListaRepository;

import jakarta.transaction.Transactional;

@Component
public class PostgresListaAdapter implements ListaRepository {

    private final JpaListaRepository jpaRepository;
    private final JpaListaMapper mapper;

    public PostgresListaAdapter(JpaListaRepository jpaRepository, JpaListaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Lista> findAll() {
        return jpaRepository.findAll().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Lista> findById(Integer id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public Lista save(Lista lista) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(lista)));
    }

    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
    }
}
