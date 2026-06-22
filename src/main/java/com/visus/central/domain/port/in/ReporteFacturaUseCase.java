package com.visus.central.domain.port.in;

import com.visus.central.domain.model.Venta;

public interface ReporteFacturaUseCase {
    byte[] generarRemito(Venta venta);
}
