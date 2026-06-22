package com.visus.central.infraestructure.util;

import java.util.Optional;

/**
 * Patrón Broadcaster + Push
 * Ventajas sobre getAllSessions
 * No depende de HttpSession ni de hilos de solicitud HTTP
 * Funciona con Push habilitado (anotación @Push en la vista)
 * Solo notifica a las UIs que realmente tienen la vista abierta
 * No itera sesiones huérfanas o cerradas
 * Es el patrón oficial de Vaadin para comunicación entre sesiones
 * Asegurarse de tener @Push en la aplicación principal y las 
 * vistas implementan Runnable para que el Broadcaster pueda 
 * llamarlas genéricamente.
 */

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;

public class PagoBroadcaster {

	private static final Logger log = LoggerFactory.getLogger(PagoBroadcaster.class);
	private static final Set<UI> uis = ConcurrentHashMap.newKeySet();

	public static void register(UI ui) {
		uis.add(ui);
	}

	public static void unregister(UI ui) {
		uis.remove(ui);
	}

	public static void broadcastPagoRealizado() {
		log.debug("Broadcasting pago realizado a {} UIs activas", uis.size());
		uis.forEach(ui -> {
			if (ui != null && !ui.isClosing()) {
				ui.access(() -> {
					// Refrescar CuentaCorrienteView
					buscarView(ui, "CuentaCorrienteView").ifPresent(view -> ((Runnable) view).run());

					// Refrescar PlanPagoDetalleView
					buscarView(ui, "PlanPagoDetalleView").ifPresent(view -> ((Runnable) view).run());

					// Refrescar CajaView (si el pago afecta caja)
					buscarView(ui, "CajaView").ifPresent(view -> ((Runnable) view).run());
				});
			}
		});
	}

	private static Optional<Component> buscarView(UI ui, String nombreClase) {
		return buscarRecursivo(ui).filter(c -> c.getClass().getSimpleName().equals(nombreClase)).findFirst();
	}

	private static Stream<Component> buscarRecursivo(Component componente) {
		if (componente.getClass().getSimpleName().equals("CuentaCorrienteView")
				|| componente.getClass().getSimpleName().equals("PlanPagoDetalleView")
				|| componente.getClass().getSimpleName().equals("CajaView")) {
			return Stream.of(componente);
		}
		return componente.getChildren().flatMap(PagoBroadcaster::buscarRecursivo);
	}

}
