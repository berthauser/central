package com.visus.central.infraestructure.persistence.adapter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.visus.central.domain.model.Caja;
import com.visus.central.domain.model.MovimientoCaja;
import com.visus.central.domain.port.out.MovimientoCajaRepository;
import com.visus.central.infraestructure.converter.MovimientoCajaMapper;
import com.visus.central.infraestructure.persistence.entity.MovimientoCajaEntity;
import com.visus.central.infraestructure.persistence.repository.JpaMovimientoCajaRepository;

@Repository
public class MovimientoCajaAdapter implements MovimientoCajaRepository {
	
	Logger logger = LoggerFactory.getLogger(MovimientoCajaAdapter.class);
	
	@Autowired
    private JpaMovimientoCajaRepository jpaRepository;

    @Autowired
    private MovimientoCajaMapper mapper;

    @Override
    public MovimientoCaja save(MovimientoCaja movimiento) {
    	logger.debug("Guardando movimiento con ID: {}", movimiento.getId()); // debe ser null
        MovimientoCajaEntity entity = mapper.toEntity(movimiento);
        MovimientoCajaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public List<MovimientoCaja> findByCaja(Caja caja) {
        List<MovimientoCajaEntity> entities = jpaRepository.findByCaja_Idcaja(caja.getId());
        return mapper.toDomainList(entities);
    }

    @Override
    public List<MovimientoCaja> findByCajaAndFecha(Caja caja, LocalDate fecha) {
        List<MovimientoCajaEntity> entities = jpaRepository.findByCaja_IdcajaAndFecha(caja.getId(), fecha);
        return mapper.toDomainList(entities);
    }
    
    @Override
    public Optional<MovimientoCaja> findById(Integer id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
    }

}
