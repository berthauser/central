package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Alicuota;
import com.visus.central.domain.port.out.AlicuotaRepository;
import com.visus.central.infraestructure.persistence.entity.JpaAlicuotaEntity;
import com.visus.central.infraestructure.persistence.repository.JpaAlicuotaRepository;
import jakarta.transaction.Transactional;

@Component
public class PostgresAlicuotaAdapter implements AlicuotaRepository {
	
	private final JpaAlicuotaRepository jpaRepository;

    public PostgresAlicuotaAdapter(JpaAlicuotaRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	@Override
    public List<Alicuota> findAll() {
        return jpaRepository.findAll().stream()
            .map(this::toModel)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Alicuota> findById(Integer id) {
        return jpaRepository.findById(id).map(this::toModel);
    }

    @Override
    @Transactional
    public Alicuota save(Alicuota alicuota) {
        JpaAlicuotaEntity entity = toEntity(alicuota);
        JpaAlicuotaEntity saved = jpaRepository.save(entity);
        return toModel(saved);
    }

    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
    }
    
    private Alicuota toModel(JpaAlicuotaEntity entity) {
    	Alicuota model = new Alicuota();
        model.setId(entity.getId());
        model.setDescripcion(entity.getDescripcion());
        model.setGravamen(entity.getGravamen());
        return model;
    }

    private JpaAlicuotaEntity toEntity(Alicuota model) {
    	JpaAlicuotaEntity entity = new JpaAlicuotaEntity();
        entity.setId(model.getId());
        entity.setDescripcion(model.getDescripcion());
        entity.setGravamen(model.getGravamen());
        return entity;
    }

}
