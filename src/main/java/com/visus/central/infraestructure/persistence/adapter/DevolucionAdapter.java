package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Devolucion;
import com.visus.central.domain.port.out.DevolucionRepository;
import com.visus.central.infraestructure.converter.JpaDevolucionMapper;
import com.visus.central.infraestructure.persistence.repository.JpaDevolucionRepository;

@Component
public class DevolucionAdapter implements DevolucionRepository {

    private final JpaDevolucionRepository jpaRepository;
    private final JpaDevolucionMapper mapper;

    public DevolucionAdapter(JpaDevolucionRepository jpaRepository, JpaDevolucionMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Devolucion save(Devolucion devolucion) {
        var entity = mapper.toEntity(devolucion);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Devolucion> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Devolucion> findByVentaId(Integer idVenta) {
        return jpaRepository.findByIdVenta(idVenta).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Devolucion> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
