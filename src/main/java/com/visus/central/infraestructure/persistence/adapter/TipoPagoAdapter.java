package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Coeficiente;
import com.visus.central.domain.model.TipoPago;
import com.visus.central.domain.port.out.TipoPagoRepository;
import com.visus.central.infraestructure.converter.JpaCoeficienteMapper;
import com.visus.central.infraestructure.converter.TipoPagoMapper;
import com.visus.central.infraestructure.persistence.entity.TipoPagoEntity;
import com.visus.central.infraestructure.persistence.repository.JpaCoeficienteRepository;
import com.visus.central.infraestructure.persistence.repository.JpaTipoPagoRepository;

@Component
public class TipoPagoAdapter implements TipoPagoRepository {

	private final JpaTipoPagoRepository tipoPagoRepository;
	private final TipoPagoMapper tipoPagoMapper;
	private final JpaCoeficienteRepository coeficienteRepository;
	private final JpaCoeficienteMapper coeficienteMapper;

	public TipoPagoAdapter(JpaTipoPagoRepository tipoPagoRepository, TipoPagoMapper tipoPagoMapper,
			JpaCoeficienteRepository coeficienteRepository, JpaCoeficienteMapper coeficienteMapper) {
		this.tipoPagoRepository = tipoPagoRepository;
		this.tipoPagoMapper = tipoPagoMapper;
		this.coeficienteRepository = coeficienteRepository;
		this.coeficienteMapper = coeficienteMapper;
	}

	@Override
	public List<TipoPago> findAll() {
		return tipoPagoRepository.findAll().stream().map(tipoPagoMapper::toModel).collect(Collectors.toList());
	}

	@Override
	public Optional<TipoPago> findById(Integer id) {
		return tipoPagoRepository.findById(id).map(tipoPagoMapper::toModel);
	}

	@Override
	public TipoPago save(TipoPago tipoPago) {
		TipoPagoEntity entity = tipoPagoMapper.toEntity(tipoPago);
		TipoPagoEntity saved = tipoPagoRepository.save(entity);
		return tipoPagoMapper.toModel(saved);
	}

	@Override
	public void deleteById(Integer id) {
		tipoPagoRepository.deleteById(id);

	}

	@Override
	public List<Coeficiente> findAllCoeficientes() {
		return coeficienteRepository.findAll().stream().map(coeficienteMapper::toModel) // ahora sí, es una instancia
				.collect(Collectors.toList());
	}

	@Override
	public List<TipoPago> findByRequiereCoeficienteTrue() {
		return tipoPagoRepository.findByRequiereCoeficienteTrue().stream().map(tipoPagoMapper::toModel)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<TipoPago> findByDescripcion(String descripcion) {
		return tipoPagoRepository.findByDescripcion(descripcion).map(tipoPagoMapper::toModel);
	}

}
