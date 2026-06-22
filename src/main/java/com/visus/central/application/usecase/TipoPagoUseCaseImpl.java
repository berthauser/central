package com.visus.central.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.visus.central.domain.model.Coeficiente;
import com.visus.central.domain.model.TipoPago;
import com.visus.central.domain.port.in.TipoPagoUseCase;
import com.visus.central.domain.port.out.TipoPagoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TipoPagoUseCaseImpl implements TipoPagoUseCase {

	private final TipoPagoRepository tipoPagoRepository;

	public TipoPagoUseCaseImpl(TipoPagoRepository tipoPagoRepository) {
		this.tipoPagoRepository = tipoPagoRepository;
	}

	@Override
	public List<TipoPago> findAll() {
		return tipoPagoRepository.findAll();
	}

	@Override
	public TipoPago findById(Integer id) {
		return tipoPagoRepository.findById(id).orElse(null);
	}

	@Override
	public TipoPago save(TipoPago model) {
		return tipoPagoRepository.save(model);
	}

	@Override
	public void deleteById(Integer id) {
		tipoPagoRepository.deleteById(id);

	}

	@Override
	public List<Coeficiente> findAllCoeficientes() {
		return tipoPagoRepository.findAllCoeficientes();
	}

}
