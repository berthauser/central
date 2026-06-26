package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Cliente;
import com.visus.central.domain.model.ClienteComboDTO;
import com.visus.central.domain.model.Domicilio;
import com.visus.central.domain.model.Estado;
import com.visus.central.domain.port.in.DomicilioUseCase;
import com.visus.central.domain.port.out.ClienteRepository;
import com.visus.central.infraestructure.converter.JpaClienteMapper;
import com.visus.central.infraestructure.persistence.entity.JpaClienteEntity;
import com.visus.central.infraestructure.persistence.repository.JpaClienteRepository;
import jakarta.transaction.Transactional;

@Component
public class PostgresClienteAdapter implements ClienteRepository {

    private final JpaClienteRepository jpaRepository;
    private final JpaClienteMapper clienteMapper;
    private final DomicilioUseCase domicilioUseCase; 

	public PostgresClienteAdapter(JpaClienteRepository jpaRepository, DomicilioUseCase domicilioUseCase,
			JpaClienteMapper clienteMapper) {
		this.jpaRepository = jpaRepository;
		this.clienteMapper = clienteMapper;
		this.domicilioUseCase = domicilioUseCase;
	}

    @Override
    public List<Cliente> findAll() {
        // CARGA LIGERA: solo datos básicos del cliente, SIN domicilios
        return jpaRepository.findAll().stream()
            .map(clienteMapper::toModel) // SOLO MAPEA CLIENTE, NO DOMICILIOS
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Cliente> findAllClientesBasico() {
        return jpaRepository.findByEstado(Estado.Habilitado).stream()
                .map(clienteMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Cliente> findById(Integer id) {
    	// CARGA PESADA: datos completos del cliente + domicilios
    	return jpaRepository.findById(id)
    			.map(entity -> {
    				Cliente cliente = clienteMapper.toModel(entity);
    				List<Domicilio> domicilios = domicilioUseCase.findByClienteId(id);
    				cliente.setDomicilios(domicilios);
    				return cliente;
    			});
    }

    @Override
    @Transactional
    public Cliente save(Cliente cliente) {
        JpaClienteEntity entity = clienteMapper.toEntity(cliente);
        JpaClienteEntity saved = jpaRepository.save(entity);
        return clienteMapper.toModel(saved);
    }
    
    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public List<Cliente> findByNombreContainingIgnoreCase(String nombre) {
        // CARGA LIGERA: solo datos básicos del cliente, SIN domicilios
        List<JpaClienteEntity> entities = jpaRepository
            .findByNombreFantasiaContainingIgnoreCaseOrNombreClienteContainingIgnoreCase(
                nombre, nombre);
        
        return entities.stream()
            .map(clienteMapper::toModel) // SOLO MAPEA CLIENTE, NO DOMICILIOS
            .collect(Collectors.toList());
    }
    
    @Override
    public List<ClienteComboDTO> findClientesParaCombo() {
        return jpaRepository.findClientesParaCombo();
    }

}
