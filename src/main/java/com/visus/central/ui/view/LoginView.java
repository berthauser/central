package com.visus.central.ui.view;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinSession;
import com.visus.central.domain.port.in.UserUseCase;

@Route("login")
@PageTitle("Iniciar sesión")
public class LoginView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	public LoginView(UserUseCase userService) {
		
		Image avatar = new Image("images/avatar.png", "Usuario");
		avatar.setWidth("64px");
		avatar.setHeight("64px");
		avatar.getStyle().set("border-radius", "50%"); // círculo perfecto
		avatar.getStyle().set("margin-bottom", "var(--lumo-space-s)");
		
		H2 titulo = new H2("Login");
		
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);
		setHeightFull();

		TextField usernameField = new TextField("Usuario");
		usernameField.setPlaceholder("Ingrese Usuario");
		usernameField.focus();
		PasswordField passwordField = new PasswordField("Contraseña");
		passwordField.setPlaceholder("Ingrese Contraseña");
		Button loginButton = new Button("Ingresar");

		usernameField.addKeyPressListener(Key.ENTER, _ -> passwordField.focus());
		passwordField.addKeyPressListener(Key.ENTER, _ -> loginButton.focus());
		loginButton.getElement().addEventListener("keydown", _ -> loginButton.click())
				.setFilter("event.key === 'Enter'");

		usernameField.addClassName("campo-estilo-imagen");
		passwordField.addClassName("campo-estilo-imagen");

		injectAutofillFix(usernameField);
		injectAutofillFix(passwordField);
		loginButton.addClassName("btn-login");
		
		// Mensaje dinámico de error
        Span feedback = new Span("Sin Credenciales");
        feedback.addClassName("mensaje-feedback");

        // Línea divisoria
        Hr linea = new Hr();
        linea.addClassName("linea-divisoria");

        // Texto institucional
        Span copyright = new Span("©Grupo Dignitas");
        copyright.addClassName("copyright-text");
        
     // Pie del login con espaciado mínimo
        VerticalLayout pieLogin = new VerticalLayout(
            linea, // línea divisoria
            copyright,
            feedback // feedback dinámico
        );
        
        pieLogin.setSpacing(false);
        pieLogin.setPadding(false);
        pieLogin.setMargin(false);
        pieLogin.getStyle().set("margin-top", "0.2rem"); // espaciado mínimo desde el botón
        
		usernameField.addValueChangeListener(_ -> resetFeedback(feedback));
		
		passwordField.addValueChangeListener(_ -> resetFeedback(feedback));

		loginButton.addClickListener(_ -> {
			
			boolean success = userService.validateCredentials(
					usernameField.getValue(),
					passwordField.getValue()
					);
			
			if (success) {
			    String role = userService.getRoleFor(usernameField.getValue());
			    if (!role.startsWith("ROLE_")) {
			        role = "ROLE_" + role;
			    }

			    Authentication auth = new UsernamePasswordAuthenticationToken(
			        usernameField.getValue(), null,
			        List.of(new SimpleGrantedAuthority(role))
			    );

			    SecurityContext context = SecurityContextHolder.createEmptyContext();
			    context.setAuthentication(auth);
			    SecurityContextHolder.setContext(context);

			    // Persistir en sesión HTTP manualmente
			    VaadinServletRequest request = (VaadinServletRequest) VaadinService.getCurrentRequest();
			    if (request != null) {
			        request.getHttpServletRequest().getSession().setAttribute(
			            "SPRING_SECURITY_CONTEXT", context);
			    }

			    // Guardar en sesión Vaadin
			    VaadinSession session = VaadinSession.getCurrent();
			    session.setAttribute("username", usernameField.getValue());
			    session.setAttribute("role", role);

			    UI.getCurrent().navigate("");
			} else {
			    feedback.setText("Credenciales inválidas");
			    feedback.addClassName("error");
			}
		});
		
		// Layout interno con clase CSS personalizada
        VerticalLayout formulario = new VerticalLayout(
            usernameField,
            passwordField,
            loginButton,
            pieLogin
        );
        
        formulario.addClassName("login-form");
        formulario.setPadding(true);
        formulario.setSpacing(true);
        formulario.setWidth("320px");
        formulario.setAlignItems(Alignment.STRETCH);

        add(avatar, titulo, formulario);
	}
	
	private void resetFeedback(Span feedback) {
	    feedback.setText("Sin Credenciales");
	    feedback.removeClassName("error");
	}

	private void injectAutofillFix(Component field) {
		field.getElement().executeJs(
			"var inp = this.inputElement;" +
			"if (inp) {" +
			"  var root = inp.getRootNode();" +
			"  if (root instanceof ShadowRoot && !root.querySelector('[data-autofill]')) {" +
			"    var style = document.createElement('style');" +
			"    style.setAttribute('data-autofill', '');" +
			"    style.textContent = 'input:-webkit-autofill,input:-webkit-autofill:hover,input:-webkit-autofill:focus{-webkit-box-shadow:0 0 0 1000px #000 inset !important;-webkit-text-fill-color:#fff !important;caret-color:#fff !important;color:#fff !important}';" +
			"    root.appendChild(style);" +
			"  }" +
			"}"
		);
	}
}