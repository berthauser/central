package com.visus.central.infraestructure.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Caja;
import com.visus.central.infraestructure.persistence.entity.CajaEntity;

@Component
public class CajaMapper {

	// Convertir de dominio a entidad JPA
	public CajaEntity toEntity(Caja domain) {
		if (domain == null) {
			return null;
		}

		CajaEntity entity = new CajaEntity();
		entity.setIdcaja(domain.getId());
		entity.setFechaApertura(domain.getFechaApertura());
		entity.setHoraApertura(domain.getHoraApertura());
		entity.setSaldoInicial(domain.getSaldoInicial());
		entity.setSaldoRealCierre(domain.getSaldoRealCierre());
		entity.setFechaCierre(domain.getFechaCierre());
		entity.setHoraCierre(domain.getHoraCierre());
		entity.setEstado(domain.getEstado() != null ? domain.getEstado() : null);
		entity.setIdusuarioApertura(domain.getIdUsuarioApertura());
		entity.setIdusuarioCierre(domain.getIdUsuarioCierre());
		entity.setObservaciones(domain.getObservaciones());

		return entity;
	}

	// Convertir de entidad JPA a dominio
	public Caja toDomain(CajaEntity entity) {
		if (entity == null) {
			return null;
		}

		Caja domain = new Caja();
		domain.setId(entity.getIdcaja());
		domain.setFechaApertura(entity.getFechaApertura());
		domain.setHoraApertura(entity.getHoraApertura());
		domain.setSaldoInicial(entity.getSaldoInicial());
		domain.setSaldoRealCierre(entity.getSaldoRealCierre());
		domain.setFechaCierre(entity.getFechaCierre());
		domain.setHoraCierre(entity.getHoraCierre());

		if (entity.getEstado() != null) {
			domain.setEstado(entity.getEstado()); // directo, ya viene en mayúscula
		}

		domain.setIdUsuarioApertura(entity.getIdusuarioApertura());
		domain.setIdUsuarioCierre(entity.getIdusuarioCierre());
		domain.setObservaciones(entity.getObservaciones());

		return domain;
	}

	// Convertir listas
	public List<CajaEntity> toEntityList(List<Caja> domainList) {
		if (domainList == null) {
			return null;
		}
		return domainList.stream().map(this::toEntity).collect(Collectors.toList());
	}

	public List<Caja> toDomainList(List<CajaEntity> entityList) {
		if (entityList == null) {
			return null;
		}
		return entityList.stream().map(this::toDomain).collect(Collectors.toList());
	}

}
