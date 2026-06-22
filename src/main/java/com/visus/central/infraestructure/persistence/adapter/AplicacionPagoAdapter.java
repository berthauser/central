package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.AplicacionPago;
import com.visus.central.domain.port.out.AplicacionPagoRepository;
import com.visus.central.infraestructure.converter.AplicacionPagoMapper;
import com.visus.central.infraestructure.persistence.entity.AplicacionPagoEntity;
import com.visus.central.infraestructure.persistence.entity.PagoEntity;
import com.visus.central.infraestructure.persistence.repository.JpaAplicacionPagoRepository;
import com.visus.central.infraestructure.persistence.repository.JpaPagoRepository;

import jakarta.transaction.Transactional;

@Component
public class AplicacionPagoAdapter implements AplicacionPagoRepository {

	private final JpaAplicacionPagoRepository jpaRepository;
    private final JpaPagoRepository pagoRepository;
    private final AplicacionPagoMapper mapper;

    public AplicacionPagoAdapter(JpaAplicacionPagoRepository jpaRepository,
                                 JpaPagoRepository pagoRepository,
                                 AplicacionPagoMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.pagoRepository = pagoRepository;
        this.mapper = mapper;
    }
    
    @Override
    public AplicacionPago save(AplicacionPago aplicacion) {
        // Obtener la entidad del pago asociado
        PagoEntity pagoEntity = pagoRepository.findById(aplicacion.getIdPago())
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + aplicacion.getIdPago()));

        // Usar el mapper que recibe el pago (sin necesidad de cuenta)
        AplicacionPagoEntity entity = mapper.toEntity(aplicacion, pagoEntity);
        AplicacionPagoEntity saved = jpaRepository.save(entity);
        return mapper.toModel(saved);
    }

    @Override
    public Optional<AplicacionPago> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<AplicacionPago> findByPlanPagoId(Long idPlanPago) {
        return jpaRepository.findByIdPlanPago(idPlanPago).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<AplicacionPago> findByVentaId(Integer idVenta) {
        return jpaRepository.findByVentaId(idVenta).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

}
