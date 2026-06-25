package com.visus.central.infraestructure.converter;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Devolucion;
import com.visus.central.domain.model.TipoOperacionDevolucion;
import com.visus.central.infraestructure.persistence.entity.JpaDevolucionEntity;

@Component
public class JpaDevolucionMapper {

    public JpaDevolucionEntity toEntity(Devolucion domain) {
        if (domain == null) return null;
        JpaDevolucionEntity entity = new JpaDevolucionEntity();
        entity.setId(domain.getId());
        entity.setIdVenta(domain.getIdVenta());
        entity.setIdArticulo(domain.getIdArticulo());
        entity.setCantidad(domain.getCantidad());
        entity.setMontoDevuelto(domain.getMontoDevuelto());
        entity.setTipoOperacion(domain.getTipoOperacion() != null ? domain.getTipoOperacion().name() : null);
        entity.setIdNotaCredito(domain.getIdNotaCredito());
        entity.setIdPago(domain.getIdPago());
        entity.setIdMovimientoCaja(domain.getIdMovimientoCaja());
        entity.setFecha(domain.getFecha());
        entity.setMalEstado(domain.isMalEstado());
        entity.setObservaciones(domain.getObservaciones());
        entity.setIdUsuario(domain.getIdUsuario());
        return entity;
    }

    public Devolucion toDomain(JpaDevolucionEntity entity) {
        if (entity == null) return null;
        Devolucion domain = new Devolucion();
        domain.setId(entity.getId());
        domain.setIdVenta(entity.getIdVenta());
        domain.setIdArticulo(entity.getIdArticulo());
        domain.setCantidad(entity.getCantidad());
        domain.setMontoDevuelto(entity.getMontoDevuelto());
        if (entity.getTipoOperacion() != null) {
            domain.setTipoOperacion(TipoOperacionDevolucion.valueOf(entity.getTipoOperacion()));
        }
        domain.setIdNotaCredito(entity.getIdNotaCredito());
        domain.setIdPago(entity.getIdPago());
        domain.setIdMovimientoCaja(entity.getIdMovimientoCaja());
        domain.setFecha(entity.getFecha());
        domain.setMalEstado(entity.isMalEstado());
        domain.setObservaciones(entity.getObservaciones());
        domain.setIdUsuario(entity.getIdUsuario());
        return domain;
    }
}
