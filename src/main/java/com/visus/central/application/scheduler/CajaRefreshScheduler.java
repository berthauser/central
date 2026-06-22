package com.visus.central.application.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.visus.central.infraestructure.util.PagoBroadcaster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CajaRefreshScheduler {
	
	private static final Logger log = LoggerFactory.getLogger(CajaRefreshScheduler.class);

    @Scheduled(fixedDelay = 120000) // 2 minutos
    public void refrescarMovimientosCaja() {
        log.debug("Scheduler: enviando refresh de Caja");
        PagoBroadcaster.broadcastPagoRealizado();
    }

}
