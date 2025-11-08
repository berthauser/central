package com.visus.central.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.visus.central.domain.model.Banco;
import com.visus.central.domain.port.in.BancoUseCase;
import com.visus.central.domain.port.out.BancoRepository;

import jakarta.transaction.Transactional;

@Service
public class BancoUseCaseImpl implements BancoUseCase {

    private final BancoRepository bancoRepository;

    public BancoUseCaseImpl(BancoRepository bancoRepository) {
        this.bancoRepository = bancoRepository;
    }

    @Override
    public List<Banco> findAll() {
        return bancoRepository.findAll();
    }

    @Override
    public Banco findById(Integer id) {
        return bancoRepository.findById(id)
            .orElse(null);
    }

    @Override
    @Transactional
    public Banco save(Banco banco) {
        return bancoRepository.save(banco);
    }
    
    @Override
    public void deleteById(Integer id) {
        bancoRepository.deleteById(id);
    }

}
