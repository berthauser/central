package com.visus.central.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.visus.central.domain.model.Concepto;
import com.visus.central.domain.port.in.ConceptoUseCase;
import com.visus.central.domain.port.out.ConceptoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ConceptoUseCaseImpl implements ConceptoUseCase {
	
	private final ConceptoRepository conceptoRepository;

    public ConceptoUseCaseImpl(ConceptoRepository conceptoRepository) {
        this.conceptoRepository = conceptoRepository;
    }

    @Override
    public List<Concepto> findAll() {
        return conceptoRepository.findAll();
    }

    @Override
    public Concepto findById(Integer id) {
        return conceptoRepository.findById(id)
            .orElse(null); // 👈 Devuelve null si no encuentra, no Optional
    }

    @Override
    public Concepto save(Concepto concepto) {
        return conceptoRepository.save(concepto);
    }

    @Override
    public void deleteById(Integer id) {
        conceptoRepository.deleteById(id);
    }
}
