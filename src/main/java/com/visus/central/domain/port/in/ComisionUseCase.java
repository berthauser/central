package com.visus.central.domain.port.in;

import java.time.LocalDate;
import java.util.List;

import com.visus.central.domain.model.ComisionVenta;

public interface ComisionUseCase {

	List<ComisionVenta> findAll();

	List<ComisionVenta> findByVendedorId(Integer idVendedor);

	List<ComisionVenta> findByFechaBetween(LocalDate desde, LocalDate hasta);

	List<ComisionVenta> findByVentaId(Integer idVenta);

	ComisionVenta calcularComisionPorPago(Long idPago);

	List<ComisionVenta> calcularComisionesPendientes();

	List<ComisionVenta> calcularComisionesPeriodo(int mes, int anio);

	void anularComision(Long idComision, String motivo);
}
