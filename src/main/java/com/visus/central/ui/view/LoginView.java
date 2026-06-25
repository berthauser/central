package com.visus.central.ui.view;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.visus.central.domain.port.in.UserUseCase;

@Route("login")
@PageTitle("Iniciar sesión")
public class LoginView extends LoginOverlay {

	private static final long serialVersionUID = 1L;

	public LoginView(UserUseCase userService) {
		LoginI18n i18n = new LoginI18n();
		LoginI18n.Header header = new LoginI18n.Header();
		header.setTitle("");
		header.setDescription("Iniciar sesión");
		i18n.setHeader(header);

		LoginI18n.Form form = new LoginI18n.Form();
		form.setUsername("Usuario");
		form.setPassword("Contraseña");
		form.setSubmit("Ingresar");
		i18n.setForm(form);

		LoginI18n.ErrorMessage error = new LoginI18n.ErrorMessage();
		error.setTitle("Credenciales inválidas");
		error.setMessage("Verifique su usuario y contraseña");
		i18n.setErrorMessage(error);
		setI18n(i18n);

		setForgotPasswordButtonVisible(false);

		// Branding: icon + text
		Icon appIcon = VaadinIcon.CUBES.create();
		appIcon.setSize("48px");
		appIcon.getStyle().set("color", "var(--lumo-primary-color)");

		Span titleText = new Span("Visus Central");
		titleText.getStyle().set("font-size", "24px").set("font-weight", "bold");

		Div branding = new Div(appIcon, titleText);
		branding.getStyle().set("display", "flex").set("align-items", "center").set("gap", "12px")
				.set("justify-content", "center").set("padding", "20px 0");

		// Attach to load frontend deps (vaadin-icon + iconset)
		getElement().appendChild(branding.getElement());

		Paragraph copyright = new Paragraph("©Grupo Dignitas");
		copyright.addClassName(LumoUtility.TextAlignment.CENTER);
		copyright.getStyle().set("font-size", "0.85rem").set("color", "var(--lumo-secondary-text-color)");
		getFooter().add(copyright);

		addLoginListener(event -> {
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

				close();
				UI.getCurrent().navigate("");
			} else {
				setError(true);
			}
		});

		addAttachListener(_ -> {
			setOpened(true);
			String html = branding.getElement().getOuterHTML();
			getElement().executeJs(
				"var w = this.shadowRoot.querySelector('vaadin-login-overlay-wrapper');" +
				"if (w && w.shadowRoot && !w.shadowRoot.querySelector('[data-brand]')) {" +
				"  var div = document.createElement('div');" +
				"  div.setAttribute('data-brand', '');" +
				"  div.innerHTML = $0;" +
				"  var brand = w.shadowRoot.querySelector('section[part=\"brand\"]');" +
				"  if (brand) {" +
				"    brand.insertBefore(div.firstElementChild, brand.firstChild);" +
				"    brand.querySelector('slot[name=\"title\"]').style.display = 'none';" +
				"  }" +
				"}",
				html
			);
		});
	}
}
