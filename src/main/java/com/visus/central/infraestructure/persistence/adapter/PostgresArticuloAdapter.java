package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Articulo;
import com.visus.central.domain.port.out.ArticuloRepository;
import com.visus.central.infraestructure.converter.JpaArticuloMapper;
import com.visus.central.infraestructure.persistence.entity.JpaArticuloEntity;
import com.visus.central.infraestructure.persistence.repository.JpaArticuloRepository;

import jakarta.transaction.Transactional;

@Component
public class PostgresArticuloAdapter implements ArticuloRepository {

	private final JpaArticuloRepository jpaArticuloRepository;
	private final JpaArticuloMapper articuloMapper;

	public PostgresArticuloAdapter(JpaArticuloRepository jpaArticuloRepository, 
			JpaArticuloMapper articuloMapper) {
		this.jpaArticuloRepository = jpaArticuloRepository;
		this.articuloMapper = articuloMapper;
	}

	@Override
	public List<Articulo> findAll() {
		return jpaArticuloRepository.findAll().stream()
				.map(articuloMapper::toModel)
				.collect(Collectors.toList());
	}

	@Override
	public Page<Articulo> findAll(Pageable pageable) {
		return jpaArticuloRepository.findAll(pageable)
				.map(articuloMapper::toModel);
	}

	@Override
	public Page<Articulo> findByDescripcionContainingIgnoreCase(String descripcion, Pageable pageable) {
		return jpaArticuloRepository.findByDescripcionContainingIgnoreCase(descripcion, pageable)
				.map(articuloMapper::toModel);
	}
	
	@Override
	public Optional<Articulo> findById(Integer id) {
		return jpaArticuloRepository.findById(id)
				.map(articuloMapper::toModel);
	}
	
	@Override
	public Optional<Articulo> findByCodigoBarra(String codigoBarra) {
		return jpaArticuloRepository.findByCodigoBarraWithRelations(codigoBarra)
	            .map(articuloMapper::toModel);
	}

	@Override
	@Transactional
	public Articulo save(Articulo articulo) {
		JpaArticuloEntity entity = articuloMapper.toEntity(articulo);
		JpaArticuloEntity savedEntity = jpaArticuloRepository.save(entity);
		return articuloMapper.toModel(savedEntity);
	}

	@Override
	public void deleteById(Integer id) {
		jpaArticuloRepository.deleteById(id);
	}

	@Override
	public List<Articulo> findByLineaId(Integer idLinea) {
		return jpaArticuloRepository.findByLineaIdLinea(idLinea).stream()
				.map(articuloMapper::toModel)
				.collect(Collectors.toList());
	}

	@Override
	public List<Articulo> findByProveedorId(Integer idProveedor) {
		return jpaArticuloRepository.findByProveedorId(idProveedor).stream()
				.map(articuloMapper::toModel)
				.collect(Collectors.toList());
	}

	// NUEVOS MÉTODOS IMPLEMENTADOS

	@Override
	public Page<Articulo> findByEstado(JpaArticuloEntity.Estado estado, Pageable pageable) {
		return jpaArticuloRepository.findByEstado(estado, pageable)
				.map(articuloMapper::toModel);
	}

	@Override
	public Page<Articulo> findByDescripcionContainingIgnoreCaseAndEstado(
			String descripcion, JpaArticuloEntity.Estado estado, Pageable pageable) {
		return jpaArticuloRepository.findByDescripcionContainingIgnoreCaseAndEstado(
				descripcion, estado, pageable)
				.map(articuloMapper::toModel);
	}

	@Override
	public Page<Articulo> findByEstadoNot(JpaArticuloEntity.Estado estado, Pageable pageable) {
		return jpaArticuloRepository.findByEstadoNot(estado, pageable)
				.map(articuloMapper::toModel);
	}

	@Override
	public List<Articulo> findByEstadoNot(JpaArticuloEntity.Estado estado) {
		return jpaArticuloRepository.findByEstadoNot(estado).stream()
				.map(articuloMapper::toModel)
				.collect(Collectors.toList());
	}

	// NUEVOS MÉTODOS PARA LA ACTUALIZACIÓN DE PRECIOS
	@Override
    public List<Articulo> findByRubroIdAndLineaIdIn(Integer rubroId, List<Integer> lineasIds) {
        System.out.println("DEBUG Adapter - Buscando artículos por rubroId: " + rubroId + " y lineasIds: " + lineasIds);
        
        // Usa el método JPA que ya debería estar implementado
        List<JpaArticuloEntity> entities;
        
        try {
            // Intenta usar el método directo
            entities = jpaArticuloRepository.findByRubroIdAndLineaIdIn(rubroId, lineasIds);
            System.out.println("DEBUG Adapter - Método directo - Encontrados: " + entities.size() + " artículos");
        } catch (Exception e) {
            System.out.println("DEBUG Adapter - Error con método directo: " + e.getMessage());
            
            // Fallback: usar el método con JOIN FETCH
            entities = jpaArticuloRepository.findByLineaIdLineaInWithRubro(lineasIds);
            System.out.println("DEBUG Adapter - Fallback - Total artículos por líneas: " + entities.size());
            
         // Filtrar manualmente por rubro
            entities = entities.stream()
                    .filter(entity -> {
                        if (entity.getLinea() == null) {
                            System.out.println("DEBUG Adapter - Artículo sin línea: ID=" + entity.getId());
                            return false;
                        }
                        if (entity.getLinea().getRubro() == null) {
                            System.out.println("DEBUG Adapter - Línea sin rubro: LineaID=" + entity.getLinea().getIdLinea());
                            return false;
                        }
                        boolean matches = entity.getLinea().getRubro().getId().equals(rubroId);
                        if (matches) {
                            System.out.println("DEBUG Adapter - Artículo coincide: ID=" + entity.getId() + 
                                               ", Rubro=" + entity.getLinea().getRubro().getId());
                        }
                        return matches;
                    })
                    .collect(Collectors.toList());
            System.out.println("DEBUG Adapter - Fallback - Filtrados por rubro: " + entities.size());
        }
        
        return entities.stream()
                .map(articuloMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public int countByRubroIdAndLineaIdIn(Integer rubroId, List<Integer> lineasIds) {
        try {
            // Intenta usar el método directo
            return jpaArticuloRepository.countByRubroIdAndLineaIdIn(rubroId, lineasIds);
        } catch (Exception e) {
            System.out.println("DEBUG Adapter count - Error: " + e.getMessage());
            
            // Fallback: contar manualmente
            List<JpaArticuloEntity> entities = jpaArticuloRepository.findByLineaIdLineaInWithRubro(lineasIds);
            return (int) entities.stream()
                    .filter(entity -> entity.getLinea() != null 
                        && entity.getLinea().getRubro() != null 
                        && entity.getLinea().getRubro().getId().equals(rubroId))
                    .count();
        }
    }

}
