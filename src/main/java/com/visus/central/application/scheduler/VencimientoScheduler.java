package com.visus.central.application.scheduler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.visus.central.domain.model.EstadoPlanPago;
import com.visus.central.domain.model.PlanPago;
import com.visus.central.domain.port.out.PlanPagoRepository;
import com.visus.central.infraestructure.util.VencimientoBroadcaster;

@Component
public class VencimientoScheduler {

	private static final Logger log = LoggerFactory.getLogger(VencimientoScheduler.class);
	private final PlanPagoRepository planPagoRepository;

	public VencimientoScheduler(PlanPagoRepository planPagoRepository) {
		this.planPagoRepository = planPagoRepository;
	}

	@Scheduled(cron = "0 0 8 * * MON-SAT")
	@Transactional
	public void actualizarCuotasVencidas() {
		log.info("Iniciando actualizaci\u00f3n de cuotas vencidas...");
		List<PlanPago> cuotasPendientes = planPagoRepository
				.findByEstadoIn(List.of(EstadoPlanPago.PENDIENTE, EstadoPlanPago.PARCIAL));

		int actualizadas = 0;
		for (PlanPago cuota : cuotasPendientes) {
			if (cuota.marcarComoVencida()) {
				planPagoRepository.save(cuota);
				actualizadas++;
			}
		}
		log.info("Finalizada actualizaci\u00f3n. Cuotas marcadas como VENCIDA: {}", actualizadas);

		if (actualizadas > 0) {
			VencimientoBroadcaster.broadcastVencimientosActualizados();
		}
	}
}
