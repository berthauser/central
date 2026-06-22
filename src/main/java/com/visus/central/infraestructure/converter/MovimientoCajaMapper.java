package com.visus.central.infraestructure.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.visus.central.domain.model.MovimientoCaja;
import com.visus.central.infraestructure.persistence.entity.MovimientoCajaEntity;

@Component
public class MovimientoCajaMapper {
	
	@Autowired
    private CajaMapper cajaMapper; // para convertir la caja asociada
	
	public MovimientoCajaEntity toEntity(MovimientoCaja domain) {
        if (domain == null) return null;
        MovimientoCajaEntity entity = new MovimientoCajaEntity();
        entity.setIdmovimiento(domain.getId());
        if (domain.getCaja() != null)
            entity.setCaja(cajaMapper.toEntity(domain.getCaja()));
        entity.setFecha(domain.getFecha());
        entity.setHora(domain.getHora());
        entity.setNumeroComprobante(domain.getNumeroComprobante());
        entity.setDebe(domain.getDebe());
        entity.setHaber(domain.getHaber());
        entity.setDescripcion(domain.getDescripcion());
        entity.setOrigen(domain.getOrigen());
        // Relaciones opcionales (las dejamos nulas por ahora)
        // entity.setComprobante(...);
        // entity.setVenta(...);
        // entity.setPlanPago(...);
        // entity.setTipoPago(...);
        return entity;
    }
	
	public MovimientoCaja toDomain(MovimientoCajaEntity entity) {
        if (entity == null) return null;
        MovimientoCaja domain = new MovimientoCaja();
        domain.setId(entity.getIdmovimiento());
        if (entity.getCaja() != null)
            domain.setCaja(cajaMapper.toDomain(entity.getCaja()));
        domain.setFecha(entity.getFecha());
        domain.setHora(entity.getHora());
        domain.setNumeroComprobante(entity.getNumeroComprobante());
        domain.setDebe(entity.getDebe());
        domain.setHaber(entity.getHaber());
        domain.setDescripcion(entity.getDescripcion());
        domain.setOrigen(entity.getOrigen());
        // Relaciones opcionales
        return domain;
    }

    public List<MovimientoCajaEntity> toEntityList(List<MovimientoCaja> domainList) {
        if (domainList == null) return null;
        return domainList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    public List<MovimientoCaja> toDomainList(List<MovimientoCajaEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream().map(this::toDomain).collect(Collectors.toList());
    }

}
