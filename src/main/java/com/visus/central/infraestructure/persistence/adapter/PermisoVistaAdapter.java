package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.PermisoVista;
import com.visus.central.domain.port.out.PermisoVistaRepository;
import com.visus.central.infraestructure.converter.JpaPermisoVistaMapper;
import com.visus.central.infraestructure.persistence.repository.JpaPermisoVistaRepository;

import jakarta.transaction.Transactional;

@Component
public class PermisoVistaAdapter implements PermisoVistaRepository {

	private final JpaPermisoVistaRepository jpaRepo;
	private final JpaPermisoVistaMapper mapper;

	public PermisoVistaAdapter(JpaPermisoVistaRepository jpaRepo, JpaPermisoVistaMapper mapper) {
		this.jpaRepo = jpaRepo;
		this.mapper = mapper;
	}

	@Override
	public List<PermisoVista> findAll() {
		return jpaRepo.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
	}

	@Override
	public Optional<PermisoVista> findByUsuarioIdAndVistaClase(Integer usuarioId, String vistaClase) {
		return jpaRepo.findByUsuarioIdAndVistaClase(usuarioId, vistaClase).map(mapper::toDomain);
	}

	@Override
	public List<PermisoVista> findByUsuarioId(Integer usuarioId) {
		return jpaRepo.findByUsuarioId(usuarioId).stream().map(mapper::toDomain).collect(Collectors.toList());
	}

	@Override
	public List<PermisoVista> findByVistaClase(String vistaClase) {
		return jpaRepo.findByVistaClase(vistaClase).stream().map(mapper::toDomain).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public PermisoVista save(PermisoVista permiso) {
		return mapper.toDomain(jpaRepo.save(mapper.toEntity(permiso)));
	}

	@Override
	@Transactional
	public void deleteById(Integer id) {
		jpaRepo.deleteById(id);
	}

	@Override
	@Transactional
	public void deleteByUsuarioId(Integer usuarioId) {
		jpaRepo.deleteByUsuarioId(usuarioId);
	}

	@Override
	public boolean existsByUsuarioIdAndVistaClase(Integer usuarioId, String vistaClase) {
		return jpaRepo.existsByUsuarioIdAndVistaClase(usuarioId, vistaClase);
	}
}
