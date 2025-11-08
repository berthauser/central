package com.visus.central.infraestructure.security;

import com.vaadin.flow.server.VaadinSession;

public final class SecurityUtils {
	
	private SecurityUtils() {
        // Clase utilitaria: constructor privado
    }

	public static String getUsername() {
        return (String) VaadinSession.getCurrent().getAttribute("username");
    }

    public static String getRole() {
        return (String) VaadinSession.getCurrent().getAttribute("role");
    }

    public static boolean hasRole(String role) {
        String currentRole = getRole();
        return currentRole != null && currentRole.equals(role);
    }

    public static boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(getUsername()) && hasRole("ROLE_ADMIN");
    }

    public static boolean isAuthenticated() {
        return getUsername() != null && getRole() != null;
    }

}
