package com.visus.central.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.visus.central.domain.model.Comprobante;
import com.visus.central.domain.port.in.ComprobanteUseCase;
import com.visus.central.domain.port.out.ComprobanteRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ComprobanteUseCaseImpl implements ComprobanteUseCase {

	private final ComprobanteRepository comprobanteRepository;

    public ComprobanteUseCaseImpl(ComprobanteRepository comprobanteRepository) {
        this.comprobanteRepository = comprobanteRepository;
    }

    @Override
    public List<Comprobante> findAll() {
        return comprobanteRepository.findAll();
    }

    @Override
    public Comprobante findById(Integer id) {
        return comprobanteRepository.findById(id)
            .orElse(null);
    }

    @Override
    public Comprobante save(Comprobante comprobante) {
        return comprobanteRepository.save(comprobante);
    }

    @Override
    public void deleteById(Integer id) {
        comprobanteRepository.deleteById(id);
    }
	
}
