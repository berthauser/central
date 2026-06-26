package com.visus.central.infraestructure.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.visus.central.domain.model.ClienteComboDTO;
import com.visus.central.domain.model.Estado;
import com.visus.central.infraestructure.persistence.entity.JpaClienteEntity;

public interface JpaClienteRepository extends JpaRepository<JpaClienteEntity, Integer> {

	// Busca clientes cuyo nombreFantasia contenga el texto (ignorando
	// mayúsculas/minúsculas)
	List<JpaClienteEntity> findByNombreFantasiaContainingIgnoreCase(String nombreFantasia);

	List<JpaClienteEntity> findByNombreClienteContainingIgnoreCase(String nombreCliente);

	// Busca clientes que coincidan con cualquiera de los dos nombres
	List<JpaClienteEntity> findByNombreFantasiaContainingIgnoreCaseOrNombreClienteContainingIgnoreCase(
			String nombreFantasia, String nombreCliente);

	// Proyección para clientes
	@Query("SELECT new com.visus.central.domain.model.ClienteComboDTO(c.id, c.nombreCliente, 'CLIENTE', c.id, null) "
			+ "FROM JpaClienteEntity c WHERE c.estado = com.visus.central.domain.model.Estado.Habilitado")
	List<ClienteComboDTO> findClientesParaCombo();

	// Versión paginada
	Page<JpaClienteEntity> findByNombreFantasiaContainingIgnoreCaseOrNombreClienteContainingIgnoreCase(
			String nombreFantasia, String nombreCliente, Pageable pageable);

	// Buscar por el enum Estado
	List<JpaClienteEntity> findByEstado(Estado estado);

	List<JpaClienteEntity> findAll(); // todos, sin filtro

	// Verifica si ya existe un cliente con ese email (unicidad)
	boolean existsByEmail(String email);

	boolean existsByNombreFantasia(String nombreFantasia);

	boolean existsByNombreCliente(String nombreCliente);

	// Búsqueda por otros campos
	List<JpaClienteEntity> findBySexo(String sexo);

	List<JpaClienteEntity> findByEstado(String estado);

	List<JpaClienteEntity> findBySituacionFiscal(String situacionFiscal);

	List<JpaClienteEntity> findByEmailContainingIgnoreCase(String email);

	List<JpaClienteEntity> findByNumero(Long numero);

}
