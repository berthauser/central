package com.visus.central.application.usecase;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.visus.central.application.event.DomainEvent;
import com.visus.central.domain.model.Departamento;
import com.visus.central.domain.port.in.DepartamentoUseCase;
import com.visus.central.domain.port.out.DepartamentoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DepartamentoUseCaseImpl implements DepartamentoUseCase {

	@Autowired
	private DepartamentoRepository depRepo;
	
	@Autowired
	private ApplicationEventPublisher publisher;

	public List<Departamento> listar() {
		return depRepo.listar();
	}
	
	public Optional<Departamento> buscarPorId(Integer id) {
		return depRepo.buscarPorId(id);
	}

	public void guardar(Departamento departamento) {
	    boolean existe = departamento.getId() != null && depRepo.buscarPorId(departamento.getId()).isPresent();
	    depRepo.guardar(departamento);
	    DomainEvent.Action accion = existe ? DomainEvent.Action.UPDATED : DomainEvent.Action.CREATED;
	    publisher.publishEvent(new DomainEvent<>(accion, departamento));
	}

	@Override
	public void eliminar(Integer id) {
	    Optional<Departamento> deptoOpt = depRepo.buscarPorId(id);
	    if (deptoOpt.isPresent()) {
	        depRepo.eliminar(id);
	        publisher.publishEvent(new DomainEvent<>(DomainEvent.Action.DELETED, deptoOpt.get()));
	    }
	}

	@Override
	public boolean tieneLocalidadesAsociadas(Integer idDepartamento) {
		return false;
	}

}