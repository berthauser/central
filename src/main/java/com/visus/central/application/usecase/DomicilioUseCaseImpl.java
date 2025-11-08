package com.visus.central.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.visus.central.domain.model.Domicilio;
import com.visus.central.domain.port.in.DomicilioUseCase;
import com.visus.central.domain.port.out.DomicilioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DomicilioUseCaseImpl implements DomicilioUseCase {

    private final DomicilioRepository repository;

    public DomicilioUseCaseImpl(DomicilioRepository repository) {
    	
        this.repository = repository;
        System.out.println("DomicilioUseCaseImpl creado");
    }

    @Override
    public List<Domicilio> findAll() {
        return repository.findAll();
    }

    @Override
    public Domicilio findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Domicilio save(Domicilio model) {
        return repository.save(model);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<Domicilio> findByVendedorId(Integer idVendedor) {
        return repository.findByVendedorId(idVendedor);
    }
    
    @Override
    public List<Domicilio> findByClienteId(Integer idCliente) {
    	return repository.findByClienteId(idCliente);
    }
}

