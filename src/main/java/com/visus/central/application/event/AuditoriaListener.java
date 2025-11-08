package com.visus.central.application.event;

import org.springframework.context.event.EventListener;

public class AuditoriaListener {
	
	@EventListener
    public <T> void onDomainEvent(DomainEvent<T> event) {
        System.out.printf("📝 Evento: %s | Entidad: %s | Hora: %s%n",
            event.getAction(),
            event.getPayload().getClass().getSimpleName(),
            event.getTimestamp()
        );
    }


}
