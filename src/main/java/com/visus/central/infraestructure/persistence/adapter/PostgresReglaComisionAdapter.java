package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.ReglaComision;
import com.visus.central.domain.port.out.ReglaComisionRepository;
import com.visus.central.infraestructure.converter.JpaReglaComisionMapper;
import com.visus.central.infraestructure.persistence.repository.JpaReglaComisionRepository;

@Component
public class PostgresReglaComisionAdapter implements ReglaComisionRepository {

	private final JpaReglaComisionRepository jpaRepository;
	private final JpaReglaComisionMapper mapper;

	public PostgresReglaComisionAdapter(JpaReglaComisionRepository jpaRepository, JpaReglaComisionMapper mapper) {
		this.jpaRepository = jpaRepository;
		this.mapper = mapper;
	}

	@Override
	public ReglaComision save(ReglaComision regla) {
		var entity = mapper.toEntity(regla);
		var saved = jpaRepository.save(entity);
		return mapper.toDomain(saved);
	}

	@Override
	public Optional<ReglaComision> findById(Long id) {
		return jpaRepository.findById(id).map(mapper::toDomain);
	}

	@Override
	public List<ReglaComision> findAll() {
		return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
	}

	@Override
	public List<ReglaComision> findByVendedorId(Integer idVendedor) {
		return jpaRepository.findByVendedorId(idVendedor).stream().map(mapper::toDomain)
				.collect(Collectors.toList());
	}

	@Override
	public List<ReglaComision> findActivas() {
		return jpaRepository.findActivas().stream().map(mapper::toDomain).collect(Collectors.toList());
	}

	@Override
	public Optional<ReglaComision> findActivaByVendedorId(Integer idVendedor) {
		return jpaRepository.findActivaByVendedorId(idVendedor).map(mapper::toDomain);
	}

	@Override
	public Optional<ReglaComision> findReglaGlobal() {
		return jpaRepository.findReglaGlobal().map(mapper::toDomain);
	}

	@Override
	public void deleteById(Long id) {
		jpaRepository.deleteById(id);
	}
}
