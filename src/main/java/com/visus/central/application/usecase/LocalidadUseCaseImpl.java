package com.visus.central.application.usecase;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.visus.central.domain.model.Localidad;
import com.visus.central.domain.port.in.LocalidadUseCase;
import com.visus.central.domain.port.out.LocalidadRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LocalidadUseCaseImpl implements LocalidadUseCase {
	
	private final LocalidadRepository locRepo;

    public LocalidadUseCaseImpl(LocalidadRepository locRepo) {
        this.locRepo = locRepo;
    }
    
    @Override
    public List<Localidad> listar() {
        return locRepo.listar();
    }

    @Override
    public Optional<Localidad> buscarPorId(Integer id) {
        return locRepo.buscarPorId(id);
    }

    @Override
    public void guardar(Localidad localidad) {
    	locRepo.guardar(localidad);
    }

    @Override
    public void eliminar(Integer id) {
    	locRepo.eliminar(id);
    }

    @Override
    public boolean tieneLocalidadesAsociadas(Integer idDepartamento) {
        return locRepo.existePorDepartamentoId(idDepartamento);
    }

}
