package com.visus.central.domain.port.in;

import java.util.List;
import java.util.Optional;
import com.visus.central.domain.model.Linea;

public interface LineaUseCase {
	List<Linea> listar();
	List<Linea> findByRubroId(Integer idRubro); // Para carga bajo demanda
    Optional<Linea> buscarPorId(Integer id);
    void guardar(Linea linea);
    void eliminar(Integer id);
    boolean tieneRubrosAsociados(Integer idRubro);

}
