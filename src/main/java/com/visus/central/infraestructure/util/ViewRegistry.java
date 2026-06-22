package com.visus.central.infraestructure.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.visus.central.ui.view.AccesoDenegadoView;
import com.visus.central.ui.view.LoginView;
import com.visus.central.ui.view.PermisosView;

public final class ViewRegistry {

	private ViewRegistry() {
	}

	private static final Map<Class<?>, String> VIEWS = new LinkedHashMap<>();

	private static final Set<Class<?>> PUBLIC_VIEWS = Set.of(
			LoginView.class,
			AccesoDenegadoView.class,
			PermisosView.class);

	private static final Map<String, String> DISPLAY_NAME_OVERRIDES = new LinkedHashMap<>();

	static {
		DISPLAY_NAME_OVERRIDES.put("CuentaCorrienteView", "Cuenta Corriente");
		DISPLAY_NAME_OVERRIDES.put("ActualizacionPreciosRubroLineaView", "Precios por Rubro/L\u00ednea");
		DISPLAY_NAME_OVERRIDES.put("ActualizacionPreciosCodigoBarraView", "Precios por C\u00f3digo Barra");
		DISPLAY_NAME_OVERRIDES.put("TipoPagoView", "Tipos de Pago");
	}

	public static synchronized void init(Set<Class<?>> viewClasses) {
		VIEWS.clear();
		// Home entry always first
		VIEWS.put(null, "Inicio");
		for (Class<?> clazz : viewClasses) {
			if (clazz == null || isPublica(clazz))
				continue;
			VIEWS.put(clazz, generarNombreVista(clazz));
		}
	}

	public static synchronized List<Map.Entry<Class<?>, String>> getVistas() {
		if (VIEWS.isEmpty())
			return List.of();
		return List.copyOf(VIEWS.entrySet());
	}

	public static synchronized Map<Class<?>, String> getViewsMap() {
		if (VIEWS.isEmpty())
			return Map.of();
		return Collections.unmodifiableMap(VIEWS);
	}

	public static String getNombreVista(Class<?> viewClass) {
		if (viewClass == null)
			return "Inicio";
		String name = VIEWS.get(viewClass);
		return name != null ? name : generarNombreVista(viewClass);
	}

	public static boolean isPublica(Class<?> viewClass) {
		return viewClass == null || PUBLIC_VIEWS.contains(viewClass);
	}

	public static String generarNombreVista(Class<?> viewClass) {
		if (viewClass == null)
			return "Inicio";
		String simpleName = viewClass.getSimpleName();

		String override = DISPLAY_NAME_OVERRIDES.get(simpleName);
		if (override != null)
			return override;

		if (simpleName.endsWith("View")) {
			simpleName = simpleName.substring(0, simpleName.length() - 4);
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < simpleName.length(); i++) {
			char c = simpleName.charAt(i);
			if (i > 0 && Character.isUpperCase(c)) {
				sb.append(' ');
			}
			sb.append(c);
		}
		return sb.toString().trim();
	}

	public static boolean isInitialized() {
		return !VIEWS.isEmpty();
	}
}
