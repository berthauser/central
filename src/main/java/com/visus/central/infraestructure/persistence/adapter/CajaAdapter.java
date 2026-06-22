package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.visus.central.domain.model.Caja;
import com.visus.central.domain.model.EstadoCaja;
import com.visus.central.domain.port.out.CajaRepository;
import com.visus.central.infraestructure.converter.CajaMapper;
import com.visus.central.infraestructure.persistence.entity.CajaEntity;
import com.visus.central.infraestructure.persistence.repository.JpaCajaRepository;

@Repository
public class CajaAdapter implements CajaRepository {

	@Autowired
	private JpaCajaRepository jpaRepository;

	@Autowired
	private CajaMapper mapper; // conversor Entity <-> Domain

	@Override
	public Caja save(Caja caja) {
		CajaEntity entity = mapper.toEntity(caja);
		CajaEntity saved = jpaRepository.save(entity);
		return mapper.toDomain(saved);
	}

	@Override
	public Optional<Caja> findById(Integer id) {
		return jpaRepository.findById(id).map(mapper::toDomain);
	}

	@Override
	public Optional<Caja> findCajaAbierta() {
		// Asumiendo que en la base el estado se guarda como "ABIERTA"
		return jpaRepository.findByEstado(EstadoCaja.ABIERTA).map(mapper::toDomain);
	}

	@Override
	public List<Caja> findAll() {
		return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
	}

}
