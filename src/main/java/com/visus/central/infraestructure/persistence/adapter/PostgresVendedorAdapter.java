package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Domicilio;
import com.visus.central.domain.model.Vendedor;
import com.visus.central.domain.port.in.DomicilioUseCase;
import com.visus.central.domain.port.out.VendedorRepository;
import com.visus.central.infraestructure.converter.JpaVendedorMapper;
import com.visus.central.infraestructure.persistence.entity.JpaVendedorEntity;
import com.visus.central.infraestructure.persistence.repository.JpaVendedorRepository;

import jakarta.transaction.Transactional;

@Component
public class PostgresVendedorAdapter implements VendedorRepository {

    private final JpaVendedorRepository repository;
    private final JpaVendedorMapper vendedorMapper;
    private final DomicilioUseCase domicilioUseCase; 

    public PostgresVendedorAdapter(JpaVendedorRepository repository, JpaVendedorMapper vendedorMapper, DomicilioUseCase domicilioUseCase) {
        this.repository = repository;
		this.vendedorMapper = vendedorMapper;
		this.domicilioUseCase = domicilioUseCase;
    }

    @Override
    public List<Vendedor> findAll() {
    	return repository.findAll().stream()
                .map(entity -> {
                    Vendedor vendedor = vendedorMapper.toModel(entity);
                    List<Domicilio> domicilios = domicilioUseCase.findByVendedorId(entity.getId());
                    vendedor.setDomicilios(domicilios);
                    return vendedor;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Vendedor> findById(Integer id) {
    	return repository.findById(id)
    			.map(entity -> {
    				Vendedor vendedor = vendedorMapper.toModel(entity);
    				// Cargar domicilios asociados
    				List<Domicilio> domicilios = domicilioUseCase.findByVendedorId(id);
    				vendedor.setDomicilios(domicilios);
    				return vendedor;
    			});
    }

    @Override
    @Transactional
    public Vendedor save(Vendedor model) {
    	JpaVendedorEntity entity = vendedorMapper.toEntity(model);
    	JpaVendedorEntity saved = repository.save(entity);
    	return vendedorMapper.toModel(saved); // DEVUELVE EL VENDEDOR GUARDADO
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public List<Vendedor> buscarPorNombre(String nombre) {
    	 return repository.findByNombreContainingIgnoreCase(nombre).stream()
    	            .map(entity -> {
    	                Vendedor vendedor = vendedorMapper.toModel(entity);
    	                List<Domicilio> domicilios = domicilioUseCase.findByVendedorId(entity.getId());
    	                vendedor.setDomicilios(domicilios);
    	                return vendedor;
    	            })
    	            .collect(Collectors.toList());
    }
    
}
