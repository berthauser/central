package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.LazyInitializationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.visus.central.domain.model.Banco;
import com.visus.central.domain.model.Proveedor;
import com.visus.central.domain.port.out.ProveedorRepository;
import com.visus.central.infraestructure.converter.JpaBancoMapper;
import com.visus.central.infraestructure.persistence.entity.JpaBancoEntity;
import com.visus.central.infraestructure.persistence.entity.JpaProveedorEntity;
import com.visus.central.infraestructure.persistence.repository.JpaBancoRepository;
import com.visus.central.infraestructure.persistence.repository.JpaProveedorRepository;

@Component
public class PostgresProveedorAdapter implements ProveedorRepository {

    private final JpaProveedorRepository jpaRepository;
    private final JpaBancoRepository bancoRepository; // NUEVA DEPENDENCIA

    public PostgresProveedorAdapter(
        JpaProveedorRepository jpaRepository,
        JpaBancoRepository bancoRepository // 👈 INYECCIÓN DE DEPENDENCIA
    ) {
        this.jpaRepository = jpaRepository;
        this.bancoRepository = bancoRepository;
    }

    @Override
    public List<Proveedor> findAll() {
        return jpaRepository.findAll().stream()
            .map(this::toModel)
            .collect(Collectors.toList());
    }

    @Override
    public List<Proveedor> findByNombreContainingIgnoreCase(String nombre) {
        // USAR EL MÉTODO DERIVADO CORRECTO
        List<JpaProveedorEntity> entities = jpaRepository
            .findByNombreFantasiaContainingIgnoreCaseOrNombreRealContainingIgnoreCase(
                nombre, nombre);
        
        return entities.stream()
            .map(this::toModel)
            .collect(Collectors.toList());
    }
    
    @Override
    public Optional<Proveedor> findById(Integer id) {
        return jpaRepository.findById(id).map(this::toModel);
    }

    @Override
    @Transactional
    public Proveedor save(Proveedor proveedor) {
    	JpaProveedorEntity entity = toEntity(proveedor);
    	JpaProveedorEntity saved = jpaRepository.save(entity);
    	return toModel(saved);
    }

    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public List<Banco> findAllBancos() {
    	List<JpaBancoEntity> entities = bancoRepository.findAll();
        return JpaBancoMapper.toModelList(entities); // 👈 Usa el método protegido
    }

    private Proveedor toModel(JpaProveedorEntity entity) {
        Proveedor model = new Proveedor();
        model.setId(entity.getId());
        model.setNumero(entity.getNumero());
        model.setNombreFantasia(entity.getNombreFantasia());
        model.setNombreReal(entity.getNombreReal());
        model.setTelefonoUno(entity.getTelefonoUno());
        model.setTelefonoDos(entity.getTelefonoDos());
        model.setTelefonoTres(entity.getTelefonoTres());
        model.setSituacionFiscal(entity.getSituacionFiscal());
        
        // Mapear Banco y PROTEGER CONTRA NULL Y LAZY INITIALIZATION
        if (entity.getBanco() != null) {
            try {
                model.setBanco(JpaBancoMapper.toModel(entity.getBanco()));
            } catch (LazyInitializationException e) {
                // Si falla lazy loading, crear banco con solo el ID
                Banco banco = new Banco();
                banco.setId(entity.getIdBanco());
                model.setBanco(banco);
            }
        }
        
        model.setEmail(entity.getEmail());
        model.setDocumento(entity.getDocumento());
        model.setEstado(entity.getEstado());
        return model;
    }
    
    private JpaProveedorEntity toEntity(Proveedor model) {
//    	System.out.println("Convirtiendo Proveedor a JpaProveedorEntity");
    	JpaProveedorEntity entity = new JpaProveedorEntity();
        entity.setId(model.getId());
        entity.setNumero(model.getNumero());
        entity.setNombreFantasia(model.getNombreFantasia());
        entity.setNombreReal(model.getNombreReal());
        entity.setTelefonoUno(model.getTelefonoUno());
        entity.setTelefonoDos(model.getTelefonoDos());
        entity.setTelefonoTres(model.getTelefonoTres());
        entity.setSituacionFiscal(model.getSituacionFiscal());
        
        // MAPEAR BANCO DESDE EL MODELO
        if (model.getBanco() != null && model.getBanco().getId() != null) {
        	System.out.println("Mapeando banco - ID: " + model.getBanco().getId());
            JpaBancoEntity bancoEntity = new JpaBancoEntity();
            bancoEntity.setId(model.getBanco().getId());
            bancoEntity.setNombre(model.getBanco().getNombre()); // SOLO PARA PRUEBAS
            entity.setBanco(bancoEntity);
            System.out.println("ID + Banco Grabado: " + bancoEntity.getId() + ' ' + bancoEntity.getNombre());
        } else {
        	System.out.println("Banco es null o sin ID");
            entity.setBanco(null);
        }
        
        entity.setEmail(model.getEmail());
        entity.setDocumento(model.getDocumento());
        entity.setEstado(model.getEstado());
//        System.out.println("Entidad creada - Banco ID: " + entity.getIdBanco());
        return entity;
    }

}
