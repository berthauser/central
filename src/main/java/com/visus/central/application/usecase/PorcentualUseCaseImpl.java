package com.visus.central.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.visus.central.domain.model.Porcentual;
import com.visus.central.domain.port.in.PorcentualUseCase;
import com.visus.central.domain.port.out.PorcentualRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PorcentualUseCaseImpl implements PorcentualUseCase {

    private final PorcentualRepository repository;

    public PorcentualUseCaseImpl(PorcentualRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Porcentual> findAll() {
        return repository.findAll();
    }

    @Override
    public Porcentual findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Porcentual save(Porcentual model) {
        return repository.save(model);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
