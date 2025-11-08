package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Coeficiente;
import com.visus.central.domain.port.out.CoeficienteRepository;
import com.visus.central.infraestructure.persistence.entity.JpaCoeficienteEntity;
import com.visus.central.infraestructure.persistence.repository.JpaCoeficienteRepository;

import jakarta.transaction.Transactional;

@Component
public class PostgresCoeficienteAdapter implements CoeficienteRepository {

	private final JpaCoeficienteRepository jpaRepository;

    public PostgresCoeficienteAdapter(JpaCoeficienteRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Coeficiente> findAll() {
        return jpaRepository.findAll().stream()
            .map(this::toModel)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Coeficiente> findById(Integer id) {
        return jpaRepository.findById(id).map(this::toModel);
    }

    @Override
    @Transactional
    public Coeficiente save(Coeficiente coeficiente) {
        JpaCoeficienteEntity entity = toEntity(coeficiente);
        JpaCoeficienteEntity saved = jpaRepository.save(entity);
        return toModel(saved);
    }

    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
    }
    
    private Coeficiente toModel(JpaCoeficienteEntity entity) {
        Coeficiente model = new Coeficiente();
        model.setId(entity.getId());
        model.setDescripcion(entity.getDescripcion());
        model.setCoeficiente(entity.getCoeficiente());
        model.setCuotas(entity.getCuotas());
        return model;
    }

    private JpaCoeficienteEntity toEntity(Coeficiente model) {
        JpaCoeficienteEntity entity = new JpaCoeficienteEntity();
        entity.setId(model.getId());
        entity.setDescripcion(model.getDescripcion());
        entity.setCoeficiente(model.getCoeficiente());
        entity.setCuotas(model.getCuotas());
        return entity;
    }
}
