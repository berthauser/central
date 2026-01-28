package com.visus.central.application.usecase;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.visus.central.application.exception.EntityNotFoundException;
import com.visus.central.domain.model.Articulo;
import com.visus.central.domain.port.in.ArticuloUseCase;
import com.visus.central.domain.port.out.ArticuloRepository;
import com.visus.central.infraestructure.persistence.entity.JpaArticuloEntity;
import com.visus.central.infraestructure.persistence.entity.JpaArticuloEntity.Estado;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ArticulosUseCaseImpl implements ArticuloUseCase {

	private final ArticuloRepository articuloRepository;

	public ArticulosUseCaseImpl(ArticuloRepository articuloRepository) { 
		this.articuloRepository = articuloRepository;
	}
	
	@Override
    public List<Articulo> findAll() {
        return articuloRepository.findAll();
    }

    @Override
	public Articulo findById(Integer id) {
		return articuloRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Artículo", id));
	}

    @Override
    public Articulo save(Articulo articulo) {
        return articuloRepository.save(articulo);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        // Cambia el estado a "No Disponible" en lugar de eliminar físicamente
        Optional<Articulo> articuloOpt = articuloRepository.findById(id);
        if (articuloOpt.isPresent()) {
            Articulo articulo = articuloOpt.get();
            articulo.setEstado(JpaArticuloEntity.Estado.NoDisponible);
            articuloRepository.save(articulo);
        }
    }

    @Override
    public void cambiarEstado(Integer id, String nuevoEstado) {
        Optional<Articulo> articuloOpt = articuloRepository.findById(id);
        if (articuloOpt.isPresent()) {
            Articulo articulo = articuloOpt.get();
            try {
                JpaArticuloEntity.Estado estado = JpaArticuloEntity.Estado.fromLabel(nuevoEstado);
                articulo.setEstado(estado);
                articuloRepository.save(articulo);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Estado no válido: " + nuevoEstado);
            }
        } else {
            throw new IllegalArgumentException("Artículo no encontrado con ID: " + id);
        }
    }
    
    @Override
    public List<Articulo> buscarActivos() {
        return articuloRepository.findByEstadoNot(JpaArticuloEntity.Estado.NoDisponible);
    }

    @Override
    public Page<Articulo> buscarActivosPaginado(Pageable pageable) {
        return articuloRepository.findByEstadoNot(JpaArticuloEntity.Estado.NoDisponible, pageable);
    }

    @Override
    public Page<Articulo> buscarPorDescripcion(String descripcion, Pageable pageable) {
        return articuloRepository.findByDescripcionContainingIgnoreCase(descripcion, pageable);
    }

    @Override
    public Optional<Articulo> buscarPorCodigoBarra(String codigoBarra) {
        return articuloRepository.findByCodigoBarra(codigoBarra);
    }

    @Override
    public List<Articulo> buscarPorLinea(Integer idLinea) {
        return articuloRepository.findByLineaId(idLinea);
    }

    @Override
    public List<Articulo> buscarPorProveedor(Integer idProveedor) {
        return articuloRepository.findByProveedorId(idProveedor);
    }
    
    @Override
    public Page<Articulo> buscarPorEstado(JpaArticuloEntity.Estado estado, Pageable pageable) {
        return articuloRepository.findByEstado(estado, pageable);
    }

    @Override
    public Page<Articulo> buscarPorDescripcionYEstado(String descripcion, JpaArticuloEntity.Estado estado, Pageable pageable) {
        return articuloRepository.findByDescripcionContainingIgnoreCaseAndEstado(descripcion, estado, pageable);
    }
    
    @Override
    public Page<Articulo> buscarConFiltros(String descripcion, String estado, Pageable pageable) {
        if (estado == null || "Todos".equals(estado)) {
            if (descripcion == null || descripcion.trim().isEmpty()) {
                return articuloRepository.findAll(pageable);
            } else {
                return articuloRepository.findByDescripcionContainingIgnoreCase(descripcion, pageable);
            }
        } else {
            JpaArticuloEntity.Estado estadoEnum = JpaArticuloEntity.Estado.fromLabel(estado);
            if (descripcion == null || descripcion.trim().isEmpty()) {
                return articuloRepository.findByEstado(estadoEnum, pageable);
            } else {
                return articuloRepository.findByDescripcionContainingIgnoreCaseAndEstado(descripcion, estadoEnum, pageable);
            }
        }
    }
    
	@Override
	public Page<Articulo> buscarPorRubro(String rubro, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Articulo> buscarPorRubroYEstado(String rubro, Estado estado, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Articulo> buscarPorRubroYDescripcion(String rubro, String descripcion, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Articulo> buscarPorRubroDescripcionYEstado(String rubro, String descripcion, Estado estado,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
