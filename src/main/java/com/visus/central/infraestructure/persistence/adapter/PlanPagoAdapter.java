package com.visus.central.infraestructure.persistence.adapter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.EstadoPlanPago;
import com.visus.central.domain.model.PlanPago;
import com.visus.central.domain.port.out.PlanPagoRepository;
import com.visus.central.infraestructure.converter.PlanPagoMapper;
import com.visus.central.infraestructure.persistence.entity.PlanPagoEntity;
import com.visus.central.infraestructure.persistence.repository.JpaPlanPagoRepository;

import jakarta.transaction.Transactional;

@Component
public class PlanPagoAdapter implements PlanPagoRepository {
	
	private final JpaPlanPagoRepository planPagoRepository;
    private final PlanPagoMapper planPagoMapper;
	
    
    public PlanPagoAdapter(JpaPlanPagoRepository planPagoRepository, PlanPagoMapper planPagoMapper) {
		this.planPagoRepository = planPagoRepository;
		this.planPagoMapper = planPagoMapper;
	}
    
    @Override
    public PlanPago save(PlanPago planPago) {
        PlanPagoEntity entity = planPagoMapper.toEntity(planPago);
        PlanPagoEntity saved = planPagoRepository.save(entity);
        return planPagoMapper.toModel(saved);
    }

    @Override
    public Optional<PlanPago> findById(Long id) {
        return planPagoRepository.findById(id).map(planPagoMapper::toModel);
    }

    @Override
    public void deleteById(Long id) {
    	planPagoRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return planPagoRepository.existsById(id);
    }

    @Override
    @Transactional
    public int actualizarEstadoPlanPago(Long id, EstadoPlanPago estado) {
        return planPagoRepository.actualizarEstadoPlanPago(id, estado);
    }
    
    @Override
    public List<PlanPago> findByVentaId(Integer idVenta) {
        return planPagoRepository.findByIdVenta(idVenta).stream()
                .map(planPagoMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlanPago> findByPendienteByVentaId(Integer idVenta) {
        return planPagoRepository.findPendientesByVentaId(idVenta).stream()
                .map(planPagoMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlanPago> findVencidos(LocalDate fecha) {
        return planPagoRepository.findVencidos(fecha).stream()
                .map(planPagoMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlanPago> findPorVencer(LocalDate fechaInicio, LocalDate fechaFin) {
        return planPagoRepository.findPorVencer(fechaInicio, fechaFin).stream()
                .map(planPagoMapper::toModel)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PlanPago> findByEstado(EstadoPlanPago estado) {
        return planPagoRepository.findByEstado(estado).stream()
                .map(planPagoMapper::toModel)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PlanPago> findByEstadoIn(List<EstadoPlanPago> estados) {
        return planPagoRepository.findByEstadoIn(estados).stream()
                .map(planPagoMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlanPago> findPendientesByClienteId(Integer idCliente) {
        return planPagoRepository.findPendientesByClienteId(idCliente).stream()
                .map(planPagoMapper::toModel)
                .collect(Collectors.toList());
    }


}
