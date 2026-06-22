package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.PermisoVista;

public interface PermisoVistaRepository {

	List<PermisoVista> findAll();

	Optional<PermisoVista> findByUsuarioIdAndVistaClase(Integer usuarioId, String vistaClase);

	List<PermisoVista> findByUsuarioId(Integer usuarioId);

	List<PermisoVista> findByVistaClase(String vistaClase);

	PermisoVista save(PermisoVista permiso);

	void deleteById(Integer id);

	void deleteByUsuarioId(Integer usuarioId);

	boolean existsByUsuarioIdAndVistaClase(Integer usuarioId, String vistaClase);
}
