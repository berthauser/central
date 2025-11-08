package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Comprobante;
import com.visus.central.domain.port.out.ComprobanteRepository;
import com.visus.central.infraestructure.persistence.entity.JpaComprobanteEntity;
import com.visus.central.infraestructure.persistence.repository.JpaComprobanteRepository;

import jakarta.transaction.Transactional;

@Component
public class PostgresComprobanteAdapter implements ComprobanteRepository {

    private final JpaComprobanteRepository jpaRepository;

    public PostgresComprobanteAdapter(JpaComprobanteRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Comprobante> findAll() {
        return jpaRepository.findAll().stream()
            .map(this::toModel)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Comprobante> findById(Integer id) {
        return jpaRepository.findById(id).map(this::toModel);
    }

    @Override
    @Transactional
    public Comprobante save(Comprobante comprobante) {
        JpaComprobanteEntity entity = toEntity(comprobante);
        JpaComprobanteEntity saved = jpaRepository.save(entity);
        return toModel(saved);
    }
    
    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
    }

    private Comprobante toModel(JpaComprobanteEntity entity) {
        Comprobante model = new Comprobante();
        model.setId(entity.getId());
        model.setNombreLargo(entity.getNombreLargo());
        model.setNombreCorto(entity.getNombreCorto());
        model.setNumeroInicial(entity.getNumeroInicial());
        model.setSucursal(entity.getSucursal());
        model.setColumna(entity.getColumna());
        return model;
    }

    private JpaComprobanteEntity toEntity(Comprobante model) {
        JpaComprobanteEntity entity = new JpaComprobanteEntity();
        entity.setId(model.getId());
        entity.setNombreLargo(model.getNombreLargo());
        entity.setNombreCorto(model.getNombreCorto());
        entity.setNumeroInicial(model.getNumeroInicial());
        entity.setSucursal(model.getSucursal());
        entity.setColumna(model.getColumna());
        return entity;
    }

}
