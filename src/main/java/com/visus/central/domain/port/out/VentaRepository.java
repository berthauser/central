package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.EstadoVenta;
import com.visus.central.domain.model.Venta;

public interface VentaRepository {

	Venta save(Venta venta);

	Optional<Venta> findById(Integer id);

	List<Venta> findByClienteId(Integer idCliente);

	List<Venta> findByArticuloCodigoBarra(String codigoBarra);

	int actualizarEstadoVenta(Integer idVenta, EstadoVenta estado);
}
