package com.visus.central.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.visus.central.domain.model.Cliente;
import com.visus.central.domain.model.Domicilio;
import com.visus.central.domain.model.GrupoFam;
import com.visus.central.domain.port.in.ClienteUseCase;
import com.visus.central.domain.port.in.DomicilioUseCase;
import com.visus.central.domain.port.in.GrupoFamUseCase;
import com.visus.central.domain.port.out.ClienteRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ClienteUseCaseImpl implements ClienteUseCase {

	private final ClienteRepository clienteRepository;
	private final DomicilioUseCase domicilioUseCase;
	private final GrupoFamUseCase grupoFamUseCase;

	public ClienteUseCaseImpl(ClienteRepository clienteRepository, DomicilioUseCase domicilioUseCase, GrupoFamUseCase grupoFamUseCase) {
		this.clienteRepository = clienteRepository;
		this.domicilioUseCase = domicilioUseCase;
		this.grupoFamUseCase = grupoFamUseCase; 
	}

    @Override
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente findById(Integer id) {
    	return clienteRepository.findById(id).orElse(null);
    }

    @Override
    public Cliente save(Cliente cliente) {
    	System.out.println("🔄 ClienteUseCaseImpl.save() - Iniciando persistencia completa");

    	// 1. Guardar el cliente primero para obtener su ID
    	Cliente clienteGuardado = clienteRepository.save(cliente);
    	System.out.println("✅ Cliente guardado con ID: " + clienteGuardado.getId());

    	Integer clienteId = clienteGuardado.getId();

    	// 2. Manejar domicilios - CORREGIDO
    	if (cliente.getDomicilios() != null) {
    		System.out.println("🔄 Procesando " + cliente.getDomicilios().size() + " domicilios");

    		// Obtener domicilios existentes en BD
    		List<Domicilio> domiciliosExistentes = domicilioUseCase.findByClienteId(clienteId);

    		// Guardar/actualizar domicilios de la lista actual
    		for (Domicilio domicilio : cliente.getDomicilios()) {
    			domicilio.setIdCliente(clienteId);
    			domicilioUseCase.save(domicilio);
    			System.out.println("✅ Domicilio guardado: " + domicilio.getTipoDomicilio() + " ID: " + domicilio.getId());
    		}

    		// Eliminar domicilios que ya no están en la lista
    		for (Domicilio domicilioExistente : domiciliosExistentes) {
    			boolean sigueEnLista = cliente.getDomicilios().stream()
    					.anyMatch(d -> d.getId() != null && d.getId().equals(domicilioExistente.getId()));
    			if (!sigueEnLista) {
    				domicilioUseCase.deleteById(domicilioExistente.getId());
    				System.out.println("🗑️ Domicilio eliminado: " + domicilioExistente.getId());
    			}
    		}
    	} else {
    		System.out.println("📝 Cliente sin domicilios para guardar");
    	}

    	// 3. NUEVO: Manejar grupo familiar
    	if (cliente.getGrupoFam() != null) {
    		System.out.println("🔄 Procesando " + cliente.getGrupoFam().size() + " miembros del grupo familiar");

    		// Obtener grupo familiar existente en BD
    		List<GrupoFam> grupoFamExistente = grupoFamUseCase.findByClienteId(clienteId);

    		// Guardar/actualizar miembros de la lista actual
    		for (GrupoFam miembro : cliente.getGrupoFam()) {
    			miembro.setIdCliente(clienteId);
    			grupoFamUseCase.save(miembro);
    			System.out.println("✅ Familiar guardado: " + miembro.getNombre() + " ID: " + miembro.getId());
    		}

    		// Eliminar miembros que ya no están en la lista
    		for (GrupoFam miembroExistente : grupoFamExistente) {
    			boolean sigueEnLista = cliente.getGrupoFam().stream()
    					.anyMatch(m -> m.getId() != null && m.getId().equals(miembroExistente.getId()));
    			if (!sigueEnLista) {
    				grupoFamUseCase.deleteById(miembroExistente.getId());
    				System.out.println("🗑️ Familiar eliminado: " + miembroExistente.getId());
    			}
    		}
    	} else {
    		System.out.println("📝 Cliente sin grupo familiar para guardar");
    	}

    	System.out.println("✅ Persistencia completa finalizada para cliente ID: " + clienteId);
    	return clienteGuardado;

    }

    @Override
    public void deleteById(Integer id) {
        System.out.println("🔄 Eliminando cliente ID: " + id);
        
        // Primero eliminar los domicilios asociados
        List<Domicilio> domicilios = domicilioUseCase.findByClienteId(id);
        for (Domicilio domicilio : domicilios) {
            domicilioUseCase.deleteById(domicilio.getId());
            System.out.println("🗑️ Domicilio eliminado: " + domicilio.getId());
        }

        // NUEVO: Eliminar grupo familiar asociado
        List<GrupoFam> grupoFam = grupoFamUseCase.findByClienteId(id);
        for (GrupoFam miembro : grupoFam) {
            grupoFamUseCase.deleteById(miembro.getId());
            System.out.println("🗑️ Familiar eliminado: " + miembro.getId());
        }

        // Luego eliminar el cliente
        clienteRepository.deleteById(id);
        System.out.println("🗑️ Cliente eliminado: " + id);
    }
    
    @Override
    public List<Cliente> findByNombreContainingIgnoreCase(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public boolean existsByEmail(String email) {
        return clienteRepository.existsByEmail(email);
    }

}
