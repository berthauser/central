package com.visus.central.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.visus.central.domain.model.Banco;
import com.visus.central.domain.model.Proveedor;
import com.visus.central.domain.port.in.ProveedorUseCase;
import com.visus.central.domain.port.out.ProveedorRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProveedorUseCaseImpl implements ProveedorUseCase {

	private final ProveedorRepository proveedorRepository;

	public ProveedorUseCaseImpl(ProveedorRepository proveedorRepository) {
		this.proveedorRepository = proveedorRepository;
	}

	@Override
	public List<Proveedor> findAll() {
		return proveedorRepository.findAll();
	}

	@Override
	public Proveedor findById(Integer id) {
		return proveedorRepository.findById(id)
				.orElse(null);
	}

	@Override
	public Proveedor save(Proveedor proveedor) {
		// Validaciones antes de guardar
		validarProveedor(proveedor);
		return proveedorRepository.save(proveedor);
	}

	@Override
	public void deleteById(Integer id) {
		proveedorRepository.deleteById(id);
	}

	@Override
	public List<Proveedor> findByNombreContainingIgnoreCase(String nombre) {
		return proveedorRepository.findByNombreContainingIgnoreCase(nombre);
	}
	@Override
	public boolean existsByEmail(String email) {
		return proveedorRepository.existsByEmail(email);
	}

	@Override
	public List<Banco> findAllBancos() {
		return proveedorRepository.findAllBancos();
	}

	// MÉTODOS AUXILIARES PRIVADOS
	private void validarProveedor(Proveedor proveedor) {
		if (proveedor == null) {
			throw new IllegalArgumentException("El proveedor no puede ser null");
		}

		// Validar campos obligatorios
		if (proveedor.getNombreFantasia() == null || proveedor.getNombreFantasia().trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre fantasía es obligatorio");
		}

		if (proveedor.getNombreReal() == null || proveedor.getNombreReal().trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre real es obligatorio");
		}

		if (proveedor.getSituacionFiscal() == null) {
			throw new IllegalArgumentException("La situación fiscal es obligatoria");
		}

		if (proveedor.getDocumento() == null) {
			throw new IllegalArgumentException("El tipo de documento es obligatorio");
		}

		// Validar unicidad de email (si se proporciona)
		if (proveedor.getEmail() != null && !proveedor.getEmail().trim().isEmpty()) {
			if (existsByEmail(proveedor.getEmail().trim())) {
				// Verificar si es una actualización del mismo proveedor
				Proveedor existente = findById(proveedor.getId());
				if (existente == null || !existente.getEmail().equals(proveedor.getEmail().trim())) {
					throw new IllegalArgumentException("El email ya está registrado por otro proveedor");
				}
			}
		}

		// Validar relación con banco (si se proporciona)
		if (proveedor.getBanco() != null && proveedor.getBanco().getId() == null) {
			throw new IllegalArgumentException("El banco seleccionado no es válido");
		}
	}

}
