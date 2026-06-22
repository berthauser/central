package com.visus.central.infraestructure.persistence.adapter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Coeficiente;
import com.visus.central.domain.model.TipoPago;
import com.visus.central.domain.port.out.CoeficienteRepository;
import com.visus.central.infraestructure.converter.JpaCoeficienteMapper;
import com.visus.central.infraestructure.converter.TipoPagoMapper;
import com.visus.central.infraestructure.persistence.entity.CoeficienteEntity;
import com.visus.central.infraestructure.persistence.repository.JpaCoeficienteRepository;
import com.visus.central.infraestructure.persistence.repository.JpaTipoPagoRepository;

import jakarta.transaction.Transactional;

@Component
public class CoeficienteAdapter implements CoeficienteRepository {

	private final JpaCoeficienteRepository coeficienteRepository;
	private final JpaCoeficienteMapper coeficienteMapper;
	private final JpaTipoPagoRepository tipoPagoRepository;
	private final TipoPagoMapper tipoPagoMapper;

	public CoeficienteAdapter(JpaCoeficienteRepository coeficienteRepository, JpaTipoPagoRepository tipoPagoRepository,
			TipoPagoMapper tipoPagoMapper, JpaCoeficienteMapper coeficienteMapper) {
		this.coeficienteRepository = coeficienteRepository;
		this.tipoPagoRepository = tipoPagoRepository;
		this.tipoPagoMapper = tipoPagoMapper;
		this.coeficienteMapper = coeficienteMapper;
	}

	@Override
	public List<Coeficiente> findAll() {
		return coeficienteRepository.findAll().stream().map(coeficienteMapper::toModel).collect(Collectors.toList());
	}

	@Override
	public List<TipoPago> findAllTiposPago() {
		return tipoPagoRepository.findAll().stream().map(tipoPagoMapper::toModel).collect(Collectors.toList());
	}

	@Override
	public Optional<Coeficiente> findById(Integer id) {
		return coeficienteRepository.findById(id).map(coeficienteMapper::toModel);
	}

	@Override
	@Transactional
	public Coeficiente save(Coeficiente coeficiente) {
		CoeficienteEntity entity = coeficienteMapper.toEntity(coeficiente);
		CoeficienteEntity saved = coeficienteRepository.save(entity);
		return coeficienteMapper.toModel(saved);
	}

	@Override
	public void deleteById(Integer id) {
		coeficienteRepository.deleteById(id);
	}

	@Override
	public List<Coeficiente> findByTipoPagoId(Integer idTipoPago) {
		return coeficienteRepository.findByTipoPagoId(idTipoPago).stream()
	            .map(coeficienteMapper::toModel)
	            .collect(Collectors.toList());
	}
	
	@Override
	public Optional<Coeficiente> findByTipoPagoIdAndCoeficiente(Integer idTipoPago, BigDecimal coeficiente) {
	    return coeficienteRepository.findByTipoPagoIdAndCoeficiente(idTipoPago, coeficiente)
	            .map(coeficienteMapper::toModel);
	}

}
