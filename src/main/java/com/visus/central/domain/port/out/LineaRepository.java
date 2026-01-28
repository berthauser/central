package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;
import com.visus.central.domain.model.Linea;

public interface LineaRepository {
	List<Linea> listar();
	Optional<Linea> buscarPorId(Integer id);
	
	// Método específico para buscar por rubro
	List<Linea> findByRubroId(Integer idRubro);
	
	void guardar(Linea linea);
	void eliminar(Integer id);
	boolean existePorRubroId(Integer idRubro);

}
