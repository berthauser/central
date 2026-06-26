package com.visus.central.ui.component;

import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.visus.central.domain.model.Lista;
import com.visus.central.domain.model.ListaPorcentual;
import com.visus.central.domain.model.Porcentual;

public class ListaForm extends AbstractForm<Lista> {

    private static final long serialVersionUID = 1L;

    private final TextField descripcion = new TextField("Descripción");
    private final Grid<ListaPorcentual> itemsGrid = new Grid<>(ListaPorcentual.class, false);
    private final ComboBox<Porcentual> porcentualSelector = new ComboBox<>("Agregar Porcentual");
    private final Button agregarItem = new Button("Agregar", new Icon(VaadinIcon.PLUS));
    private final Button quitarItem = new Button("Quitar", new Icon(VaadinIcon.TRASH));

    private final List<Porcentual> todosPorcentuales;

    public ListaForm(List<Porcentual> todosPorcentuales) {
        super(Lista.class);
        this.todosPorcentuales = todosPorcentuales;
        buildLayout();
    }

    private void buildLayout() {
        String style = "campo-estilo-imagen";

        descripcion.addClassName(style);
        descripcion.setWidth("400px");
        descripcion.setMaxLength(100);

        itemsGrid.addColumn(lp -> lp.getPorcentual() != null ? lp.getPorcentual().getDescripcion() : "")
            .setHeader("PORCENTUAL");
        itemsGrid.addColumn(lp -> lp.getPorcentual() != null && lp.getPorcentual().getPorcentual() != null
            ? String.format("%.2f", lp.getPorcentual().getPorcentual()) : "")
            .setHeader("%");
        itemsGrid.addColumn(lp -> lp.getPorcentual() != null && lp.getPorcentual().getClasificacion() != null
            ? lp.getPorcentual().getClasificacion().name() : "")
            .setHeader("TIPO");
        itemsGrid.setHeight("200px");
        itemsGrid.addClassName("grid-documentacion-dark");

        porcentualSelector.addClassName(style);
        porcentualSelector.setItems(todosPorcentuales);
        porcentualSelector.setItemLabelGenerator(Porcentual::getDescripcion);
        porcentualSelector.setWidth("300px");

        agregarItem.addClickListener(_ -> agregarPorcentual());
        quitarItem.addClickListener(_ -> quitarPorcentual());

        HorizontalLayout topRow = new HorizontalLayout(descripcion, porcentualSelector, agregarItem, quitarItem);
        topRow.setAlignItems(Alignment.END);

        add(topRow, itemsGrid, buildBotonera());
        bindFields();
    }

    private void agregarPorcentual() {
        Porcentual seleccionado = porcentualSelector.getValue();
        if (seleccionado == null) {
            Notification.show("Seleccione un porcentual");
            return;
        }

        boolean yaExiste = current.getItems().stream()
            .anyMatch(item -> item.getPorcentualId() != null
                && item.getPorcentualId().equals(seleccionado.getId()));

        if (yaExiste) {
            Notification.show("El porcentual ya está en la lista");
            return;
        }

        ListaPorcentual item = new ListaPorcentual();
        item.setPorcentual(seleccionado);
        item.setPorcentualId(seleccionado.getId());
        current.getItems().add(item);
        itemsGrid.setItems(current.getItems());
        porcentualSelector.clear();
    }

    private void quitarPorcentual() {
        Set<ListaPorcentual> seleccionados = itemsGrid.getSelectedItems();
        if (seleccionados.isEmpty()) {
            Notification.show("Seleccione un ítem para quitar");
            return;
        }
        current.getItems().removeAll(seleccionados);
        itemsGrid.setItems(current.getItems());
    }

    @Override
    protected void bindFields() {
        binder.forField(descripcion)
            .asRequired("La descripción es obligatoria")
            .bind(Lista::getDescripcion, Lista::setDescripcion);
    }

    @Override
    protected void setFormValues(Lista entity) {
        if (entity == null) {
            descripcion.clear();
            itemsGrid.setItems();
            return;
        }
        descripcion.setValue(entity.getDescripcion() != null ? entity.getDescripcion() : "");
        itemsGrid.setItems(entity.getItems());
    }

    @Override
    protected void updateCurrentFromBinder() {
    }
}
