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
import com.visus.central.domain.model.Porcentual;
import com.visus.central.domain.port.in.PorcentualUseCase;
import com.visus.central.ui.component.CentralLayout;
import com.visus.central.ui.component.PorcentualForm;

@Route(value = "porcentuales", layout = CentralLayout.class)
@PageTitle("Gestión de Porcentuales")
public class PorcentualesView extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    private final PorcentualUseCase servicio;
    private final Grid<Porcentual> grid = new Grid<>(Porcentual.class, false);
    private final TextField filtro = new TextField();
    private final PorcentualForm form = new PorcentualForm();
    private final Button nuevo = new Button("Nuevo Porcentual", new Icon(VaadinIcon.PLUS));
    private final Button inicio = new Button("Inicio", new Icon(VaadinIcon.HOME));

    public PorcentualesView(PorcentualUseCase servicio) {
        this.servicio = servicio;

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
        grid.addColumn(Porcentual::getDescripcion).setHeader("DESCRIPCIÓN");
        grid.addColumn(p -> p.getPorcentual() != null ? String.format("%.2f", p.getPorcentual()) : "").setHeader("%");
        grid.addColumn(p -> p.getClasificacion() != null ? p.getClasificacion().name() : "").setHeader("TIPO");
        grid.setSizeFull();

        grid.asSingleSelect().addValueChangeListener(e -> {
            Porcentual seleccionado = e.getValue();
            if (seleccionado != null) {
                form.setEntity(seleccionado);
                form.setVisible(true);
            }
        });
    }

    private void configurarEventos() {
        form.addCreateListener(e -> {
            servicio.save((Porcentual) e.getEntity());
            Notification.show("Porcentual creado", 3000, Notification.Position.BOTTOM_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            actualizarLista();
            ocultarFormulario();
        });

        form.addUpdateListener(e -> {
            servicio.save((Porcentual) e.getEntity());
            Notification.show("Porcentual modificado", 3000, Notification.Position.BOTTOM_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            actualizarLista();
            ocultarFormulario();
        });

        form.addDeleteListener(e -> {
            Porcentual p = (Porcentual) e.getEntity();
            if (p.getId() != null) {
                Dialog dialogo = new Dialog();
                dialogo.setHeaderTitle("Confirmar eliminación");
                Span mensaje = new Span("¿Está seguro de que desea eliminar el porcentual?");
                mensaje.getStyle().set("font-weight", "500");
                Button aceptar = new Button("Eliminar", _ -> {
                    servicio.deleteById(p.getId());
                    actualizarLista();
                    ocultarFormulario();
                    Notification.show("Porcentual eliminado", 3000, Notification.Position.BOTTOM_CENTER)
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
        form.setEntity(new Porcentual());
        form.setVisible(true);
    }

    private void actualizarLista() {
        String texto = filtro.getValue();
        List<Porcentual> lista = (texto == null || texto.isEmpty())
            ? servicio.findAll()
            : servicio.findAll().stream()
                .filter(p -> p.getDescripcion() != null
                    && p.getDescripcion().toLowerCase().contains(texto.toLowerCase()))
                .collect(Collectors.toList());
        grid.setItems(lista);
    }

    private void ocultarFormulario() {
        form.setVisible(false);
        grid.asSingleSelect().clear();
    }
}
