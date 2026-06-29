package com.visus.central.ui.view;

import java.util.List;
import java.util.stream.Collectors;

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
import com.visus.central.domain.model.Lista;
import com.visus.central.domain.port.in.ListaUseCase;
import com.visus.central.domain.port.in.PorcentualUseCase;
import com.visus.central.ui.component.CentralLayout;
import com.visus.central.ui.component.ListaForm;

@Route(value = "listas", layout = CentralLayout.class)
@PageTitle("Gestión de Listas de Precios")
public class ListasView extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    private final ListaUseCase servicio;
    private final Grid<Lista> grid = new Grid<>(Lista.class, false);
    private final TextField filtro = new TextField();
    private final ListaForm form;
    private final Button nuevo = new Button("Nueva Lista", new Icon(VaadinIcon.PLUS));
    private final Button inicio = new Button("Inicio", new Icon(VaadinIcon.HOME));

    public ListasView(ListaUseCase servicio, PorcentualUseCase porcentualService) {
        this.servicio = servicio;
        this.form = new ListaForm(porcentualService.findAll());

        configurarVista();
        configurarFiltro();
        configurarGrilla();
        configurarEventos();
        actualizarLista();
    }

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

    private void configurarFiltro() {
        filtro.setPlaceholder("Buscar por nombre...");
        filtro.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        filtro.setClearButtonVisible(true);
        filtro.addValueChangeListener(_ -> actualizarLista());
    }

    private void configurarGrilla() {
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setColumnReorderingAllowed(true);
        grid.setHeight("400px");
        grid.addClassName("grid-documentacion-dark");
        grid.addColumn(Lista::getDescripcion).setHeader("LISTA");
        grid.addColumn(l -> l.getItems() != null ? l.getItems().size() : 0).setHeader("CANT. PORCENTUALES");
        grid.setSizeFull();

        grid.asSingleSelect().addValueChangeListener(e -> {
            Lista seleccionado = e.getValue();
            if (seleccionado != null) {
                form.setEntity(seleccionado);
                form.setVisible(true);
            }
        });
    }

    private void configurarEventos() {
        form.addCreateListener(e -> {
            servicio.save((Lista) e.getEntity());
            Notification.show("Lista creada", 3000, Notification.Position.BOTTOM_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            actualizarLista();
            ocultarFormulario();
        });

        form.addUpdateListener(e -> {
            servicio.save((Lista) e.getEntity());
            Notification.show("Lista modificada", 3000, Notification.Position.BOTTOM_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            actualizarLista();
            ocultarFormulario();
        });

        form.addDeleteListener(e -> {
            Lista l = (Lista) e.getEntity();
            if (l.getId() != null) {
                Dialog dialogo = new Dialog();
                dialogo.setHeaderTitle("Confirmar eliminación");
                Span mensaje = new Span("¿Está seguro de que desea eliminar la lista?");
                mensaje.getStyle().set("font-weight", "500");
                Button aceptar = new Button("Eliminar", _ -> {
                    servicio.deleteById(l.getId());
                    actualizarLista();
                    ocultarFormulario();
                    Notification.show("Lista eliminada", 3000, Notification.Position.BOTTOM_CENTER)
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
        });

        form.addBackListener(_ -> ocultarFormulario());
    }

    private void iniciarAlta() {
        form.setEntity(new Lista());
        form.setVisible(true);
    }

    private void actualizarLista() {
        String texto = filtro.getValue();
        List<Lista> lista = (texto == null || texto.isEmpty())
            ? servicio.findAll()
            : servicio.findAll().stream()
                .filter(l -> l.getDescripcion() != null
                    && l.getDescripcion().toLowerCase().contains(texto.toLowerCase()))
                .collect(Collectors.toList());
        grid.setItems(lista);
    }

    private void ocultarFormulario() {
        form.setVisible(false);
        grid.asSingleSelect().clear();
    }
}
