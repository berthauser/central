package com.visus.central.ui.view;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.visus.central.domain.port.in.UserUseCase;

@Route("login")
@PageTitle("Iniciar sesión")
public class LoginView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	public LoginView(UserUseCase userService) {
		LoginOverlay login = new LoginOverlay();

		LoginI18n i18n = LoginI18n.createDefault();
		i18n.getHeader().setTitle("Visus Central");
		i18n.getHeader().setDescription("Iniciar sesión");
		i18n.getForm().setUsername("Usuario");
		i18n.getForm().setPassword("Contraseña");
		i18n.getForm().setSubmit("Ingresar");
		i18n.getErrorMessage().setTitle("Credenciales inválidas");
		i18n.getErrorMessage().setMessage("Verifique su usuario y contraseña");
		login.setI18n(i18n);

		login.setForgotPasswordButtonVisible(false);

		Paragraph copyright = new Paragraph("©Grupo Dignitas");
		copyright.addClassName(LumoUtility.TextAlignment.CENTER);
		copyright.getStyle().set("font-size", "0.85rem").set("color", "var(--lumo-secondary-text-color)");
		login.getFooter().add(copyright);

		login.addLoginListener(event -> {
			boolean success = userService.validateCredentials(event.getUsername(), event.getPassword());

			if (success) {
				String role = userService.getRoleFor(event.getUsername());
				if (!role.startsWith("ROLE_")) {
					role = "ROLE_" + role;
				}

				Authentication auth = new UsernamePasswordAuthenticationToken(event.getUsername(), null,
						List.of(new SimpleGrantedAuthority(role)));

				SecurityContext context = SecurityContextHolder.createEmptyContext();
				context.setAuthentication(auth);
				SecurityContextHolder.setContext(context);

				VaadinServletRequest request = (VaadinServletRequest) VaadinService.getCurrentRequest();
				if (request != null) {
					request.getHttpServletRequest().getSession().setAttribute("SPRING_SECURITY_CONTEXT", context);
				}

				VaadinSession session = VaadinSession.getCurrent();
				session.setAttribute("username", event.getUsername());
				session.setAttribute("role", role);

				login.close();
				UI.getCurrent().navigate("");
			} else {
				login.setError(true);
			}
		});

		login.setOpened(true);

		add(login);
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);
	}
}
