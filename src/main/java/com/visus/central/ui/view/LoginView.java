package com.visus.central.ui.view;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.server.VaadinSession;
import com.visus.central.domain.port.in.UserUseCase;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
		usernameField.setPlaceholder("Ingrese usuario");
		usernameField.focus();
		PasswordField passwordField = new PasswordField("Contraseña");
		passwordField.setPlaceholder("Ingrese contraseña");
		Button loginButton = new Button("Ingresar");
		
		usernameField.addClassName("campo-estilo-imagen");
		passwordField.addClassName("campo-estilo-imagen");
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
				
				 // Obtener el rol real del usuario
		        String role = userService.getRoleFor(usernameField.getValue()); // "ROLE_ADMIN", "ROLE_USER", etc.

		        Authentication auth = new UsernamePasswordAuthenticationToken(
		            usernameField.getValue(),
		            null,
		            List.of(new SimpleGrantedAuthority(role))
		        );

		        // 🔐 Persistencia manual del contexto de seguridad
		        SecurityContext context = SecurityContextHolder.createEmptyContext();
		        context.setAuthentication(auth);
		        SecurityContextHolder.setContext(context);

		        HttpServletRequest request = ((VaadinServletRequest) VaadinService.getCurrentRequest()).getHttpServletRequest();
		        HttpServletResponse response = ((VaadinServletResponse) VaadinService.getCurrentResponse()).getHttpServletResponse();
		        new HttpSessionSecurityContextRepository().saveContext(context, request, response);

		        // 🔐 También guardás en sesión Vaadin si lo necesitás para componentes visuales
		        VaadinSession session = VaadinSession.getCurrent();
		        session.setAttribute("username", usernameField.getValue());
		        session.setAttribute("role", role);

		        // 🔀 Redirección
		        UI.getCurrent().navigate("/");

				
				
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
	    feedback.setText("Sin credenciales");
	    feedback.removeClassName("error");
	}
}