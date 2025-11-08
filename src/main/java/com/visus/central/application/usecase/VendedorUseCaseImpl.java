package com.visus.central.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.visus.central.domain.model.Domicilio;
import com.visus.central.domain.model.Vendedor;
import com.visus.central.domain.port.in.DomicilioUseCase;
import com.visus.central.domain.port.in.VendedorUseCase;
import com.visus.central.domain.port.out.VendedorRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class VendedorUseCaseImpl implements VendedorUseCase {
	
	private final VendedorRepository vendedorRepository;
    private final DomicilioUseCase domicilioUseCase;

    public VendedorUseCaseImpl(VendedorRepository vendedorRepository, DomicilioUseCase domicilioUseCase) {
        this.vendedorRepository = vendedorRepository;
        this.domicilioUseCase = domicilioUseCase;
    }

    @Override
    public List<Vendedor> findAll() {
        return vendedorRepository.findAll();
    }

    @Override
    public Vendedor findById(Integer id) {
        return vendedorRepository.findById(id).orElse(null);
    }

    @Override
    public Vendedor save(Vendedor vendedor) {
    	System.out.println("🔄 VendedorUseCaseImpl.save() - Iniciando persistencia completa");

    	// 1. Guardar el vendedor primero para obtener su ID
    	Vendedor vendedorGuardado = vendedorRepository.save(vendedor);
    	System.out.println("✅ Vendedor guardado con ID: " + vendedorGuardado.getId());

    	Integer vendedorId = vendedorGuardado.getId();

    	// 2. Manejar domicilios
    	if (vendedor.getDomicilios() != null) {
    		System.out.println("🔄 Procesando " + vendedor.getDomicilios().size() + " domicilios");

    		// Obtener domicilios existentes en BD
    		List<Domicilio> domiciliosExistentes = domicilioUseCase.findByVendedorId(vendedorId);

    		// Guardar/actualizar domicilios de la lista actual
    		for (Domicilio domicilio : vendedor.getDomicilios()) {
    			domicilio.setIdVendedor(vendedorId);
    			domicilioUseCase.save(domicilio);
    			System.out.println("✅ Domicilio guardado: " + domicilio.getTipoDomicilio() + " ID: " + domicilio.getId());
    		}


    		// Eliminar domicilios que ya no están en la lista
    		for (Domicilio domicilioExistente : domiciliosExistentes) {
    			boolean sigueEnLista = vendedor.getDomicilios().stream()
    					.anyMatch(d -> d.getId() != null && d.getId().equals(domicilioExistente.getId()));
    			if (!sigueEnLista) {
    				domicilioUseCase.deleteById(domicilioExistente.getId());
    				System.out.println("🗑️ Domicilio eliminado: " + domicilioExistente.getId());
    			}
    		}
    	} else {
    		System.out.println("📝 Vendedor sin domicilios para guardar");
    	}

    	System.out.println("✅ Persistencia completa finalizada para vendedor ID: " + vendedorId);
    	return vendedorGuardado;
    }

    @Override
    public void deleteById(Integer id) {
        System.out.println("🔄 Eliminando vendedor ID: " + id);
        
        // Primero eliminar los domicilios asociados
        List<Domicilio> domicilios = domicilioUseCase.findByVendedorId(id);
        for (Domicilio domicilio : domicilios) {
            domicilioUseCase.deleteById(domicilio.getId());
            System.out.println("🗑️ Domicilio eliminado: " + domicilio.getId());
        }

        // Luego eliminar el vendedor
        vendedorRepository.deleteById(id);
        System.out.println("🗑️ Vendedor eliminado: " + id);
    }

    @Override
    public List<Vendedor> findByNombreContainingIgnoreCase(String nombre) {
        return vendedorRepository.buscarPorNombre(nombre);
    }

    @Override
    public boolean existsByEmail(String email) {
        return vendedorRepository.existsByEmail(email);
    }
    
}
