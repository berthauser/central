package com.visus.central.infraestructure.persistence.adapter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Pago;
import com.visus.central.domain.port.out.PagoRepository;
import com.visus.central.infraestructure.converter.JpaPagoMapper;
import com.visus.central.infraestructure.persistence.entity.PagoEntity;
import com.visus.central.infraestructure.persistence.repository.JpaPagoRepository;

import jakarta.transaction.Transactional;

@Component
public class PagoAdapter implements PagoRepository {

	private final JpaPagoRepository jpaRepository;
	private final JpaPagoMapper mapper;

	public PagoAdapter(JpaPagoRepository jpaRepository, JpaPagoMapper mapper) {
		this.jpaRepository = jpaRepository;
		this.mapper = mapper;
	}

	@Override
	public Pago save(Pago pago) {
		PagoEntity entity = mapper.toJpa(pago);
		PagoEntity saved = jpaRepository.save(entity);
		return mapper.toDomain(saved);
	}

	@Override
	public Optional<Pago> findById(Long id) {
		return jpaRepository.findById(id).map(mapper::toDomain);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		jpaRepository.deleteById(id);
	}

	@Override
	public List<Pago> findAll() {
		return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
	}

	@Override
	public boolean existsById(Long id) {
		return jpaRepository.existsById(id);
	}

	@Override
	public List<Pago> findByIdCliente(Integer idCliente) {
		return jpaRepository.findByIdCliente(idCliente).stream().map(mapper::toDomain).collect(Collectors.toList());
	}

	@Override
	public List<Pago> findByIdClienteAndFechaBetween(Integer idCliente, LocalDate fechaInicio, LocalDate fechaFin) {
		// El método en JpaPagoRepository debería llamarse findByIdClienteAndFechaBetween
		return jpaRepository.findByIdClienteAndFechaBetween(idCliente, fechaInicio, fechaFin).stream()
				.map(mapper::toDomain).collect(Collectors.toList());
	}

	@Override
	public List<Pago> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin) {
		return jpaRepository.findByFechaBetween(fechaInicio, fechaFin).stream().map(mapper::toDomain)
				.collect(Collectors.toList());
	}

	@Override
	public BigDecimal sumMontoTotalByVentaId(Integer idVenta) {
		return jpaRepository.sumMontoTotalByIdVenta(idVenta);
	}

}
