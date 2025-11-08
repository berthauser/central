package com.visus.central.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.visus.central.domain.model.Coeficiente;
import com.visus.central.domain.port.in.CoeficienteUseCase;
import com.visus.central.domain.port.out.CoeficienteRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CoeficienteUseCaseImpl implements CoeficienteUseCase {
	
	private final CoeficienteRepository coeficienteRepository;

    public CoeficienteUseCaseImpl(CoeficienteRepository coeficienteRepository) {
        this.coeficienteRepository = coeficienteRepository;
    }

    @Override
    public List<Coeficiente> findAll() {
        return coeficienteRepository.findAll();
    }

    @Override
    public Coeficiente findById(Integer id) {
        return coeficienteRepository.findById(id)
            .orElse(null);
    }

    @Override
    public Coeficiente save(Coeficiente coeficiente) {
        return coeficienteRepository.save(coeficiente);
    }

    @Override
    public void deleteById(Integer id) {
        coeficienteRepository.deleteById(id);
    }

}
