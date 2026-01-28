package com.visus.central.domain.port.in;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Rubro;

public interface RubroUseCase {
	List<Rubro> listar();
    Optional<Rubro> buscarPorId(Integer id);
    void guardar(Rubro rubro);
    void eliminar(Integer id);
    boolean tieneLineasAsociadas(Integer idRubro);

}
