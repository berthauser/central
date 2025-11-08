package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Domicilio;
import com.visus.central.domain.port.out.DomicilioRepository;
import com.visus.central.infraestructure.converter.JpaDomicilioMapper;
import com.visus.central.infraestructure.persistence.entity.JpaClienteEntity;
import com.visus.central.infraestructure.persistence.entity.JpaDomicilioEntity;
import com.visus.central.infraestructure.persistence.entity.JpaVendedorEntity;
import com.visus.central.infraestructure.persistence.repository.JpaDomicilioRepository;

import jakarta.transaction.Transactional;

@Component
public class PostgresDomicilioAdapter implements DomicilioRepository {

    private final JpaDomicilioRepository repository;

    public PostgresDomicilioAdapter(JpaDomicilioRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Domicilio> findAll() {
        return repository.findAll().stream()
            .map(JpaDomicilioMapper::toModel)
            .collect(Collectors.toList());
    }

    @Override
    public Domicilio findById(Integer id) {
        return repository.findById(id)
            .map(JpaDomicilioMapper::toModel)
            .orElse(null);
    }

    @Override
    @Transactional
    public Domicilio save(Domicilio model) {
        JpaDomicilioEntity entity = JpaDomicilioMapper.toEntity(model);
        JpaDomicilioEntity saved = repository.save(entity);
        return JpaDomicilioMapper.toModel(saved);
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
            .map(JpaDomicilioMapper::toModel)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Domicilio> findByClienteId(Integer idCliente) {
    	JpaClienteEntity clienteRef = new JpaClienteEntity();
    	clienteRef.setId(idCliente);
    	
    	return repository.findByCliente(clienteRef).stream()
    			.map(JpaDomicilioMapper::toModel)
    			.collect(Collectors.toList());
    }

}
