package com.visus.central.domain.port.in;

import java.math.BigDecimal;
import java.util.List;

public interface ActualizarPreciosUseCase {
	int actualizarPreciosPorRubroYLineas(Integer rubroId, List<Integer> lineasIds, BigDecimal porcentaje);
	
	int actualizarPrecioPorCodigoBarra(String codigoBarra, BigDecimal porcentaje);

}
