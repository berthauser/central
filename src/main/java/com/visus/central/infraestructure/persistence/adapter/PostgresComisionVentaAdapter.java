package com.visus.central.infraestructure.persistence.adapter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.ComisionVenta;
import com.visus.central.domain.port.out.ComisionVentaRepository;
import com.visus.central.infraestructure.converter.JpaComisionVentaMapper;
import com.visus.central.infraestructure.persistence.repository.JpaComisionVentaRepository;

@Component
public class PostgresComisionVentaAdapter implements ComisionVentaRepository {

	private final JpaComisionVentaRepository jpaRepository;
	private final JpaComisionVentaMapper mapper;

	public PostgresComisionVentaAdapter(JpaComisionVentaRepository jpaRepository, JpaComisionVentaMapper mapper) {
		this.jpaRepository = jpaRepository;
		this.mapper = mapper;
	}

	@Override
	public ComisionVenta save(ComisionVenta comision) {
		var entity = mapper.toEntity(comision);
		var saved = jpaRepository.save(entity);
		return mapper.toDomain(saved);
	}

	@Override
	public Optional<ComisionVenta> findById(Long id) {
		return jpaRepository.findById(id).map(mapper::toDomain);
	}

	@Override
	public List<ComisionVenta> findAll() {
		return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
	}

	@Override
	public List<ComisionVenta> findByVendedorId(Integer idVendedor) {
		return jpaRepository.findByVendedorId(idVendedor).stream().map(mapper::toDomain)
				.collect(Collectors.toList());
	}

	@Override
	public List<ComisionVenta> findByVentaId(Integer idVenta) {
		return jpaRepository.findByVentaId(idVenta).stream().map(mapper::toDomain)
				.collect(Collectors.toList());
	}

	@Override
	public List<ComisionVenta> findByFechaCalculoBetween(LocalDate desde, LocalDate hasta) {
		return jpaRepository.findByFechaCalculoBetween(desde, hasta).stream().map(mapper::toDomain)
				.collect(Collectors.toList());
	}

	@Override
	public List<ComisionVenta> findByEstado(String estado) {
		return jpaRepository.findByEstado(estado).stream().map(mapper::toDomain)
				.collect(Collectors.toList());
	}

	@Override
	public boolean existsByPagoId(Long idPago) {
		return jpaRepository.existsByIdPago(idPago);
	}

	@Override
	public void deleteById(Long id) {
		jpaRepository.deleteById(id);
	}
}
