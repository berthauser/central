package com.visus.central.ui.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.visus.central.domain.model.User;
import com.visus.central.domain.port.in.UserUseCase;
import com.visus.central.infraestructure.security.SecurityUtils;
import com.visus.central.ui.component.CentralLayout;
import com.visus.central.ui.component.UserForm;

@Route(value = "usuarios", layout = CentralLayout.class)
@PageTitle("Gestión de Usuarios")
public class UsersView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private UserUseCase userService;
	private final Grid<User> grid = new Grid<>(User.class, false);
	private UserForm form;

	public UsersView(UserUseCase userService) {
		
		if (!SecurityUtils.isAdmin()) {
		    showAccessDeniedNotification();
		    UI.getCurrent().navigate("");
		    return;
		}
		
        form = new UserForm();
        form.setVisible(true);

        form.addCreateListener(e -> {
            userService.create(e.getUser());
            form.clearForm();
            refreshGrid();
        });

        form.addUpdateListener(e -> {
            userService.update(e.getUser());
            form.clearForm();
            refreshGrid();
        });

        form.addDeleteListener(e -> {
            userService.delete(e.getUser().getUsername());
            Notification.show("Usuario eliminado: " + e.getUser().getUsername());
            form.clearForm();
            refreshGrid();
        });

        form.addBackListener(_ -> UI.getCurrent().navigate("/"));

        grid.addColumn(User::getUsername).setHeader("Usuario");
        grid.addColumn(User::getRole).setHeader("Rol");
        grid.setItems(userService.findAll());

        grid.asSingleSelect().addValueChangeListener(event -> {
            User seleccionado = event.getValue();
            if (seleccionado != null) {
                form.setUser(seleccionado);
            }
        });

        add(new H2("Gestión de Usuarios"), form, grid);
    }

	private void showAccessDeniedNotification() {
	        Notification notification = new Notification();
	        notification.setText("Acceso denegado: esta sección es exclusiva para el usuario ADMIN.");
	        notification.setDuration(4000); // 4 segundos
	        notification.setPosition(Notification.Position.MIDDLE);
	        notification.open();
	    }

	private void refreshGrid() {
		grid.setItems(userService.findAll());
	}

}
