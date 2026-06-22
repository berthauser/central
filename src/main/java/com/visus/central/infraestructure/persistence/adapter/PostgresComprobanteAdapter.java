package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Comprobante;
import com.visus.central.domain.model.NombreCorto;
import com.visus.central.domain.port.out.ComprobanteRepository;
import com.visus.central.infraestructure.converter.JpaComprobanteMapper;
import com.visus.central.infraestructure.persistence.entity.JpaComprobanteEntity;
import com.visus.central.infraestructure.persistence.repository.JpaComprobanteRepository;

import jakarta.transaction.Transactional;

@Component
public class PostgresComprobanteAdapter implements ComprobanteRepository {

	private final JpaComprobanteRepository jpaRepository;
	private final JpaComprobanteMapper mapper; // ← inyectar mapper

	public PostgresComprobanteAdapter(JpaComprobanteRepository jpaRepository, JpaComprobanteMapper mapper) {
		this.jpaRepository = jpaRepository;
		this.mapper = mapper;
	}

	@Override
	public List<Comprobante> findAll() {
		return jpaRepository.findAll().stream().map(mapper::toModel).collect(Collectors.toList());
	}

	@Override
	public Optional<Comprobante> findActivo() {
		return jpaRepository.findByActivoTrue().map(mapper::toModel);
	}

	@Override
	public Optional<Comprobante> findById(Integer id) {
		return jpaRepository.findById(id).map(mapper::toModel);
	}

	@Override
	@Transactional
	public Comprobante save(Comprobante comprobante) {
		JpaComprobanteEntity entity = mapper.toEntity(comprobante);
		JpaComprobanteEntity saved = jpaRepository.save(entity);
		return mapper.toModel(saved);
	}

	@Override
	public void deleteById(Integer id) {
		jpaRepository.deleteById(id);
	}

	@Override
	public Optional<Comprobante> findByNombreCorto(String nombreCorto) {
		try {
	        NombreCorto enumValue = NombreCorto.valueOf(nombreCorto);
	        return jpaRepository.findByNombreCorto(enumValue).map(mapper::toModel);
	    } catch (IllegalArgumentException e) {
	        throw new RuntimeException("Nombre de comprobante inválido: " + nombreCorto);
	    }
	}

}
