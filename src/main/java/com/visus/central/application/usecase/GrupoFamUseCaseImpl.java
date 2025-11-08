package com.visus.central.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.visus.central.domain.model.GrupoFam;
import com.visus.central.domain.port.in.GrupoFamUseCase;
import com.visus.central.domain.port.out.GrupoFamRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class GrupoFamUseCaseImpl implements GrupoFamUseCase {

    private final GrupoFamRepository grupoFamRepository;

    public GrupoFamUseCaseImpl(GrupoFamRepository grupoFamRepository) {
        this.grupoFamRepository = grupoFamRepository;
    }

    @Override
    public List<GrupoFam> findAll() {
        return grupoFamRepository.findAll();
    }

    @Override
    public GrupoFam findById(Integer id) {
        return grupoFamRepository.findById(id).orElse(null);
    }

    @Override
    public GrupoFam save(GrupoFam grupoFam) {
        return grupoFamRepository.save(grupoFam);
    }

    @Override
    public void deleteById(Integer id) {
        grupoFamRepository.deleteById(id);
    }

    @Override
    public List<GrupoFam> findByNombreContainingIgnoreCase(String nombre) {
        return grupoFamRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public List<GrupoFam> findByClienteId(Integer idCliente) {
        return grupoFamRepository.findByClienteId(idCliente);
    }

    @Override
    public boolean existsByNumero(Integer numero) {
        return grupoFamRepository.existsByNumero(numero);
    }

}
