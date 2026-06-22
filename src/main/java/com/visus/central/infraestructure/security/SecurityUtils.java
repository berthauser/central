package com.visus.central.infraestructure.security;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.server.VaadinSession;

public final class SecurityUtils {

	private SecurityUtils() {
	}

	public static String getUsername() {
		// Primero intentar con SecurityContextHolder
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
			return auth.getName();
		}
		// Fallback a VaadinSession
		VaadinSession session = VaadinSession.getCurrent();
		if (session != null) {
			return (String) session.getAttribute("username");
		}
		return null;
	}

	public static String getRole() {
		// Primero intentar con SecurityContextHolder
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated()) {
			Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
			String role = authorities.stream().map(GrantedAuthority::getAuthority)
					.filter(r -> r.startsWith("ROLE_")).findFirst().orElse(null);
			if (role != null) {
				return role;
			}
		}
		// Fallback a VaadinSession
		VaadinSession session = VaadinSession.getCurrent();
		if (session != null) {
			String role = (String) session.getAttribute("role");
			if (role != null && !role.startsWith("ROLE_")) {
				role = "ROLE_" + role;
			}
			return role;
		}
		return null;
	}

	public static boolean hasRole(String role) {
		String currentRole = getRole();
		return currentRole != null && currentRole.equals(role);
	}

	public static boolean isAdmin() {
		return hasRole("ROLE_ADMIN");
	}

	public static boolean isAuthenticated() {
		// Primero intentar con SecurityContextHolder
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
			return true;
		}
		// Fallback a VaadinSession
		VaadinSession session = VaadinSession.getCurrent();
		return session != null && session.getAttribute("username") != null;
	}

}
