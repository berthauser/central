package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Domicilio;
import com.visus.central.domain.port.out.DomicilioRepository;
import com.visus.central.infraestructure.converter.JpaDomicilioMapper;
import com.visus.central.infraestructure.persistence.entity.JpaDomicilioEntity;
import com.visus.central.infraestructure.persistence.entity.JpaVendedorEntity;
import com.visus.central.infraestructure.persistence.repository.JpaDomicilioRepository;

import jakarta.transaction.Transactional;

@Component
public class DomicilioAdapter implements DomicilioRepository {

    private final JpaDomicilioRepository repository;
    private final JpaDomicilioMapper domicilioMapper;

    public DomicilioAdapter(JpaDomicilioRepository repository, JpaDomicilioMapper domicilioMapper) {
        this.repository = repository;
		this.domicilioMapper = domicilioMapper;
    }

    @Override
    public List<Domicilio> findAll() {
        return repository.findAll().stream()
            .map(domicilioMapper::toModel)
            .collect(Collectors.toList());
    }

    @Override
    public Domicilio findById(Integer id) {
        return repository.findById(id)
            .map(domicilioMapper::toModel)
            .orElse(null);
    }

    @Override
    @Transactional
    public Domicilio save(Domicilio model) {
        JpaDomicilioEntity entity = domicilioMapper.toEntity(model);
        JpaDomicilioEntity saved = repository.save(entity);
        return domicilioMapper.toModel(saved);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<Domicilio> findByVendedorId(Integer idVendedor) {
        JpaVendedorEntity vendedorRef = new JpaVendedorEntity();
        vendedorRef.setId(idVendedor);

        return repository.findByVendedor(vendedorRef).stream()
            .map(domicilioMapper::toModel)
            .collect(Collectors.toList());
    }
    
  @Override
  public List<Domicilio> findByClienteId(Integer idCliente) {
	  return repository.findByClienteId(idCliente).stream()
              .map(domicilioMapper::toModel)
              .collect(Collectors.toList());
  }

}
