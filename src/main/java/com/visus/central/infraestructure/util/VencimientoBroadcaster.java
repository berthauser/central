package com.visus.central.infraestructure.util;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;

public class VencimientoBroadcaster {

	private static final Logger log = LoggerFactory.getLogger(VencimientoBroadcaster.class);
	private static final Set<UI> uis = ConcurrentHashMap.newKeySet();

	public static void register(UI ui) {
		uis.add(ui);
	}

	public static void unregister(UI ui) {
		uis.remove(ui);
	}

	public static void broadcastVencimientosActualizados() {
		log.debug("Broadcasting vencimientos actualizados a {} UIs activas", uis.size());
		uis.forEach(ui -> {
			if (ui != null && !ui.isClosing()) {
				ui.access(() -> {
					buscarView(ui, "CuentaCorrienteView").ifPresent(view -> ((Runnable) view).run());
					buscarView(ui, "PlanPagoDetalleView").ifPresent(view -> ((Runnable) view).run());
				});
			}
		});
	}

	private static Optional<Component> buscarView(UI ui, String nombreClase) {
		return buscarRecursivo(ui).filter(c -> c.getClass().getSimpleName().equals(nombreClase)).findFirst();
	}

	private static Stream<Component> buscarRecursivo(Component componente) {
		if (componente.getClass().getSimpleName().equals("CuentaCorrienteView")
				|| componente.getClass().getSimpleName().equals("PlanPagoDetalleView")) {
			return Stream.of(componente);
		}
		return componente.getChildren().flatMap(VencimientoBroadcaster::buscarRecursivo);
	}
}
