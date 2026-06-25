package com.visus.central.domain.port.in;

import java.math.BigDecimal;
import java.util.List;

import com.visus.central.domain.model.Devolucion;
import com.visus.central.domain.model.Venta;

public interface DevolucionUseCase {

    List<Venta> buscarVentasPorCliente(Integer idCliente);

    List<Venta> buscarVentasPorCodigoBarra(String codigoBarra);

    Devolucion procesarDevolucion(Integer idVenta, Integer idArticulo, Integer cantidad,
                                  BigDecimal montoDevuelto,
                                  boolean malEstado, String observaciones, Integer idUsuario);
}
