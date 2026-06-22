package com.visus.central.infraestructure.persistence.adapter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.visus.central.domain.model.Caja;
import com.visus.central.domain.model.MovimientoCaja;
import com.visus.central.domain.port.out.CajaReadRepository;
import com.visus.central.domain.port.out.CajaRepository;
import com.visus.central.domain.port.out.MovimientoCajaRepository;
import com.visus.central.domain.port.out.dto.MovimientoReporte;
import com.visus.central.domain.port.out.dto.ReporteCajaDiaria;

@Repository
public class CajaReadAdapter implements CajaReadRepository {

	@Autowired
	private CajaRepository cajaRepo;

	@Autowired
	private MovimientoCajaRepository movimientoRepo;

	@Override
	public ReporteCajaDiaria obtenerReporteCajaDiaria(Integer idCaja) {
		// 1. Obtener la caja
		Caja caja = cajaRepo.findById(idCaja)
				.orElseThrow(() -> new IllegalArgumentException("Caja no encontrada con ID: " + idCaja));

		// 2. Obtener todos los movimientos de esa caja
		List<MovimientoCaja> movimientos = movimientoRepo.findByCaja(caja);

		// 3. Calcular totales
		BigDecimal totalIngresos = movimientos.stream().filter(MovimientoCaja::esIngreso).map(MovimientoCaja::getDebe)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal totalEgresos = movimientos.stream().filter(MovimientoCaja::esEgreso).map(MovimientoCaja::getHaber)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal saldoFinal = caja.getSaldoInicial().add(totalIngresos).subtract(totalEgresos);

		// 4. Convertir movimientos a MovimientoReporte (record)
		List<MovimientoReporte> movimientosReporte = movimientos.stream()
				.map(mov -> new MovimientoReporte(mov.getFecha(), mov.getHora(), mov.getDescripcion(),
						mov.esIngreso() ? mov.getDebe() : BigDecimal.ZERO,
						mov.esEgreso() ? mov.getHaber() : BigDecimal.ZERO,
						mov.getOrigen() != null ? mov.getOrigen().toString() : ""))
				.collect(Collectors.toList());

		// 5. Construir el ReporteCajaDiaria (record)
		return new ReporteCajaDiaria(caja.getId(), caja.getFechaApertura(), caja.getHoraApertura(),
				caja.getFechaCierre(), caja.getHoraCierre(), caja.getSaldoInicial(), totalIngresos, totalEgresos,
				saldoFinal, movimientosReporte);
	}

}
