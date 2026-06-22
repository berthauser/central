package com.visus.central.infraestructure.security;

import org.springframework.stereotype.Component;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.visus.central.domain.model.User;
import com.visus.central.domain.port.in.PermisoVistaUseCase;
import com.visus.central.domain.port.in.UserUseCase;
import com.visus.central.infraestructure.util.SpringContextBridge;
import com.visus.central.infraestructure.util.ViewRegistry;
import com.visus.central.ui.view.AccesoDenegadoView;
import com.visus.central.ui.view.LoginView;

@Component
public class RouteGuardListener implements VaadinServiceInitListener {

	private static final long serialVersionUID = 1L;

	@Override
	public void serviceInit(ServiceInitEvent event) {
		event.getSource().addUIInitListener(uiEvent -> {
			uiEvent.getUI().addBeforeEnterListener(this::checkAccess);
		});
	}

	private void checkAccess(BeforeEnterEvent event) {
		Class<?> target = event.getNavigationTarget();

		if (target == null || ViewRegistry.isPublica(target)) {
			return;
		}

		if (!SecurityUtils.isAuthenticated()) {
			event.forwardTo(LoginView.class);
			return;
		}

		String username = SecurityUtils.getUsername();
		if (username == null) {
			event.forwardTo(AccesoDenegadoView.class);
			return;
		}

		UserUseCase userUseCase = SpringContextBridge.getBean(UserUseCase.class);
		Integer userId = userUseCase.buscarPorUsername(username)
				.map(User::getId)
				.orElse(null);
		if (userId == null) {
			event.forwardTo(AccesoDenegadoView.class);
			return;
		}

		PermisoVistaUseCase permisoService = SpringContextBridge.getBean(PermisoVistaUseCase.class);
		if (!permisoService.puedeVer(userId, target.getName())) {
			event.forwardTo(AccesoDenegadoView.class);
		}
	}
}
