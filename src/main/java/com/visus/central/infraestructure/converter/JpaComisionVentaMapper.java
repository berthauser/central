package com.visus.central.infraestructure.converter;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.ComisionVenta;
import com.visus.central.infraestructure.persistence.entity.JpaComisionVentaEntity;

@Component
public class JpaComisionVentaMapper {

	private final JpaVendedorMapper vendedorMapper;
	private final JpaVentaMapper ventaMapper;

	public JpaComisionVentaMapper(JpaVendedorMapper vendedorMapper, JpaVentaMapper ventaMapper) {
		this.vendedorMapper = vendedorMapper;
		this.ventaMapper = ventaMapper;
	}

	public JpaComisionVentaEntity toEntity(ComisionVenta domain) {
		if (domain == null)
			return null;

		JpaComisionVentaEntity entity = new JpaComisionVentaEntity();
		entity.setId(domain.getId());
		entity.setVendedor(vendedorMapper.toEntity(domain.getVendedor()));
		entity.setVenta(ventaMapper.toEntity(domain.getVenta()));
		entity.setIdPago(domain.getIdPago());
		entity.setBaseComisionable(domain.getBaseComisionable());
		entity.setPorcentaje(domain.getPorcentaje());
		entity.setComisionBruta(domain.getComisionBruta());
		entity.setAjustes(domain.getAjustes());
		entity.setComisionFinal(domain.getComisionFinal());
		entity.setFechaCalculo(domain.getFechaCalculo());
		entity.setEstado(domain.getEstado());
		entity.setObservaciones(domain.getObservaciones());
		return entity;
	}

	public ComisionVenta toDomain(JpaComisionVentaEntity entity) {
		if (entity == null)
			return null;

		ComisionVenta domain = new ComisionVenta();
		domain.setId(entity.getId());
		domain.setVendedor(vendedorMapper.toModel(entity.getVendedor()));
		domain.setVenta(ventaMapper.toModel(entity.getVenta()));
		domain.setIdPago(entity.getIdPago());
		domain.setBaseComisionable(entity.getBaseComisionable());
		domain.setPorcentaje(entity.getPorcentaje());
		domain.setComisionBruta(entity.getComisionBruta());
		domain.setAjustes(entity.getAjustes());
		domain.setComisionFinal(entity.getComisionFinal());
		domain.setFechaCalculo(entity.getFechaCalculo());
		domain.setEstado(entity.getEstado());
		domain.setObservaciones(entity.getObservaciones());
		return domain;
	}
}
