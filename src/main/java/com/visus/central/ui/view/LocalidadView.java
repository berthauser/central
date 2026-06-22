package com.visus.central.ui.view;

import java.util.List;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.visus.central.domain.model.Localidad;
import com.visus.central.domain.port.in.LocalidadUseCase;
import com.visus.central.ui.component.CentralLayout;
import com.visus.central.ui.component.LocalidadForm;

@Route(value = "localidades", layout = CentralLayout.class)
@PageTitle("Gestión de Localidades")
public class LocalidadView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final LocalidadUseCase localidadService;

	private final Grid<Localidad> grid = new Grid<>(Localidad.class, false);
	private final TextField filtro = new TextField();
	private final Button agregar = new Button("Nueva Localidad", new Icon(VaadinIcon.PLUS));
	private final Button inicio = new Button("Inicio", new Icon(VaadinIcon.HOME));

	private final LocalidadForm form;

	public LocalidadView(LocalidadUseCase localidadService) {
		this.localidadService = localidadService;

		form = new LocalidadForm();
		form.setVisible(false);

		configurarVista();
		configurarFiltro();
		configurarGrilla();
		configurarFormulario();
		configurarEventos();
		actualizarLista();
	}

	// Configura layout general
	private void configurarVista() {
		setSizeFull();
		setPadding(true);
		setSpacing(true);
		filtro.addClassName("campo-estilo-imagen");
		filtro.setWidth("250px");
		agregar.addClassName("btn-nuevo");
		inicio.addClassName("btn-volver-home");
		inicio.addClickListener(_ -> UI.getCurrent().navigate(HomeView.class));
		agregar.addClickListener(_ -> iniciarAlta());
		HorizontalLayout acciones = new HorizontalLayout(agregar, filtro, inicio);
		add(acciones, grid, form);
	}

	// Filtro por nombre
	private void configurarFiltro() {
		filtro.setPlaceholder("Buscar por nombre...");
		filtro.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
		filtro.setClearButtonVisible(true);
		filtro.addValueChangeListener(_ -> actualizarLista());
	}

	private void iniciarAlta() {
		form.setEntity(new Localidad());
		form.setVisible(true);
	}

	// Grilla institucional
	private void configurarGrilla() {
		grid.getColumns().forEach(col -> col.setAutoWidth(true));
		grid.setColumnReorderingAllowed(true);
		grid.setHeight("400px");
		grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		grid.addClassName("grid-documentacion-dark");
		grid.addColumn(Localidad::getNombre).setHeader("LOCALIDAD").setAutoWidth(true);
		grid.addColumn(Localidad::getCodigoPostal).setHeader("CÓDIGO POSTAL").setAutoWidth(true);
		grid.setSizeFull();
		grid.asSingleSelect().addValueChangeListener(e -> {
			Localidad seleccionada = e.getValue();
			if (seleccionada != null) {
				form.setEntity(seleccionada);
				form.setVisible(true);
			}
		});
	}

	// Formulario desacoplado
	private void configurarFormulario() {
		form.addClassName("form-panel");
		form.setVisible(false);
	}

	// Eventos del formulario
	private void configurarEventos() {
		form.addCreateListener(e -> {
			Localidad nueva = (Localidad) e.getEntity();
			localidadService.guardar(nueva);
			Notification.show("Localidad creada correctamente", 3000, Notification.Position.BOTTOM_CENTER)
					.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			actualizarLista();
			ocultarFormulario();
		});

		form.addUpdateListener(e -> {
			localidadService.guardar((Localidad) e.getEntity());
			actualizarLista();
			ocultarFormulario();
			Notification.show("Localidad modificada correctamente", 3000, Notification.Position.BOTTOM_CENTER)
					.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		});

		form.addDeleteListener(e -> {
			Localidad localidad = (Localidad) e.getEntity();

			if (localidad.getIdlocalidad() != null) {
				Dialog dialogo = new Dialog();
				dialogo.setHeaderTitle("Confirmar eliminación");

				Span mensaje = new Span("¿Está seguro de que desea eliminar la Localidad?");
				mensaje.getStyle().set("font-weight", "500");

				Button aceptar = new Button("Eliminar", _ -> {
					localidadService.eliminar(localidad.getIdlocalidad());
					actualizarLista();
					ocultarFormulario();
					Notification.show("Localidad eliminada", 3000, Notification.Position.BOTTOM_CENTER)
							.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					dialogo.close();
				});

				Button cancelar = new Button("Cancelar", _ -> dialogo.close());

				aceptar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
				cancelar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

				dialogo.getFooter().add(aceptar, cancelar);
				dialogo.add(mensaje);
				dialogo.open();
			} else {
				Notification.show("La localidad aún no fue guardada", 3000, Notification.Position.BOTTOM_CENTER)
						.addThemeVariants(NotificationVariant.LUMO_WARNING);
			}
		});

		form.addBackListener(_ -> ocultarFormulario());
		inicio.addClickListener(_ -> getUI().ifPresent(ui -> ui.navigate("")));
	}

	// Actualiza la grilla
	private void actualizarLista() {
		String texto = filtro.getValue() != null ? filtro.getValue().trim().toLowerCase() : "";
		List<Localidad> todas = localidadService.listar();
		List<Localidad> filtradas = todas.stream().filter(l -> l.getNombre().toLowerCase().contains(texto)).toList();
		grid.setItems(filtradas);
	}

	private void ocultarFormulario() {
		form.setVisible(false);
		grid.asSingleSelect().clear();
	}

}
