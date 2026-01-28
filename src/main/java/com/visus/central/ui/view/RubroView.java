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
import com.visus.central.domain.model.Rubro;
import com.visus.central.domain.port.in.LineaUseCase;
import com.visus.central.domain.port.in.RubroUseCase;
import com.visus.central.ui.component.CentralLayout;
import com.visus.central.ui.component.RubroForm;

@Route(value = "rubros", layout = CentralLayout.class)
@PageTitle("Gestión de Rubros para Artículos")
public class RubroView extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	
	private final RubroUseCase rubroService;
	private final LineaUseCase lineaService;
	private final Grid<Rubro> grid = new Grid<>(Rubro.class, false);
	private final TextField filtro = new TextField();
	private final RubroForm form = new RubroForm();
	private final Button nuevo = new Button("Nuevo Rubro", new Icon(VaadinIcon.PLUS));
	private final Button inicio = new Button("Inicio", new Icon(VaadinIcon.HOME));
	
	@Autowired
	public RubroView(RubroUseCase rubroService, LineaUseCase lineaService) {
        this.rubroService = rubroService;
        this.lineaService = lineaService;
        
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
        grid.addColumn(Rubro::getDescripcion).setHeader("RUBRO");
        grid.setSizeFull();

        grid.asSingleSelect().addValueChangeListener(e -> {
            Rubro seleccionado = e.getValue();
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
            Rubro nuevo = (Rubro) e.getEntity();
            rubroService.guardar(nuevo);
            Notification.show("Rubro creado", 3000, Notification.Position.BOTTOM_CENTER)
            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            actualizarLista();
            form.setVisible(false);
        });

        form.addUpdateListener(e -> {
        	Rubro modificado = (Rubro) e.getEntity();
        	rubroService.guardar(modificado);
        	Notification.show("Rubro modificado", 3000, Notification.Position.BOTTOM_CENTER)
        	.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        	actualizarLista();
        	form.setVisible(false);
        });

        form.addDeleteListener(e -> {
        	Rubro rubro = (Rubro) e.getEntity();
           	if (lineaService.tieneRubrosAsociados(rubro.getId())) {
                mostrarConfirmacionConDependencias(rubro);
            } else {
                mostrarConfirmacionSinDependencias(rubro);
            }
        });
        
        form.addBackListener(_ -> form.setVisible(false));
    }

    // Alta institucional
    private void iniciarAlta() {
        Rubro nuevoRubro = new Rubro();
        nuevoRubro.setDescripcion(""); // valor por defecto
        form.setEntity(nuevoRubro);
        form.setVisible(true);
    }

    // Actualiza la grilla
    private void actualizarLista() {
        String texto = filtro.getValue();
        List<Rubro> lista = (texto == null || texto.isEmpty())
            ? rubroService.listar()
            : rubroService.listar().stream()
                .filter(rubro -> rubro.getDescripcion().toLowerCase().contains(texto.toLowerCase()))
                .collect(Collectors.toList());
        grid.setItems(lista);
    }
    
    private void mostrarConfirmacionConDependencias(Rubro rubro) {
        Dialog dialogo = new Dialog();
        dialogo.setHeaderTitle("Confirmar eliminación");

        Span mensaje = new Span("El Rubro tiene Líneas asociadas. ¿Desea eliminarlo igualmente?");
        mensaje.getStyle().set("font-weight", "500");

        Button aceptar = new Button("Eliminar", _ -> {
            rubroService.eliminar(rubro.getId());
            actualizarLista();
            form.setVisible(false);
            Notification.show("Rubro eliminado", 3000, Notification.Position.BOTTOM_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            dialogo.close();
        });

        Button cancelar = new Button("Cancelar", _ -> dialogo.close());

        aceptar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        dialogo.getFooter().add(aceptar, cancelar);
        dialogo.add(mensaje);
        dialogo.open();
    }
    
    private void mostrarConfirmacionSinDependencias(Rubro rubro) {
        Dialog dialogo = new Dialog();
        dialogo.setHeaderTitle("Confirmar eliminación");

        Span mensaje = new Span("¿Está seguro de que desea eliminar el Rubro?");
        mensaje.getStyle().set("font-weight", "500");

        Button aceptar = new Button("Eliminar", _ -> {
            rubroService.eliminar(rubro.getId());
            actualizarLista();
            form.setVisible(false);
            Notification.show("Rubro eliminado", 3000, Notification.Position.BOTTOM_CENTER)
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