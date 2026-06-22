package com.visus.central.domain.port.out;

import java.util.Optional;

import com.visus.central.domain.model.EstadoVenta;
import com.visus.central.domain.model.Venta;

public interface VentaRepository {

	Venta save(Venta venta);

	Optional<Venta> findById(Integer id);
	
	int actualizarEstadoVenta(Integer idVenta, EstadoVenta estado); 
}
