package com.visus.central.domain.port.in;

import java.math.BigDecimal;

import com.visus.central.domain.model.Venta;

public interface FacturacionUseCase {

	Venta iniciarNuevaVenta();

	void agregarItem(Venta venta, String codigoBarra, Integer cantidad);

	void eliminarItem(Venta venta, int index);

	void aplicarDescuento(Venta venta, BigDecimal porcentaje);

	void guardarVenta(Venta venta, boolean imprimir);

	BigDecimal calcularSubtotal(Venta venta);

	BigDecimal calcularTotal(Venta venta);

	/**
	 * Verifica si existe una caja abierta en el sistema.
	 * 
	 * @return true si hay una caja abierta, false en caso contrario.
	 */
	boolean hayCajaAbierta();

}
