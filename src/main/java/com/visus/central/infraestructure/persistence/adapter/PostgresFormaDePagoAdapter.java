package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Coeficiente;
import com.visus.central.domain.model.FormaDePago;
import com.visus.central.domain.port.out.FormaDePagoRepository;
import com.visus.central.infraestructure.persistence.entity.JpaFormaDePagoEntity;
import com.visus.central.infraestructure.persistence.repository.JpaCoeficienteRepository;
import com.visus.central.infraestructure.persistence.repository.JpaFormaDePagoRepository;

import jakarta.transaction.Transactional;

import com.visus.central.infraestructure.converter.JpaCoeficienteMapper;

@Component
public class PostgresFormaDePagoAdapter implements FormaDePagoRepository {

	private final JpaFormaDePagoRepository jpaRepository;
	private final JpaCoeficienteRepository coeficienteRepository; 

    public PostgresFormaDePagoAdapter(JpaFormaDePagoRepository jpaRepository, JpaCoeficienteRepository coeficienteRepository) {
        this.jpaRepository = jpaRepository;
		this.coeficienteRepository = coeficienteRepository;
    }

    @Override
    public List<FormaDePago> findAll() {
        return jpaRepository.findAll().stream()
            .map(this::toModel)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<FormaDePago> findById(Integer id) {
        return jpaRepository.findById(id).map(this::toModel);
    }

    @Override
    @Transactional
    public FormaDePago save(FormaDePago formaDePago) {
        JpaFormaDePagoEntity entity = toEntity(formaDePago);
        JpaFormaDePagoEntity saved = jpaRepository.save(entity);
        return toModel(saved);
    }

    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
    }
    
    @Override
    public List<Coeficiente> findAllCoeficientes() {
        return coeficienteRepository.findAll().stream()
            .map(JpaCoeficienteMapper::toModel)
            .collect(Collectors.toList());
    }
    
    private FormaDePago toModel(JpaFormaDePagoEntity entity) {
        FormaDePago model = new FormaDePago();
        model.setId(entity.getId());
        model.setModalidad(entity.getModalidad());
        model.setCoeficiente(entity.getCoeficiente() != null ? 
                JpaCoeficienteMapper.toModel(entity.getCoeficiente()) : null);
        model.setEsDtoProntoPago(entity.getEsDtoProntoPago());
        model.setDtoProntoPago(entity.getDtoProntoPago());
        model.setEsMesesCompletos(entity.getEsMesesCompletos());
        return model;
    }

    private JpaFormaDePagoEntity toEntity(FormaDePago model) {
        JpaFormaDePagoEntity entity = new JpaFormaDePagoEntity();
        entity.setId(model.getId());
        entity.setModalidad(model.getModalidad());
        entity.setCoeficiente(model.getCoeficiente() != null ? 
                JpaCoeficienteMapper.toEntity(model.getCoeficiente()) : null);
        entity.setEsDtoProntoPago(model.getEsDtoProntoPago());
        entity.setDtoProntoPago(model.getDtoProntoPago());
        entity.setEsMesesCompletos(model.getEsMesesCompletos());
        return entity;
    }
    
}
