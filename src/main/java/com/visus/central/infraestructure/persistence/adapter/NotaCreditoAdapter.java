package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.NotaCredito;
import com.visus.central.domain.port.out.NotaCreditoRepository;
import com.visus.central.infraestructure.converter.JpaNotaCreditoMapper;
import com.visus.central.infraestructure.persistence.repository.JpaNotaCreditoRepository;

import jakarta.transaction.Transactional;

@Component
public class NotaCreditoAdapter implements NotaCreditoRepository {

	private final JpaNotaCreditoRepository jpaRepo;
	private final JpaNotaCreditoMapper mapper;

	public NotaCreditoAdapter(JpaNotaCreditoRepository jpaRepo, JpaNotaCreditoMapper mapper) {
		this.jpaRepo = jpaRepo;
		this.mapper = mapper;
	}

	@Override
	@Transactional
	public NotaCredito save(NotaCredito notaCredito) {
		return mapper.toDomain(jpaRepo.save(mapper.toEntity(notaCredito)));
	}

	@Override
	public List<NotaCredito> findNoConsumidosByClienteId(Integer idCliente) {
		return jpaRepo.findByClienteIdAndConsumidoFalse(idCliente).stream()
				.map(mapper::toDomain)
				.collect(Collectors.toList());
	}
}
