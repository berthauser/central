package com.visus.central.domain.port.out;

import java.util.List;
import com.visus.central.domain.model.Unidad;

public interface UnidadRepository {

	List<Unidad> findByPresentacionId(Integer idPresentacion);
}
