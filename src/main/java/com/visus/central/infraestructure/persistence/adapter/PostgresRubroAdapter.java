package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Rubro;
import com.visus.central.domain.port.out.RubroRepository;
import com.visus.central.infraestructure.converter.JpaRubroMapper;
import com.visus.central.infraestructure.persistence.repository.JpaRubroRepository;

import jakarta.transaction.Transactional;

@Component
public class PostgresRubroAdapter implements RubroRepository {

	private final JpaRubroRepository jpaRepository;
	private final JpaRubroMapper rubroMapper;

	public PostgresRubroAdapter(JpaRubroRepository jpaRepo, JpaRubroMapper mapper) {
		this.jpaRepository = jpaRepo;
		this.rubroMapper = mapper;
	}

	@Override
	public List<Rubro> listar() {
		return jpaRepository.findAll().stream()
				.map(rubroMapper::toDomain)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<Rubro> buscarPorId(Integer id) {
		return jpaRepository.findById(id).map(rubroMapper::toDomain);
	}

	@Override
	@Transactional
	public void guardar(Rubro rubro) {
		jpaRepository.save(rubroMapper.toEntity(rubro));
	}

	@Override
	public void eliminar(Integer id) {
		jpaRepository.deleteById(id);
	}

	@Override
	public List<Rubro> buscarPorNombre(String nombre) {
		return jpaRepository.findByDescripcionContainingIgnoreCase(nombre).stream()
				.map(rubroMapper::toDomain)
				.collect(Collectors.toList());
	}

}
