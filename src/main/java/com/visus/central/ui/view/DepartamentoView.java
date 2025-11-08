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
import com.visus.central.domain.model.Departamento;
import com.visus.central.domain.port.in.DepartamentoUseCase;
import com.visus.central.domain.port.in.LocalidadUseCase;
import com.visus.central.infraestructure.persistence.entity.JpaDepartamentoEntity.Provincia;
import com.visus.central.ui.component.DepartamentoForm;
import com.visus.central.ui.component.CentralLayout;

@Route(value = "departamentos", layout = CentralLayout.class)
@PageTitle("Gestión de Departamentos")
public class DepartamentoView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final DepartamentoUseCase departamentoService;
	private final LocalidadUseCase localidadService;
	private final Grid<Departamento> grid = new Grid<>(Departamento.class, false);
	private final TextField filtro = new TextField();
	private final DepartamentoForm form = new DepartamentoForm();
	private final Button nuevo = new Button("Nuevo Departamento", new Icon(VaadinIcon.PLUS));
	private final Button inicio = new Button("Inicio", new Icon(VaadinIcon.HOME));
	
	@Autowired
	public DepartamentoView(DepartamentoUseCase departamentoService, LocalidadUseCase localidadService) {
        this.departamentoService = departamentoService;
        this.localidadService = localidadService;
        
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
        grid.addColumn(Departamento::getNombre).setHeader("DEPARTAMENTO");
        grid.addColumn(dep -> dep.getProvincia() != null ? dep.getProvincia().toString() : "")
        .setHeader("PROVINCIA");
        grid.setSizeFull();

        grid.asSingleSelect().addValueChangeListener(e -> {
            Departamento seleccionado = e.getValue();
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
            Departamento nuevo = (Departamento) e.getEntity();
            departamentoService.guardar(nuevo);
            Notification.show("Departamento creado", 3000, Notification.Position.BOTTOM_CENTER)
            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            actualizarLista();
            form.setVisible(false);
        });

        form.addUpdateListener(e -> {
        	Departamento modificado = (Departamento) e.getEntity();
        	departamentoService.guardar(modificado);
        	Notification.show("Departamento modificado", 3000, Notification.Position.BOTTOM_CENTER)
        	.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        	actualizarLista();
        	form.setVisible(false);
        });

        form.addDeleteListener(e -> {
        	Departamento depto = (Departamento) e.getEntity();
           	if (localidadService.tieneLocalidadesAsociadas(depto.getId())) {
                mostrarConfirmacionConDependencias(depto);
            } else {
                mostrarConfirmacionSinDependencias(depto);
            }
        });
        
        form.addBackListener(_ -> form.setVisible(false));
    }

    // Alta institucional
    private void iniciarAlta() {
        Departamento nuevoDepartamento = new Departamento();
        nuevoDepartamento.setProvincia(Provincia.Entre_Ríos); // valor por defecto
        form.setEntity(nuevoDepartamento);
        form.setVisible(true);
    }

    // Actualiza la grilla
    private void actualizarLista() {
        String texto = filtro.getValue();
        List<Departamento> lista = (texto == null || texto.isEmpty())
            ? departamentoService.listar()
            : departamentoService.listar().stream()
                .filter(dep -> dep.getNombre().toLowerCase().contains(texto.toLowerCase()))
                .collect(Collectors.toList());
        grid.setItems(lista);
    }
    
    private void mostrarConfirmacionConDependencias(Departamento depto) {
        Dialog dialogo = new Dialog();
        dialogo.setHeaderTitle("Confirmar eliminación");

        Span mensaje = new Span("El Departamento tiene Localidades asociadas. ¿Desea eliminarlo igualmente?");
        mensaje.getStyle().set("font-weight", "500");

        Button aceptar = new Button("Eliminar", _ -> {
            departamentoService.eliminar(depto.getId());
            actualizarLista();
            form.setVisible(false);
            Notification.show("Departamento eliminado", 3000, Notification.Position.BOTTOM_CENTER)
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
    
    private void mostrarConfirmacionSinDependencias(Departamento depto) {
        Dialog dialogo = new Dialog();
        dialogo.setHeaderTitle("Confirmar eliminación");

        Span mensaje = new Span("¿Está seguro de que desea eliminar el Departamento?");
        mensaje.getStyle().set("font-weight", "500");

        Button aceptar = new Button("Eliminar", _ -> {
            departamentoService.eliminar(depto.getId());
            actualizarLista();
            form.setVisible(false);
            Notification.show("Departamento eliminado", 3000, Notification.Position.BOTTOM_CENTER)
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