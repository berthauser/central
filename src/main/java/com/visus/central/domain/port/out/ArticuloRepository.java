package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.visus.central.domain.model.Articulo;
import com.visus.central.domain.model.EstadoArticulo;

public interface ArticuloRepository {
	
	List<Articulo> findAll();

	// Métodos existentes
	Page<Articulo> findByDescripcionContainingIgnoreCase(String descripcion, Pageable pageable);
	Optional<Articulo> findByCodigoBarra(String codigoBarra);
	Optional<Articulo> findById(Integer id);
	Articulo save(Articulo articulo);
	void deleteById(Integer id);
	
	// Búsquedas específicas
	List<Articulo> findByLineaId(Integer idLinea);
	List<Articulo> findByProveedorId(Integer idProveedor);

	// NUEVOS MÉTODOS PARA FILTRO DE ESTADO
	Page<Articulo> findByEstado(EstadoArticulo estado, Pageable pageable);
	Page<Articulo> findByDescripcionContainingIgnoreCaseAndEstado(
			String descripcion, EstadoArticulo estado, Pageable pageable);

	// Métodos para búsqueda excluyendo "No Disponible"
	Page<Articulo> findByEstadoNot(EstadoArticulo estado, Pageable pageable);
	List<Articulo> findByEstadoNot(EstadoArticulo estado);

	// Método para buscar todos con paginación
	Page<Articulo> findAll(Pageable pageable);
	
	// Usados para la actualizacion de Precios
	List<Articulo> findByRubroIdAndLineaIdIn(Integer rubroId, List<Integer> lineasIds);
	int countByRubroIdAndLineaIdIn(Integer rubroId, List<Integer> lineasIds);

}
