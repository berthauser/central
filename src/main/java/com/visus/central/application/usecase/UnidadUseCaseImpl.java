package com.visus.central.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.visus.central.application.port.in.UnidadUseCase;
import com.visus.central.domain.model.Unidad;
import com.visus.central.domain.port.out.UnidadRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UnidadUseCaseImpl implements UnidadUseCase {

	private final UnidadRepository unidadRepository;

	public UnidadUseCaseImpl(UnidadRepository unidadRepository) {
		this.unidadRepository = unidadRepository;
	}

	@Override
	public List<Unidad> buscarPorPresentacion(Integer idPresentacion) {
		return unidadRepository.findByPresentacionId(idPresentacion);
	}
}
