package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Medida;
import com.visus.central.domain.port.out.MedidaRepository;
import com.visus.central.infraestructure.converter.JpaMedidaMapper;
import com.visus.central.infraestructure.persistence.repository.JpaMedidaRepository;

import jakarta.transaction.Transactional;

@Component
public class PostgresMedidaAdapter implements MedidaRepository {
	
	
	private final JpaMedidaRepository jpaRepository;
    private final JpaMedidaMapper mapper;

    public PostgresMedidaAdapter(JpaMedidaRepository jpaRepository, JpaMedidaMapper mapper) {
		this.jpaRepository = jpaRepository;
		this.mapper = mapper;
	}

	@Override
    public List<Medida> listar() {
        return jpaRepository.findAll().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public Optional<Medida> buscarPorId(Integer id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void guardar(Medida medida) {
    	jpaRepository.save(mapper.toEntity(medida));
    }

    @Override
    public void eliminar(Integer id) {
    	jpaRepository.deleteById(id);
    }

}
