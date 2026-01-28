package com.visus.central.application.usecase;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.visus.central.domain.model.Linea;
import com.visus.central.domain.port.in.LineaUseCase;
import com.visus.central.domain.port.out.LineaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LineaUseCaseImpl implements LineaUseCase {
	
	private final LineaRepository lineaRepository;

	public LineaUseCaseImpl(LineaRepository lineaRepository) {
		this.lineaRepository = lineaRepository;
	}

    @Override
    public List<Linea> listar() {
        return lineaRepository.listar();
    }

    @Override
    public Optional<Linea> buscarPorId(Integer id) {
        return lineaRepository.buscarPorId(id);
    }

    @Override
    public void guardar(Linea linea) {
    	lineaRepository.guardar(linea);
    }

    @Override
    public void eliminar(Integer id) {
    	lineaRepository.eliminar(id);
    }

    @Override
    public boolean tieneRubrosAsociados(Integer idRubro) {
        return lineaRepository.existePorRubroId(idRubro);
    }

    @Override
    public List<Linea> findByRubroId(Integer idRubro) {
    	System.out.println("LineaUseCaseImpl.findByRubroId() - Rubro ID: " + idRubro);
    	return lineaRepository.findByRubroId(idRubro);
    }

}
