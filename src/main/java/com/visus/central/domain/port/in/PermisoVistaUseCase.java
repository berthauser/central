package com.visus.central.domain.port.in;

import java.util.List;
import java.util.Map;

import com.visus.central.domain.model.PermisoVista;

public interface PermisoVistaUseCase {

	boolean puedeVer(Integer usuarioId, String vistaClase);

	List<PermisoVista> obtenerPermisosPorUsuario(Integer usuarioId);

	List<PermisoVista> obtenerTodos();

	void guardar(PermisoVista permiso);

	void guardarTodos(List<PermisoVista> permisos);

	Map<String, Boolean> getMapaPermisos(Integer usuarioId, List<String> vistas);
}
