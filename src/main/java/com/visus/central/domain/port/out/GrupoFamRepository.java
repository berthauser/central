package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.ClienteComboDTO;
import com.visus.central.domain.model.GrupoFam;

public interface GrupoFamRepository {
	List<GrupoFam> findAll();

	Optional<GrupoFam> findById(Integer id);

	GrupoFam save(GrupoFam grupoFam);

	void deleteById(Integer id);

	List<ClienteComboDTO> findFamiliaresParaCombo(); // NUEVO

	List<GrupoFam> findByNombreContainingIgnoreCase(String nombre);

	List<GrupoFam> findByClienteId(Integer idCliente);

	boolean existsByNumero(Integer numero);

}
