package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Articulo;
import com.visus.central.domain.model.EstadoArticulo;
import com.visus.central.domain.model.Unidad;
import com.visus.central.domain.model.UnidadConCantidad;
import com.visus.central.domain.port.out.ArticuloRepository;
import com.visus.central.infraestructure.converter.JpaArticuloMapper;
import com.visus.central.infraestructure.persistence.entity.JpaArticuloEntity;
import com.visus.central.infraestructure.persistence.entity.JpaArticuloUnidadEntity;
import com.visus.central.infraestructure.persistence.entity.JpaUnidadEntity;
import com.visus.central.infraestructure.persistence.repository.JpaArticuloRepository;
import com.visus.central.infraestructure.persistence.repository.JpaArticuloUnidadRepository;

import jakarta.transaction.Transactional;

@Component
public class PostgresArticuloAdapter implements ArticuloRepository {

	private final JpaArticuloRepository jpaArticuloRepository;
	private final JpaArticuloMapper articuloMapper;
	private final JpaArticuloUnidadRepository jpaArticuloUnidadRepository;

	public PostgresArticuloAdapter(JpaArticuloRepository jpaArticuloRepository,
			JpaArticuloMapper articuloMapper,
			JpaArticuloUnidadRepository jpaArticuloUnidadRepository) {
		this.jpaArticuloRepository = jpaArticuloRepository;
		this.articuloMapper = articuloMapper;
		this.jpaArticuloUnidadRepository = jpaArticuloUnidadRepository;
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
	@Transactional
	public Optional<Articulo> findById(Integer id) {
		return jpaArticuloRepository.findById(id)
				.map(articuloMapper::toModel)
				.map(this::cargarUnidades);
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

		if (savedEntity.getId() != null) {
			jpaArticuloUnidadRepository.deleteByArticuloId(savedEntity.getId());
			if (articulo.getUnidades() != null) {
				for (UnidadConCantidad uc : articulo.getUnidades()) {
					if (uc.getUnidad() != null && uc.getUnidad().getId() != null) {
						JpaArticuloUnidadEntity au = new JpaArticuloUnidadEntity();
						au.setArticulo(savedEntity);
						JpaUnidadEntity jpaUnidad = new JpaUnidadEntity();
						jpaUnidad.setId(uc.getUnidad().getId());
						au.setUnidad(jpaUnidad);
						au.setCantidad(uc.getCantidad() != null ? uc.getCantidad() : 1);
						jpaArticuloUnidadRepository.save(au);
					}
				}
			}
		}

		Articulo model = articuloMapper.toModel(savedEntity);
		model.setUnidades(articulo.getUnidades());
		return model;
	}

	private Articulo cargarUnidades(Articulo model) {
		if (model != null && model.getId() != null) {
			List<UnidadConCantidad> unidades = jpaArticuloUnidadRepository.findByArticuloId(model.getId())
					.stream()
					.map(au -> {
						Unidad u = new Unidad();
						u.setId(au.getUnidad().getId());
						u.setIdPresentacion(au.getUnidad().getIdPresentacion());
						u.setMedida(au.getUnidad().getMedida());
						Integer cantidad = au.getCantidad() != null ? au.getCantidad() : 1;
						return new UnidadConCantidad(u, cantidad);
					})
					.collect(Collectors.toList());
			model.setUnidades(unidades);
		}
		return model;
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
	public Page<Articulo> findByEstado(EstadoArticulo estado, Pageable pageable) {
		return jpaArticuloRepository.findByEstado(estado, pageable)
				.map(articuloMapper::toModel);
	}

	@Override
	public Page<Articulo> findByDescripcionContainingIgnoreCaseAndEstado(
			String descripcion, EstadoArticulo estado, Pageable pageable) {
		return jpaArticuloRepository.findByDescripcionContainingIgnoreCaseAndEstado(
				descripcion, estado, pageable)
				.map(articuloMapper::toModel);
	}

	@Override
	public Page<Articulo> findByEstadoNot(EstadoArticulo estado, Pageable pageable) {
		return jpaArticuloRepository.findByEstadoNot(estado, pageable)
				.map(articuloMapper::toModel);
	}

	@Override
	public List<Articulo> findByEstadoNot(EstadoArticulo estado) {
		return jpaArticuloRepository.findByEstadoNot(estado).stream()
				.map(articuloMapper::toModel)
				.collect(Collectors.toList());
	}

	@Override
	public List<Articulo> findByRubroId(Integer rubroId) {
		return jpaArticuloRepository.findByRubroId(rubroId).stream()
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
