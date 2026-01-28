package com.visus.central.domain.port.in;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.visus.central.domain.model.Articulo;
import com.visus.central.infraestructure.persistence.entity.JpaArticuloEntity;

public interface ArticuloUseCase extends CrudUseCase<Articulo> {
	 
	// Métodos existentes
	Page<Articulo> buscarPorDescripcion(String descripcion, Pageable pageable);
	Optional<Articulo> buscarPorCodigoBarra(String codigoBarra);
	List<Articulo> buscarPorLinea(Integer idLinea);
	List<Articulo> buscarPorProveedor(Integer idProveedor);

	// Nuevos métodos
	void cambiarEstado(Integer id, String nuevoEstado);

	// Para filtro de estado
	Page<Articulo> buscarPorEstado(JpaArticuloEntity.Estado estado, Pageable pageable);
	Page<Articulo> buscarPorDescripcionYEstado(String descripcion, JpaArticuloEntity.Estado estado, Pageable pageable);

	// Para mostrar solo artículos activos (no "No Disponible")
	List<Articulo> buscarActivos();
	Page<Articulo> buscarActivosPaginado(Pageable pageable);

	// Método flexible (alternativa)
	Page<Articulo> buscarConFiltros(String descripcion, String estado, Pageable pageable);
	
	// Métodos adicionales
	Page<Articulo> buscarPorRubro(String rubro, Pageable pageable);
	Page<Articulo> buscarPorRubroYEstado(String rubro, JpaArticuloEntity.Estado estado, Pageable pageable);
	Page<Articulo> buscarPorRubroYDescripcion(String rubro, String descripcion, Pageable pageable);
	Page<Articulo> buscarPorRubroDescripcionYEstado(String rubro, String descripcion, JpaArticuloEntity.Estado estado, Pageable pageable);

}
