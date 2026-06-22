package com.visus.central.ui.view;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.visus.central.domain.model.User;
import com.visus.central.domain.port.in.PasswordChangeUseCase;
import com.visus.central.domain.port.in.UserUseCase;
import com.visus.central.infraestructure.security.SecurityUtils;
import com.visus.central.ui.component.CentralLayout;
import com.visus.central.ui.component.ChangePasswordForUserDialog;
import com.visus.central.ui.component.UserForm;

@Route(value = "usuarios", layout = CentralLayout.class)
@PageTitle("Gesti\u00f3n de Usuarios")
public class UsersView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final UserUseCase userService;
	private final PasswordChangeUseCase passwordChangeUseCase;
	private final Grid<User> grid = new Grid<>(User.class, false);
	private final UserForm form;
	private List<User> usersList = new ArrayList<>();

	public UsersView(UserUseCase userService, PasswordChangeUseCase passwordChangeUseCase) {
		this.userService = userService;
		this.passwordChangeUseCase = passwordChangeUseCase;

		form = new UserForm();

		if (!SecurityUtils.isAdmin()) {
			showAccessDeniedNotification();
			UI.getCurrent().navigate("");
			return;
		}

		form.setVisible(false);

		form.addCreateListener(e -> {
			userService.create(e.getUser());
			form.clearForm();
			form.setVisible(false);
			refreshGrid();
			showSuccess("Usuario creado");
		});

		form.addUpdateListener(e -> {
			userService.update(e.getUser());
			form.clearForm();
			form.setVisible(false);
			refreshGrid();
			showSuccess("Usuario actualizado");
		});

		form.addDeleteListener(e -> {
			userService.delete(e.getUser().getUsername());
			form.clearForm();
			form.setVisible(false);
			refreshGrid();
			showSuccess("Usuario eliminado: " + e.getUser().getUsername());
		});

		form.addBackListener(_ -> {
			form.clearForm();
			form.setVisible(false);
		});

		add(new H2("Gesti\u00f3n de Usuarios"), buildHeader(), grid, form);
		configurarGrilla();
		refreshGrid();
	}

	private HorizontalLayout buildHeader() {
		Button nuevo = new Button("Nuevo", new Icon(VaadinIcon.PLUS));
		nuevo.addClassName("btn-nuevo");
		nuevo.addClickListener(_ -> {
			form.clearForm();
			form.setVisible(true);
		});

		TextField filtro = new TextField();
		filtro.setPlaceholder("Buscar por usuario...");
		filtro.setClearButtonVisible(true);
		filtro.addClassName("campo-estilo-imagen");
		filtro.addValueChangeListener(e -> {
			String criterio = e.getValue();
			if (criterio == null || criterio.isBlank()) {
				grid.setItems(usersList);
			} else {
				grid.setItems(usersList.stream()
						.filter(u -> u.getUsername().toLowerCase().contains(criterio.toLowerCase()))
						.toList());
			}
		});

		Button inicio = new Button("Inicio", new Icon(VaadinIcon.HOME));
		inicio.addClassName("btn-volver-home");
		inicio.addClickListener(_ -> UI.getCurrent().navigate(""));

		HorizontalLayout header = new HorizontalLayout(nuevo, filtro, inicio);
		header.setAlignItems(Alignment.CENTER);
		return header;
	}

	private void configurarGrilla() {
		grid.addClassName("grid-documentacion-dark");
		grid.setHeight("400px");
		grid.setColumnReorderingAllowed(true);

		grid.addColumn(User::getUsername).setHeader("USUARIO").setAutoWidth(true);
		grid.addColumn(User::getRole).setHeader("ROL").setAutoWidth(true);

		grid.addComponentColumn(user -> {
			HorizontalLayout actions = new HorizontalLayout();
			actions.setSpacing(true);

			Button changePwdBtn = new Button("Cambiar pwd", _ -> {
				new ChangePasswordForUserDialog(user, passwordChangeUseCase).open();
			});
			changePwdBtn.addClassName("btn-nuevo");
			actions.add(changePwdBtn);

			Button permisosBtn = new Button("Permisos", _ -> {
				UI.getCurrent().navigate("permisos/" + user.getId());
			});
			permisosBtn.addClassName("btn-nuevo");
			actions.add(permisosBtn);

			return actions;
		}).setHeader("ACCI\u00d3N").setAutoWidth(true);

		grid.asSingleSelect().addValueChangeListener(event -> {
			User seleccionado = event.getValue();
			if (seleccionado != null) {
				form.setUser(seleccionado);
				form.setVisible(true);
				grid.setHeight("300px");
			}
		});
	}

	private void refreshGrid() {
		usersList = userService.findAll();
		grid.setItems(usersList);
		grid.setHeight("400px");
	}

	private void showAccessDeniedNotification() {
		Notification.show("Acceso denegado: esta secci\u00f3n es exclusiva para el usuario ADMIN.",
				4000, Notification.Position.MIDDLE);
	}

	private void showSuccess(String message) {
		Notification.show(message, 3000, Notification.Position.BOTTOM_CENTER);
	}
}
