package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Concepto;
import com.visus.central.domain.port.out.ConceptoRepository;
import com.visus.central.infraestructure.persistence.entity.JpaConceptoEntity;
import com.visus.central.infraestructure.persistence.repository.JpaConceptoRepository;

import jakarta.transaction.Transactional;

@Component
public class PostgresConceptoAdapter implements ConceptoRepository {
	
	private final JpaConceptoRepository jpaRepository;

    public PostgresConceptoAdapter(JpaConceptoRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Concepto> findAll() {
        return jpaRepository.findAll().stream()
            .map(this::toModel)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Concepto> findById(Integer id) {
        return jpaRepository.findById(id).map(this::toModel);
    }

    @Override
    @Transactional
    public Concepto save(Concepto concepto) {
        JpaConceptoEntity entity = toEntity(concepto);
        JpaConceptoEntity saved = jpaRepository.save(entity);
        return toModel(saved);
    }
    
    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
    }

    private Concepto toModel(JpaConceptoEntity entity) {
        Concepto model = new Concepto();
        model.setId(entity.getId());
        model.setDescripcion(entity.getDescripcion());
        return model;
    }

    private JpaConceptoEntity toEntity(Concepto model) {
        JpaConceptoEntity entity = new JpaConceptoEntity();
        entity.setId(model.getId());
        entity.setDescripcion(model.getDescripcion());
        return entity;
    }

}
