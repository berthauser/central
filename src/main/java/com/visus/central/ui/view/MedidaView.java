package com.visus.central.ui.view;

import java.util.List;

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
import com.visus.central.domain.model.Medida;
import com.visus.central.domain.port.in.MedidaUseCase;
import com.visus.central.ui.component.CentralLayout;
import com.visus.central.ui.component.MedidaForm;

@Route(value = "medidas", layout = CentralLayout.class)
@PageTitle("Gestión de Medidas de Artículos")
public class MedidaView extends VerticalLayout {
	
private static final long serialVersionUID = 1L;
	
	private final MedidaUseCase medidaService;

    private final Grid<Medida> grid = new Grid<>(Medida.class, false);
    private final TextField filtro = new TextField();
    private final MedidaForm form = new MedidaForm();
    private final Button agregar = new Button("Nueva Medida", new Icon(VaadinIcon.PLUS));
	private final Button inicio = new Button("Inicio", new Icon(VaadinIcon.HOME));
  
    @Autowired
	public MedidaView(MedidaUseCase medidaService) {
        this.medidaService = medidaService;
        
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
        add(acciones, grid, form);
    }
    
    // Filtro por nombre
    private void configurarFiltro() {
        filtro.setPlaceholder("Buscar por descripción...");
        filtro.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        filtro.setClearButtonVisible(true);
        filtro.addValueChangeListener(_ -> actualizarLista());
    }
    
    // Alta institucional
    private void iniciarAlta() {
    	Medida nuevaMedida = new Medida();
        form.setEntity(nuevaMedida);
        form.setVisible(true);
    }
    
    // Grilla institucional    
    private void configurarGrilla() {
    	grid.getColumns().forEach(col -> col.setAutoWidth(true));
    	grid.setColumnReorderingAllowed(true);
    	grid.setHeight("300px");
    	grid.addClassName("grid-documentacion-dark");
        grid.addColumn(Medida::getDescripcion).setHeader("DESCRIPCIÓN").setAutoWidth(true);
        grid.addColumn(Medida::getAbreviatura).setHeader("ABREVIATURA").setAutoWidth(true);
        grid.setSizeFull();
        grid.asSingleSelect().addValueChangeListener(e -> {
        	Medida seleccionada = e.getValue();
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
    		Medida nueva = (Medida) e.getEntity();
    		medidaService.guardar(nueva);
    		Notification.show("Medida creada correctamente", 3000, Notification.Position.BOTTOM_CENTER)
    		.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            actualizarLista();
            ocultarFormulario();
        });
    	
    	form.addUpdateListener(e -> {
            medidaService.guardar((Medida) e.getEntity());
            actualizarLista();
            ocultarFormulario();
            Notification.show("Medida modificada correctamente", 3000, Notification.Position.BOTTOM_CENTER)
        	.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });

    	form.addDeleteListener(e -> {
    		Medida medida = (Medida) e.getEntity();
            
            if (medida.getId() != null) {
            	Dialog dialogo = new Dialog();
                dialogo.setHeaderTitle("Confirmar eliminación");

                Span mensaje = new Span("¿Está seguro de que desea eliminar la Medida?");
                mensaje.getStyle().set("font-weight", "500");

                Button aceptar = new Button("Eliminar", _ -> {
                	medidaService.eliminar(medida.getId());
                    actualizarLista();
                    ocultarFormulario();
                    Notification.show("Medida eliminada", 3000, Notification.Position.BOTTOM_CENTER)
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
                Notification.show("La Medida aún no fue guardada", 3000, Notification.Position.BOTTOM_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_WARNING);
            }
        });

    	form.addBackListener(_ -> ocultarFormulario());
    	inicio.addClickListener(_ -> getUI().ifPresent(ui -> ui.navigate("")));
    }
    
    // Actualiza la grilla
    private void actualizarLista() {
        String texto = filtro.getValue() != null ? filtro.getValue().trim().toLowerCase() : "";
        List<Medida> todas = medidaService.listar();
        List<Medida> filtradas = todas.stream()
            .filter(l -> l.getDescripcion().toLowerCase().contains(texto))
            .toList();
        grid.setItems(filtradas);
    }
    
    private void ocultarFormulario() {
        form.setVisible(false);
        grid.asSingleSelect().clear();
    }

}
