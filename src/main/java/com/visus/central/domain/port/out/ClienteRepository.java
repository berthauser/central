package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Cliente;
import com.visus.central.domain.model.ClienteComboDTO;

public interface ClienteRepository {
	
	List<Cliente> findAll();

	List<Cliente> findByNombreContainingIgnoreCase(String nombre);

	List<Cliente> findAllClientesBasico(); // solo clientes habilitados
	
	List<ClienteComboDTO> findClientesParaCombo();

	Optional<Cliente> findById(Integer id);

	Cliente save(Cliente cliente);

	void deleteById(Integer id);

	boolean existsByEmail(String email);
}
