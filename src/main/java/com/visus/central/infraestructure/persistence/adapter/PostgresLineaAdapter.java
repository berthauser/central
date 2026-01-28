package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Linea;
import com.visus.central.domain.port.out.LineaRepository;
import com.visus.central.infraestructure.converter.JpaLineaMapper;
import com.visus.central.infraestructure.persistence.entity.JpaLineaEntity;
import com.visus.central.infraestructure.persistence.repository.JpaLineaRepository;
import jakarta.transaction.Transactional;

@Component
public class PostgresLineaAdapter implements LineaRepository {
	
	private final JpaLineaRepository jpaLineaRepository;
    private final JpaLineaMapper mapper;
	
    public PostgresLineaAdapter(JpaLineaRepository jpaLineaRepository, JpaLineaMapper mapper) {
		this.jpaLineaRepository = jpaLineaRepository;
		this.mapper = mapper;
	}
    
    @Override
    public List<Linea> listar() {
        return jpaLineaRepository.findAll().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Linea> findByRubroId(Integer idRubro) {
        System.out.println("PostgresLineaAdapter.findByRubroId() - Rubro ID: " + idRubro);
        
        // Usar el método del repositorio JPA
        List<JpaLineaEntity> entities = jpaLineaRepository.findByRubroId(idRubro);
        
        System.out.println("Líneas encontradas para rubro " + idRubro + ": " + entities.size());
        
        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<Linea> buscarPorId(Integer id) {
        return jpaLineaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void guardar(Linea linea) {
    	jpaLineaRepository.save(mapper.toEntity(linea));
    }

    @Override
    public void eliminar(Integer id) {
    	jpaLineaRepository.deleteById(id);
    }

    @Override
    public boolean existePorRubroId(Integer idRubro) {
        return jpaLineaRepository.existsById(idRubro);
    }

}
