package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Localidad;
import com.visus.central.domain.port.out.LocalidadRepository;
import com.visus.central.infraestructure.converter.JpaLocalidadMapper;
import com.visus.central.infraestructure.persistence.repository.JpaLocalidadRepository;

import jakarta.transaction.Transactional;

@Component
public class PostgresLocalidadAdapter implements LocalidadRepository {
	
	private final JpaLocalidadRepository jpaRepo;
    private final JpaLocalidadMapper mapper;

    public PostgresLocalidadAdapter(JpaLocalidadRepository jpaRepo, JpaLocalidadMapper mapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
    }

    @Override
    public List<Localidad> listar() {
        return jpaRepo.findAll().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public Optional<Localidad> buscarPorId(Integer id) {
        return jpaRepo.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void guardar(Localidad localidad) {
        jpaRepo.save(mapper.toEntity(localidad));
    }

    @Override
    public void eliminar(Integer id) {
        jpaRepo.deleteById(id);
    }

    @Override
    public boolean existePorDepartamentoId(Integer idDepartamento) {
        return jpaRepo.existsByDepartamento_Id(idDepartamento);
    }

}
