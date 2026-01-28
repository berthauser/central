package com.visus.central.application.usecase;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.visus.central.application.event.DomainEvent;
import com.visus.central.domain.model.Rubro;
import com.visus.central.domain.port.in.RubroUseCase;
import com.visus.central.domain.port.out.RubroRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RubroUseCaseImpl implements RubroUseCase {
	
	@Autowired
	private RubroRepository rubroRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;

	public List<Rubro> listar() {
		return rubroRepository.listar();
	}
	
	public Optional<Rubro> buscarPorId(Integer id) {
		return rubroRepository.buscarPorId(id);
	}

	public void guardar(Rubro rubro) {
	    boolean existe = rubro.getId() != null && rubroRepository.buscarPorId(rubro.getId()).isPresent();
	    rubroRepository.guardar(rubro);
	    DomainEvent.Action accion = existe ? DomainEvent.Action.UPDATED : DomainEvent.Action.CREATED;
	    publisher.publishEvent(new DomainEvent<>(accion, rubro));
	}

	@Override
	public void eliminar(Integer id) {
	    Optional<Rubro> rubroOpt = rubroRepository.buscarPorId(id);
	    if (rubroOpt.isPresent()) {
	    	rubroRepository.eliminar(id);
	        publisher.publishEvent(new DomainEvent<>(DomainEvent.Action.DELETED, rubroOpt.get()));
	    }
	}

	@Override
	public boolean tieneLineasAsociadas(Integer idRubro) {
		return false;
	}

}
