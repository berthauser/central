package com.visus.central.application.usecase;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.visus.central.domain.model.Medida;
import com.visus.central.domain.port.in.MedidaUseCase;
import com.visus.central.domain.port.out.MedidaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MedidaUseCaseImpl implements MedidaUseCase {
	
	private final MedidaRepository medidaRepository;

	public MedidaUseCaseImpl(MedidaRepository medidaRepository) {
		this.medidaRepository = medidaRepository;
	}
	
	@Override
    public List<Medida> listar() {
        return medidaRepository.listar();
    }

    @Override
    public Optional<Medida> buscarPorId(Integer id) {
        return medidaRepository.buscarPorId(id);
    }

    @Override
    public void guardar(Medida medida) {
    	medidaRepository.guardar(medida);
    }

    @Override
    public void eliminar(Integer id) {
    	medidaRepository.eliminar(id);
    }

}
