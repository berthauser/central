package com.visus.central.application.usecase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.visus.central.domain.model.PermisoVista;
import com.visus.central.domain.port.in.PermisoVistaUseCase;
import com.visus.central.domain.port.out.PermisoVistaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PermisoVistaUseCaseImpl implements PermisoVistaUseCase {

	private final PermisoVistaRepository repository;

	public PermisoVistaUseCaseImpl(PermisoVistaRepository repository) {
		this.repository = repository;
	}

	@Override
	public boolean puedeVer(Integer usuarioId, String vistaClase) {
		if (usuarioId == null) return false;
		return repository.findByUsuarioIdAndVistaClase(usuarioId, vistaClase)
				.map(PermisoVista::isPuedeVer)
				.orElse(true);
	}

	@Override
	public List<PermisoVista> obtenerPermisosPorUsuario(Integer usuarioId) {
		return repository.findByUsuarioId(usuarioId);
	}

	@Override
	public List<PermisoVista> obtenerTodos() {
		return repository.findAll();
	}

	@Override
	public void guardar(PermisoVista permiso) {
		repository.save(permiso);
	}

	@Override
	@Transactional
	public void guardarTodos(List<PermisoVista> nuevosPermisos) {
		if (nuevosPermisos.isEmpty()) return;
		Integer usuarioId = nuevosPermisos.getFirst().getUsuarioId();

		List<PermisoVista> existentes = repository.findByUsuarioId(usuarioId);
		Map<String, PermisoVista> mapaExistente = existentes.stream()
				.collect(Collectors.toMap(PermisoVista::getVistaClase, p -> p));

		// Eliminar permisos que ya no están en la lista
		java.util.Set<String> nuevasClaves = nuevosPermisos.stream()
				.map(PermisoVista::getVistaClase)
				.collect(Collectors.toSet());
		for (PermisoVista existente : existentes) {
			if (!nuevasClaves.contains(existente.getVistaClase())) {
				repository.deleteById(existente.getId());
			}
		}

		// Actualizar o insertar según corresponda
		for (PermisoVista nuevo : nuevosPermisos) {
			PermisoVista existente = mapaExistente.get(nuevo.getVistaClase());
			if (existente != null) {
				existente.setPuedeVer(nuevo.isPuedeVer());
				repository.save(existente);
			} else {
				nuevo.setId(null);
				repository.save(nuevo);
			}
		}
	}

	@Override
	public Map<String, Boolean> getMapaPermisos(Integer usuarioId, List<String> vistas) {
		List<PermisoVista> existentes = repository.findByUsuarioId(usuarioId);
		Map<String, Boolean> mapa = existentes.stream()
				.collect(Collectors.toMap(PermisoVista::getVistaClase, PermisoVista::isPuedeVer));
		Map<String, Boolean> resultado = new HashMap<>();
		for (String vista : vistas) {
			resultado.put(vista, mapa.getOrDefault(vista, true));
		}
		return resultado;
	}
}
