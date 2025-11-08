package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.GrupoFam;
import com.visus.central.domain.port.out.GrupoFamRepository;
import com.visus.central.infraestructure.persistence.entity.JpaClienteEntity;
import com.visus.central.infraestructure.persistence.entity.JpaGrupoFamEntity;
import com.visus.central.infraestructure.persistence.repository.JpaGrupoFamRepository;

import jakarta.transaction.Transactional;

@Component
public class PostgresGrupoFamAdapter implements GrupoFamRepository {

    private final JpaGrupoFamRepository jpaRepository;

    public PostgresGrupoFamAdapter(JpaGrupoFamRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<GrupoFam> findAll() {
        return jpaRepository.findAll().stream()
            .map(this::toModel)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<GrupoFam> findById(Integer id) {
        return jpaRepository.findById(id).map(this::toModel);
    }

    @Override
    @Transactional
    public GrupoFam save(GrupoFam grupoFam) {
        JpaGrupoFamEntity entity = toEntity(grupoFam);
        JpaGrupoFamEntity saved = jpaRepository.save(entity);
        return toModel(saved);
    }
    
    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<GrupoFam> findByNombreContainingIgnoreCase(String nombre) {
        return jpaRepository.findByNombreContainingIgnoreCase(nombre).stream()
            .map(this::toModel)
            .collect(Collectors.toList());
    }

    @Override
    public List<GrupoFam> findByClienteId(Integer idCliente) {
        return jpaRepository.findByClienteId(idCliente).stream()
            .map(this::toModel)
            .collect(Collectors.toList());
    }

    @Override
    public boolean existsByNumero(Integer numero) {
        return jpaRepository.existsByNumero(numero);
    }
    
    private GrupoFam toModel(JpaGrupoFamEntity entity) {
        GrupoFam model = new GrupoFam();
        model.setId(entity.getIdgrupofam());
        model.setNombre(entity.getNombre());
        model.setIdCliente(entity.getCliente() != null ? entity.getCliente().getId() : null);
        model.setNumero(entity.getNumero());
        model.setEstado(entity.getEstado());
        model.setParentesco(entity.getParentesco());
        model.setDocumento(entity.getTipoDocumento());
        return model;
    }

    private JpaGrupoFamEntity toEntity(GrupoFam model) {
        JpaGrupoFamEntity entity = new JpaGrupoFamEntity();
        entity.setIdgrupofam(model.getId());
        entity.setNombre(model.getNombre());
        
        if (model.getIdCliente() != null) {
            JpaClienteEntity cliente = new JpaClienteEntity();
            cliente.setId(model.getIdCliente());
            entity.setCliente(cliente);
        }
        
        entity.setNumero(model.getNumero());
        entity.setEstado(model.getEstado());
        entity.setParentesco(model.getParentesco());
        entity.setTipoDocumento(model.getDocumento());
        return entity;
    }

}
