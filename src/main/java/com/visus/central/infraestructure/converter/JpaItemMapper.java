package com.visus.central.infraestructure.converter;


/*
 * itemEntity.setVenta(ventaEntity);
 * 
 * JPA automáticamente extrae el id de ventaEntity y lo asigna a la columna idventa en la tabla items. No necesito un campo idVenta en la entidad. Lo mismo con articulo.
 * 
 * */

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Articulo;
import com.visus.central.domain.model.Item;
import com.visus.central.infraestructure.persistence.entity.JpaItemEntity;
import com.visus.central.infraestructure.persistence.entity.JpaVentaEntity;

@Component
public class JpaItemMapper {
	
	private final JpaArticuloMapper articuloMapper;

    public JpaItemMapper(JpaArticuloMapper articuloMapper) {
        this.articuloMapper = articuloMapper;
    }
	
    
 // Convierte dominio a entidad JPA, vinculándola a una ventaEntity
    public JpaItemEntity toEntity(Item domain, JpaVentaEntity ventaEntity) {
        if (domain == null) return null;
        JpaItemEntity entity = new JpaItemEntity();
        entity.setIdItem(domain.getIdItem());
        entity.setVenta(ventaEntity);
        entity.setArticulo(articuloMapper.toEntity(domain.getArticulo()));
        entity.setCantidad(domain.getCantidad());
        entity.setPrecioUnitario(domain.getPrecioUnitario());  // BigDecimal directo
        entity.setDescuentoArticulo(domain.getDescuentoArticulo());
        entity.setSaldoArticulo(domain.getSaldoArticulo());
        return entity;
    }
	
	
	
	
 // Convierte entidad JPA a dominio (sin IDs redundantes)
    public Item toDomain(JpaItemEntity entity) {
        if (entity == null) return null;
        Articulo articulo = articuloMapper.toModel(entity.getArticulo());
        // La venta se puede dejar null (se asigna en VentaMapper si es necesario)
        return new Item(
            entity.getIdItem(),
            null,  // venta
            articulo,
            entity.getCantidad(),
            entity.getPrecioUnitario(),
            entity.getDescuentoArticulo(),
            entity.getSaldoArticulo()
        );
    }
	
	
	

}
