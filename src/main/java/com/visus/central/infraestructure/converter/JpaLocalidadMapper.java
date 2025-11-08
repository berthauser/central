package com.visus.central.infraestructure.converter;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Departamento;
import com.visus.central.domain.model.Localidad;
import com.visus.central.infraestructure.persistence.entity.JpaDepartamentoEntity;
import com.visus.central.infraestructure.persistence.entity.JpaLocalidadEntity;

@Component
public class JpaLocalidadMapper {
	
	public Localidad toDomain(JpaLocalidadEntity entity) {
        if (entity == null) return null;

        Localidad model = new Localidad();
        model.setIdlocalidad(entity.getIdlocalidad());
        model.setNombre(entity.getNombre());
        model.setCodigoPostal(entity.getCodigoPostal());

        if (entity.getDepartamento() != null) {
            Departamento d = new Departamento();
            d.setId(entity.getDepartamento().getId());
            d.setNombre(entity.getDepartamento().getNombre());
            model.setDepartamento(d);
        }

        return model;
    }

	public JpaLocalidadEntity toEntity(Localidad model) {
        if (model == null) return null;

        JpaLocalidadEntity entity = new JpaLocalidadEntity();
        entity.setIdlocalidad(model.getIdlocalidad());
        entity.setNombre(model.getNombre());
        entity.setCodigoPostal(model.getCodigoPostal());

        if (model.getDepartamento() != null) {
            JpaDepartamentoEntity d = new JpaDepartamentoEntity();
            d.setId(model.getDepartamento().getId());
            entity.setDepartamento(d);
        }

        return entity;
    }


}
