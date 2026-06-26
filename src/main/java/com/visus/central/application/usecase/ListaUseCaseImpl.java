package com.visus.central.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.visus.central.domain.model.Lista;
import com.visus.central.domain.port.in.ListaUseCase;
import com.visus.central.domain.port.out.ListaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ListaUseCaseImpl implements ListaUseCase {

    private final ListaRepository repository;

    public ListaUseCaseImpl(ListaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Lista> findAll() {
        return repository.findAll();
    }

    @Override
    public Lista findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Lista save(Lista model) {
        return repository.save(model);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
