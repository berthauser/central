package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Porcentual;
import com.visus.central.domain.port.out.PorcentualRepository;
import com.visus.central.infraestructure.converter.JpaPorcentualMapper;
import com.visus.central.infraestructure.persistence.repository.JpaPorcentualRepository;

import jakarta.transaction.Transactional;

@Component
public class PostgresPorcentualAdapter implements PorcentualRepository {

    private final JpaPorcentualRepository jpaRepository;
    private final JpaPorcentualMapper mapper;

    public PostgresPorcentualAdapter(JpaPorcentualRepository jpaRepository, JpaPorcentualMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Porcentual> findAll() {
        return jpaRepository.findAll().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Porcentual> findById(Integer id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public Porcentual save(Porcentual porcentual) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(porcentual)));
    }

    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
    }
}
