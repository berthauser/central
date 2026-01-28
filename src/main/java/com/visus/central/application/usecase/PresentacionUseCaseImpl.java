package com.visus.central.application.usecase;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.visus.central.application.event.DomainEvent;
import com.visus.central.domain.model.Presentacion;
import com.visus.central.domain.port.in.PresentacionUseCase;
import com.visus.central.domain.port.out.PresentacionRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class PresentacionUseCaseImpl implements PresentacionUseCase {
	
	@Autowired
	private PresentacionRepository presentacionRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;

	public List<Presentacion> listar() {
		return presentacionRepository.listar();
	}
	
	public Optional<Presentacion> buscarPorId(Integer id) {
		return presentacionRepository.buscarPorId(id);
	}

	public void guardar(Presentacion presentacion) {
	    boolean existe = presentacion.getId() != null && presentacionRepository.buscarPorId(presentacion.getId()).isPresent();
	    presentacionRepository.guardar(presentacion);
	    DomainEvent.Action accion = existe ? DomainEvent.Action.UPDATED : DomainEvent.Action.CREATED;
	    publisher.publishEvent(new DomainEvent<>(accion, presentacion));
	}

	@Override
	public void eliminar(Integer id) {
	    Optional<Presentacion> rubroOpt = presentacionRepository.buscarPorId(id);
	    if (rubroOpt.isPresent()) {
	    	presentacionRepository.eliminar(id);
	        publisher.publishEvent(new DomainEvent<>(DomainEvent.Action.DELETED, rubroOpt.get()));
	    }
	}

}
