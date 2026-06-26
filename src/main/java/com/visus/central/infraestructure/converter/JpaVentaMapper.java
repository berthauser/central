package com.visus.central.infraestructure.converter;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Venta;
import com.visus.central.infraestructure.persistence.entity.JpaVentaEntity;

@Component
public class JpaVentaMapper {
	
	private final JpaComprobanteMapper comprobanteMapper;
    private final JpaClienteMapper clienteMapper;
    private final JpaVendedorMapper vendedorMapper;

	public JpaVentaMapper(JpaComprobanteMapper comprobanteMapper, JpaClienteMapper clienteMapper,
			JpaVendedorMapper vendedorMapper) {
		this.comprobanteMapper = comprobanteMapper;
		this.clienteMapper = clienteMapper;
		this.vendedorMapper = vendedorMapper;
    }
    
    
    public JpaVentaEntity toEntity(Venta domain) {
        if (domain == null) return null;
        JpaVentaEntity entity = new JpaVentaEntity();
        entity.setId(domain.getId());
        entity.setComprobante(comprobanteMapper.toEntity(domain.getComprobante()));
        entity.setCliente(clienteMapper.toEntity(domain.getCliente()));
        entity.setVendedor(vendedorMapper.toEntity(domain.getVendedor()));
        entity.setFechaVenta(domain.getFechaVenta());
        entity.setNumeroComprobante(domain.getNumeroComprobante());
        entity.setGrupoFamiliarId(domain.getGrupoFamiliarId());  // Solo ID
        entity.setEsBonificado(domain.getEsBonificado());
        entity.setBonificacion(domain.getBonificacion());
        entity.setObservaciones(domain.getObservaciones());
        // Convertir enum de dominio a enum de la entidad JPA
//        entity.setEstado(JpaVentaEntity.Estado.valueOf(domain.getEstado().name()));
        entity.setEstado(domain.getEstado());
        
		/*
		 * Mapear items 
		 * Necesitamos pasar la entidad venta a cada item; como aún
		 * no está persistida, // se la pasamos en el momento de guardar (ver adaptador)
		 * Esto se hará en el adaptador, no aquí.
		 */
        if (domain.getItems() != null) {
			/*
			 * entity.setItems(domain.getItems().stream() .map(item ->
			 * itemMapper.toEntity(item, entity)) .collect(Collectors.toList()));
			 */
       
        }
        return entity;
    }
    
    public Venta toModel(JpaVentaEntity entity) {
        if (entity == null) return null;
        Venta domain = new Venta();
        domain.setId(entity.getId());
        domain.setComprobante(comprobanteMapper.toModel(entity.getComprobante()));
        domain.setCliente(clienteMapper.toModel(entity.getCliente()));
        domain.setVendedor(vendedorMapper.toModel(entity.getVendedor()));
        domain.setFechaVenta(entity.getFechaVenta());
        domain.setNumeroComprobante(entity.getNumeroComprobante());
        domain.setGrupoFamiliarId(entity.getGrupoFamiliarId());
        domain.setEsBonificado(entity.getEsBonificado());
        domain.setBonificacion(entity.getBonificacion());
        domain.setObservaciones(entity.getObservaciones());
        domain.setEstado(entity.getEstado());   // entity.getEstado() ya es EstadoVenta
        
        /* Mapear Items
         * La vista Vaadin necesita mostrar la grilla de ítems, y el caso de uso 
         * calcula totales basados en la lista. Sin mapear los ítems, la venta no tendría detalle.
         * */
//        if (entity.getItems() != null) {
//            List<Item> itemsDomain = entity.getItems().stream()
//                .map(itemEntity -> itemMapper.toDomain(itemEntity))
//                .collect(Collectors.toList());
//            domain.setItems(itemsDomain);
//            // Opcional: establecer la relación bidireccional (item -> venta)
//            // itemsDomain.forEach(item -> item.setVenta(domain));
//        }
        return domain;
    }

}
