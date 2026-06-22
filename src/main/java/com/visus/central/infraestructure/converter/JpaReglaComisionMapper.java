package com.visus.central.infraestructure.converter;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.ReglaComision;
import com.visus.central.infraestructure.persistence.entity.JpaReglaComisionEntity;

@Component
public class JpaReglaComisionMapper {

	private final JpaVendedorMapper vendedorMapper;
	private final JpaTramoComisionMapper tramoMapper;

	public JpaReglaComisionMapper(JpaVendedorMapper vendedorMapper, JpaTramoComisionMapper tramoMapper) {
		this.vendedorMapper = vendedorMapper;
		this.tramoMapper = tramoMapper;
	}

	public JpaReglaComisionEntity toEntity(ReglaComision domain) {
		if (domain == null)
			return null;

		JpaReglaComisionEntity entity = new JpaReglaComisionEntity();
		entity.setId(domain.getId());
		entity.setVendedor(vendedorMapper.toEntity(domain.getVendedor()));
		entity.setNombre(domain.getNombre());
		entity.setTipoBaseComisionable(domain.getTipoBaseComisionable());
		entity.setTipoCalculo(domain.getTipoCalculo());
		entity.setValorCalculo(domain.getValorCalculo());
		entity.setTipoEvento(domain.getTipoEvento());
		entity.setIncluirDescuentos(domain.getIncluirDescuentos());
		entity.setAjustarDevoluciones(domain.getAjustarDevoluciones());
		entity.setVentanaAjusteDias(domain.getVentanaAjusteDias());
		entity.setActivo(domain.getActivo());
		entity.setFechaCreacion(domain.getFechaCreacion());

		if (domain.getTramos() != null) {
			entity.setTramos(domain.getTramos().stream().map(t -> {
				var te = tramoMapper.toEntity(t);
				te.setRegla(entity);
				return te;
			}).collect(Collectors.toList()));
		}
		return entity;
	}

	public ReglaComision toDomain(JpaReglaComisionEntity entity) {
		if (entity == null)
			return null;

		ReglaComision domain = new ReglaComision();
		domain.setId(entity.getId());
		domain.setVendedor(vendedorMapper.toModel(entity.getVendedor()));
		domain.setNombre(entity.getNombre());
		domain.setTipoBaseComisionable(entity.getTipoBaseComisionable());
		domain.setTipoCalculo(entity.getTipoCalculo());
		domain.setValorCalculo(entity.getValorCalculo());
		domain.setTipoEvento(entity.getTipoEvento());
		domain.setIncluirDescuentos(entity.getIncluirDescuentos());
		domain.setAjustarDevoluciones(entity.getAjustarDevoluciones());
		domain.setVentanaAjusteDias(entity.getVentanaAjusteDias());
		domain.setActivo(entity.getActivo());
		domain.setFechaCreacion(entity.getFechaCreacion());

		if (entity.getTramos() != null) {
			domain.setTramos(entity.getTramos().stream().map(te -> {
				var t = tramoMapper.toDomain(te);
				t.setRegla(domain);
				return t;
			}).collect(Collectors.toList()));
		}
		return domain;
	}
}
