package com.visus.central.infraestructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.EstadoVenta;
import com.visus.central.domain.model.Item;
import com.visus.central.domain.model.Venta;
import com.visus.central.domain.port.out.VentaRepository;
import com.visus.central.infraestructure.converter.JpaItemMapper;
import com.visus.central.infraestructure.converter.JpaVentaMapper;
import com.visus.central.infraestructure.persistence.entity.JpaItemEntity;
import com.visus.central.infraestructure.persistence.entity.JpaVentaEntity;
import com.visus.central.infraestructure.persistence.repository.JpaVentaRepository;

@Component
public class PostgresVentaAdapter implements VentaRepository {
	
	private final JpaVentaRepository jpaRepository;
    private final JpaVentaMapper ventaMapper;
    private final JpaItemMapper itemMapper;

    public PostgresVentaAdapter(JpaVentaRepository jpaRepository, JpaVentaMapper ventaMapper, JpaItemMapper itemMapper) {
        this.jpaRepository = jpaRepository;
        this.ventaMapper = ventaMapper;
        this.itemMapper = itemMapper;
    }

    @Override
    public Venta save(Venta venta) {
        // Convertir venta a entidad (sin items primero)
        JpaVentaEntity entity = ventaMapper.toEntity(venta);
        // Limpiar items por si acaso
        entity.getItems().clear();
        // Agregar items con la relación establecida
        for (Item item : venta.getItems()) {
            JpaItemEntity itemEntity = itemMapper.toEntity(item, entity);
            entity.getItems().add(itemEntity);
        }
        JpaVentaEntity saved = jpaRepository.save(entity);
        return ventaMapper.toModel(saved);
    }

	@Override
	public Optional<Venta> findById(Integer idVenta) {
		return jpaRepository.findById(idVenta).map(ventaMapper::toModel);
	}
	
	@Override
    public int actualizarEstadoVenta(Integer idVenta, EstadoVenta estado) {
	    return jpaRepository.actualizarEstadoVenta(idVenta, estado);
    }

	
	

}
