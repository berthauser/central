package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Presentacion;
import com.visus.central.domain.port.out.PresentacionRepository;
import com.visus.central.infraestructure.converter.JpaPresentacionMapper;
import com.visus.central.infraestructure.persistence.repository.JpaPresentacionRepository;
import jakarta.transaction.Transactional;

@Component
public class PostgresPresentacionAdapter implements PresentacionRepository {
	
	private final JpaPresentacionRepository jpaRepository;

	private final JpaPresentacionMapper presentacionMapper;

	public PostgresPresentacionAdapter(JpaPresentacionRepository jpaRepository,
			JpaPresentacionMapper presentacionMapper) {
		this.jpaRepository = jpaRepository;
		this.presentacionMapper = presentacionMapper;
	}

	@Override
	public List<Presentacion> listar() {
		return jpaRepository.findAll().stream()
				.map(presentacionMapper::toDomain)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<Presentacion> buscarPorId(Integer id) {
		return jpaRepository.findById(id).map(presentacionMapper::toDomain);
	}

	@Override
	@Transactional
	public void guardar(Presentacion presentacion) {
		jpaRepository.save(presentacionMapper.toEntity(presentacion));
	}

	@Override
	public void eliminar(Integer id) {
		jpaRepository.deleteById(id);
	}

	@Override
	public List<Presentacion> buscarPorDescripcion(String descripcion) {
		return jpaRepository.findByDescripcionContainingIgnoreCase(descripcion).stream()
				.map(presentacionMapper::toDomain)
				.collect(Collectors.toList());
	}

}
