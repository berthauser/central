package com.visus.central.infraestructure.persistence.adapter;

import com.visus.central.domain.model.Departamento;
import com.visus.central.domain.port.out.DepartamentoRepository;
import com.visus.central.infraestructure.converter.JpaDepartamentoMapper;
import com.visus.central.infraestructure.persistence.repository.JpaDepartamentoRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PostgresDepartamentoAdapter implements DepartamentoRepository {

    private final JpaDepartamentoRepository jpaRepository;
    private final JpaDepartamentoMapper deptoMapper;

    public PostgresDepartamentoAdapter(JpaDepartamentoRepository jpaRepo, JpaDepartamentoMapper mapper) {
        this.jpaRepository = jpaRepo;
        this.deptoMapper = mapper;
    }

    @Override
    public List<Departamento> listar() {
        return jpaRepository.findAll().stream()
            .map(deptoMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Departamento> buscarPorId(Integer id) {
        return jpaRepository.findById(id).map(deptoMapper::toDomain);
    }

    @Override
    @Transactional
    public void guardar(Departamento model) {
    	jpaRepository.save(deptoMapper.toEntity(model));
    }

    @Override
    public void eliminar(Integer id) {
    	jpaRepository.deleteById(id);
    }
    
    @Override
    public List<Departamento> buscarPorNombre(String nombre) {
        return jpaRepository.findByNombreContainingIgnoreCase(nombre).stream()
            .map(deptoMapper::toDomain)
            .collect(Collectors.toList());
    }

}