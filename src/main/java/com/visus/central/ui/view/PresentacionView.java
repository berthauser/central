package com.visus.central.ui.view;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
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
import com.visus.central.domain.model.Presentacion;
import com.visus.central.domain.port.in.PresentacionUseCase;
import com.visus.central.ui.component.CentralLayout;
import com.visus.central.ui.component.PresentacionForm;

@Route(value = "presentaciones", layout = CentralLayout.class)
@PageTitle("Gestión de Presentaciones para Artículos")
public class PresentacionView extends VerticalLayout {
	
	private static final long serialVersionUID = 1L;

	private final PresentacionUseCase presentacionService;
	private final Grid<Presentacion> grid = new Grid<>(Presentacion.class, false);
	private final TextField filtro = new TextField();
	private final PresentacionForm form = new PresentacionForm();
	private final Button nuevo = new Button("Nueva Presentación", new Icon(VaadinIcon.PLUS));
	private final Button inicio = new Button("Inicio", new Icon(VaadinIcon.HOME));

	@Autowired
	public PresentacionView(PresentacionUseCase presentacionService) {
		this.presentacionService = presentacionService;

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
		nuevo.addClassName("btn-nuevo");
		inicio.addClassName("btn-volver-home");
		inicio.addClickListener(_ -> UI.getCurrent().navigate(HomeView.class));
		nuevo.addClickListener(_ -> iniciarAlta());
		HorizontalLayout acciones = new HorizontalLayout(nuevo, filtro, inicio);
		add(acciones, grid, form);
	}

	// Filtro por nombre
	private void configurarFiltro() {
		filtro.setPlaceholder("Buscar por nombre...");
		filtro.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
		filtro.setClearButtonVisible(true);
		filtro.addValueChangeListener(_ -> actualizarLista());
	}

	// Grilla institucional
	private void configurarGrilla() {
		grid.getColumns().forEach(col -> col.setAutoWidth(true));
		grid.setColumnReorderingAllowed(true);
		grid.setHeight("400px");
		grid.addClassName("grid-documentacion-dark");
		grid.addColumn(Presentacion::getDescripcion).setHeader("PRESENTACIÓN");
		grid.setSizeFull();

		grid.asSingleSelect().addValueChangeListener(e -> {
			Presentacion seleccionado = e.getValue();
			if (seleccionado != null) {
				form.setEntity(seleccionado);
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
			Presentacion nuevo = (Presentacion) e.getEntity();
			presentacionService.guardar(nuevo);
			Notification.show("Presentación creada", 3000, Notification.Position.BOTTOM_CENTER)
			.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			actualizarLista();
			form.setVisible(false);
		});

		form.addUpdateListener(e -> {
			Presentacion modificado = (Presentacion) e.getEntity();
			presentacionService.guardar(modificado);
			Notification.show("Presentación modificada", 3000, Notification.Position.BOTTOM_CENTER)
			.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			actualizarLista();
			form.setVisible(false);
		});

		form.addDeleteListener(e -> {
			Presentacion presentacion = (Presentacion) e.getEntity();
			mostrarConfirmacionSinDependencias(presentacion);
		});

		form.addBackListener(_ -> form.setVisible(false));
	}

	// Alta institucional
	private void iniciarAlta() {
		Presentacion nuevaPresentacion = new Presentacion();
		nuevaPresentacion.setDescripcion(""); // valor por defecto
		form.setEntity(nuevaPresentacion);
		form.setVisible(true);
	}

	// Actualiza la grilla
	private void actualizarLista() {
		String texto = filtro.getValue();
		List<Presentacion> lista = (texto == null || texto.isEmpty())
				? presentacionService.listar()
						: presentacionService.listar().stream()
						.filter(presentacion -> presentacion.getDescripcion().toLowerCase().contains(texto.toLowerCase()))
						.collect(Collectors.toList());
		grid.setItems(lista);
	}

	private void mostrarConfirmacionSinDependencias(Presentacion presentacion) {
		Dialog dialogo = new Dialog();
		dialogo.setHeaderTitle("Confirmar eliminación");

		Span mensaje = new Span("¿Está seguro de que desea eliminar la Presentación?");
		mensaje.getStyle().set("font-weight", "500");

		Button aceptar = new Button("Eliminar", _ -> {
			presentacionService.eliminar(presentacion.getId());
			actualizarLista();
			form.setVisible(false);
			Notification.show("Presentación eliminada", 3000, Notification.Position.BOTTOM_CENTER)
			.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			dialogo.close();
		});

		Button cancelar = new Button("Cancelar", _ -> dialogo.close());

		aceptar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		cancelar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		dialogo.getFooter().add(aceptar, cancelar);
		dialogo.add(mensaje);
		dialogo.open();
	}

}
