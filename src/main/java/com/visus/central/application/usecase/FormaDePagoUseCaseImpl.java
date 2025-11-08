package com.visus.central.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.visus.central.domain.model.Coeficiente;
import com.visus.central.domain.model.FormaDePago;
import com.visus.central.domain.port.in.FormaDePagoUseCase;
import com.visus.central.domain.port.out.FormaDePagoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FormaDePagoUseCaseImpl implements FormaDePagoUseCase {

	private final FormaDePagoRepository formaDePagoRepository;

    public FormaDePagoUseCaseImpl(FormaDePagoRepository formaDePagoRepository) {
        this.formaDePagoRepository = formaDePagoRepository;
    }

    @Override
    public List<FormaDePago> findAll() {
        return formaDePagoRepository.findAll();
    }

    @Override
    public FormaDePago findById(Integer id) {
        return formaDePagoRepository.findById(id)
            .orElse(null);
    }

    @Override
    public FormaDePago save(FormaDePago formaDePago) {
        return formaDePagoRepository.save(formaDePago);
    }

    @Override
    public void deleteById(Integer id) {
        formaDePagoRepository.deleteById(id);
    }
    
	// Nuevo método para obtener coeficientes
    public List<Coeficiente> findAllCoeficientes() {
        return formaDePagoRepository.findAllCoeficientes();
    }
}
