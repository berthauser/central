package com.visus.central.ui.view;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.visus.central.domain.model.PermisoVista;
import com.visus.central.domain.model.User;
import com.visus.central.domain.port.in.PermisoVistaUseCase;
import com.visus.central.domain.port.in.UserUseCase;
import com.visus.central.infraestructure.security.SecurityUtils;
import com.visus.central.infraestructure.util.ViewRegistry;
import com.visus.central.ui.component.CentralLayout;

@Route(value = "permisos", layout = CentralLayout.class)
@PageTitle("Configuraci\u00f3n de Permisos")
public class PermisosView extends VerticalLayout implements HasUrlParameter<Integer> {

	private static final long serialVersionUID = 1L;

	private final PermisoVistaUseCase permisoService;
	private final UserUseCase userService;
	private final ComboBox<User> userSelector = new ComboBox<>("Usuario");
	private final Grid<VistaRow> grid = new Grid<>(VistaRow.class, false);
	private final Map<Class<?>, String> vistas;

	private List<User> usersList = new ArrayList<>();
	private User usuarioSeleccionado;

	public PermisosView(PermisoVistaUseCase permisoService, UserUseCase userService) {
		this.permisoService = permisoService;
		this.userService = userService;
		this.vistas = new LinkedHashMap<>(ViewRegistry.getViewsMap());

		if (!SecurityUtils.isAdmin()) {
			Notification.show("Acceso denegado: solo ADMIN puede configurar permisos.",
					4000, Notification.Position.MIDDLE);
			UI.getCurrent().navigate("");
			return;
		}

		setPadding(true);
		setSpacing(true);

		add(new H2("Configuraci\u00f3n de Permisos por Usuario"));
		buildUserSelector();
		buildButtons();
		buildGrid();
		cargarUsuarios();
	}

	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter Integer userId) {
		if (userId != null && usersList != null) {
			usersList.stream()
					.filter(u -> u.getId().equals(userId))
					.findFirst()
					.ifPresent(u -> {
						usuarioSeleccionado = u;
						userSelector.setValue(u);
						cargarPermisos(u.getId());
					});
		}
	}

	private void cargarUsuarios() {
		usersList = userService.findAll();
		userSelector.setItems(usersList);
	}

	private void buildUserSelector() {
		userSelector.setItemLabelGenerator(User::getUsername);
		userSelector.setRequiredIndicatorVisible(true);
		userSelector.addClassName("campo-estilo-imagen");
		userSelector.setWidth("300px");
		userSelector.addValueChangeListener(e -> {
			usuarioSeleccionado = e.getValue();
			if (usuarioSeleccionado != null) {
				cargarPermisos(usuarioSeleccionado.getId());
			}
		});

		Button inicio = new Button("Inicio", new Icon(VaadinIcon.HOME));
		inicio.addClassName("btn-volver-home");
		inicio.addClickListener(_ -> UI.getCurrent().navigate(""));

		HorizontalLayout headerRow = new HorizontalLayout(userSelector, inicio);
		headerRow.setAlignItems(Alignment.END);
		add(headerRow);
	}

	private void buildGrid() {
		grid.addClassName("grid-documentacion-dark");
		grid.setHeight("450px");
		grid.setColumnReorderingAllowed(false);

		grid.addColumn(VistaRow::getNombre).setHeader("VISTA").setAutoWidth(true).setFlexGrow(1);

		grid.addComponentColumn(row -> {
			Checkbox check = new Checkbox(row.isVisible());
			check.addValueChangeListener(e -> row.setVisible(e.getValue()));
			return check;
		}).setHeader("VISIBLE").setAutoWidth(true);

		add(grid);
	}

	private void buildButtons() {
		Button guardar = new Button("Guardar", new Icon(VaadinIcon.CHECK));
		guardar.addClassName("btn-grabar");
		guardar.addClickListener(_ -> guardarPermisos());

		Button habilitarTodas = new Button("Habilitar todas");
		habilitarTodas.addClassName("btn-nuevo");
		habilitarTodas.addClickListener(_ -> {
			grid.getListDataView().getItems().forEach(row -> row.setVisible(true));
			grid.getDataProvider().refreshAll();
		});

		Button deshabilitarTodas = new Button("Deshabilitar todas");
		deshabilitarTodas.addClassName("btn-eliminar");
		deshabilitarTodas.addClickListener(_ -> {
			grid.getListDataView().getItems().forEach(row -> row.setVisible(false));
			grid.getDataProvider().refreshAll();
		});

		HorizontalLayout botones = new HorizontalLayout(guardar, habilitarTodas, deshabilitarTodas);
		botones.setAlignItems(Alignment.CENTER);

		add(botones);
	}

	private void cargarPermisos(Integer userId) {
		List<String> classNames = vistas.keySet().stream()
				.map(c -> c != null ? c.getName() : "")
				.toList();

		Map<String, Boolean> permisos = permisoService.getMapaPermisos(userId, classNames);

		List<VistaRow> rows = vistas.entrySet().stream()
				.map(e -> {
					Class<?> viewClass = e.getKey();
					String className = viewClass != null ? viewClass.getName() : "";
					String nombreVista = e.getValue();
					boolean visible = permisos.getOrDefault(className, true);
					return new VistaRow(viewClass, nombreVista, visible);
				})
				.toList();

		grid.setItems(rows);
	}

	private void guardarPermisos() {
		if (usuarioSeleccionado == null) {
			Notification.show("Seleccione un usuario primero.", 3000, Notification.Position.MIDDLE);
			return;
		}

		List<PermisoVista> permisos = grid.getListDataView().getItems()
				.map(row -> new PermisoVista(usuarioSeleccionado.getId(),
						row.getViewClass() != null ? row.getViewClass().getName() : "",
						row.isVisible()))
				.collect(Collectors.toList());

		permisoService.guardarTodos(permisos);
		Notification.show("Permisos guardados para " + usuarioSeleccionado.getUsername(),
				3000, Notification.Position.BOTTOM_CENTER);
	}

	public static class VistaRow {
		private final Class<?> viewClass;
		private final String nombre;
		private boolean visible;

		public VistaRow(Class<?> viewClass, String nombre, boolean visible) {
			this.viewClass = viewClass;
			this.nombre = nombre;
			this.visible = visible;
		}

		public Class<?> getViewClass() { return viewClass; }
		public String getNombre() { return nombre; }
		public boolean isVisible() { return visible; }
		public void setVisible(boolean visible) { this.visible = visible; }
	}
}
