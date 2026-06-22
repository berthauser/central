package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Unidad;
import com.visus.central.domain.port.out.UnidadRepository;
import com.visus.central.infraestructure.converter.JpaUnidadMapper;
import com.visus.central.infraestructure.persistence.repository.JpaUnidadRepository;

@Component
public class PostgresUnidadAdapter implements UnidadRepository {

	private final JpaUnidadRepository jpaUnidadRepository;
	private final JpaUnidadMapper unidadMapper;

	public PostgresUnidadAdapter(JpaUnidadRepository jpaUnidadRepository, JpaUnidadMapper unidadMapper) {
		this.jpaUnidadRepository = jpaUnidadRepository;
		this.unidadMapper = unidadMapper;
	}

	@Override
	public List<Unidad> findByPresentacionId(Integer idPresentacion) {
		return jpaUnidadRepository.findByIdPresentacionOrderByMedida(idPresentacion).stream()
				.map(unidadMapper::toModel)
				.collect(Collectors.toList());
	}
}
