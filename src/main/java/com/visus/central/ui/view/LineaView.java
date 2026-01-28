package com.visus.central.ui.view;

import java.util.List;

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
import com.visus.central.domain.model.Linea;
import com.visus.central.domain.port.in.LineaUseCase;
import com.visus.central.domain.port.in.RubroUseCase;
import com.visus.central.ui.component.CentralLayout;
import com.visus.central.ui.component.LineaForm;

@Route(value = "lineas", layout = CentralLayout.class)
@PageTitle("Gestión de Líneas de Artículos")
public class LineaView extends VerticalLayout {
	
private static final long serialVersionUID = 1L;
	
	private final LineaUseCase lineaUseCase;
    private final RubroUseCase rubroUseCase;

    private final Grid<Linea> grid = new Grid<>(Linea.class, false);
    private final TextField filtro = new TextField();
    private final Button agregar = new Button("Nueva Línea", new Icon(VaadinIcon.PLUS));
	private final Button inicio = new Button("Inicio", new Icon(VaadinIcon.HOME));

    private final LineaForm lineaForm;

    public LineaView(LineaUseCase lineaUseCase, RubroUseCase rubroUseCase) {
    		this.lineaUseCase = lineaUseCase;
    		this.rubroUseCase = rubroUseCase;

        lineaForm = new LineaForm(rubroUseCase.listar());
        lineaForm.setVisible(false);

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
        agregar.addClassName("btn-nuevo");
        inicio.addClassName("btn-volver-home");
    	inicio.addClickListener(_ -> UI.getCurrent().navigate(HomeView.class));
        agregar.addClickListener(_ -> iniciarAlta());
        HorizontalLayout acciones = new HorizontalLayout(agregar, filtro, inicio);
        add(acciones, grid, lineaForm);
    }
    
    // Filtro por nombre
    private void configurarFiltro() {
        filtro.setPlaceholder("Buscar por nombre...");
        filtro.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        filtro.setClearButtonVisible(true);
        filtro.addValueChangeListener(_ -> actualizarLista());
    }
    
    // Alta institucional
    private void iniciarAlta() {
        Linea nuevaLinea = new Linea();
        // Opcional: asignar un rubro por defecto
        if (!rubroUseCase.listar().isEmpty()) {
            nuevaLinea.setRubro(rubroUseCase.listar().get(0));
        }
        lineaForm.setEntity(nuevaLinea);
        lineaForm.setVisible(true);
    }
    
    // Grilla institucional    
    private void configurarGrilla() {
    	grid.getColumns().forEach(col -> col.setAutoWidth(true));
    	grid.setColumnReorderingAllowed(true);
    	grid.setHeight("400px");
    	grid.addClassName("grid-documentacion-dark");
        grid.addColumn(Linea::getDescripcion).setHeader("LÍNEA").setAutoWidth(true);
        grid.addColumn(l -> l.getRubro() != null ? l.getRubro().getDescripcion() : "")
            .setHeader("RUBRO").setAutoWidth(true);
        grid.setSizeFull();
        grid.asSingleSelect().addValueChangeListener(e -> {
            Linea seleccionada = e.getValue();
            if (seleccionada != null) {
                lineaForm.setEntity(seleccionada);
                lineaForm.setVisible(true);
            }
        });
    }
    
    // Formulario desacoplado
    private void configurarFormulario() {
    	lineaForm.addClassName("form-panel");
    	lineaForm.setVisible(false);
    }
    
    // Eventos del formulario
    private void configurarEventos() {
    	lineaForm.addCreateListener(e -> {
            Linea nueva = (Linea) e.getEntity();
    		lineaUseCase.guardar(nueva);
    		Notification.show("Línea creada correctamente", 3000, Notification.Position.BOTTOM_CENTER)
    		.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            actualizarLista();
            ocultarFormulario();
        });
    	
    	lineaForm.addUpdateListener(e -> {
            lineaUseCase.guardar((Linea) e.getEntity());
            actualizarLista();
            ocultarFormulario();
            Notification.show("Línea modificada correctamente", 3000, Notification.Position.BOTTOM_CENTER)
        	.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });

    	lineaForm.addDeleteListener(e -> {
            Linea linea = (Linea) e.getEntity();
            
            if (linea.getIdLinea() != null) {
            	Dialog dialogo = new Dialog();
                dialogo.setHeaderTitle("Confirmar eliminación");

                Span mensaje = new Span("¿Está seguro de que desea eliminar la Línea?");
                mensaje.getStyle().set("font-weight", "500");

                Button aceptar = new Button("Eliminar", _ -> {
                	lineaUseCase.eliminar(linea.getIdLinea());
                    actualizarLista();
                    ocultarFormulario();
                    Notification.show("Línea eliminada", 3000, Notification.Position.BOTTOM_CENTER)
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
                Notification.show("La línea aún no fue guardada", 3000, Notification.Position.BOTTOM_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_WARNING);
            }
        });

    	lineaForm.addBackListener(_ -> ocultarFormulario());
    	inicio.addClickListener(_ -> getUI().ifPresent(ui -> ui.navigate("")));
    }
    
    // Actualiza la grilla
    private void actualizarLista() {
        String texto = filtro.getValue() != null ? filtro.getValue().trim().toLowerCase() : "";
        List<Linea> todas = lineaUseCase.listar();
        List<Linea> filtradas = todas.stream()
            .filter(l -> l.getDescripcion().toLowerCase().contains(texto))
            .toList();
        grid.setItems(filtradas);
    }
    
    private void ocultarFormulario() {
    	lineaForm.setVisible(false);
        grid.asSingleSelect().clear();
    }
	
	

}
