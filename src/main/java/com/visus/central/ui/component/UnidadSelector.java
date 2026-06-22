package com.visus.central.ui.component;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.visus.central.domain.model.Unidad;
import com.visus.central.domain.model.UnidadConCantidad;

public class UnidadSelector extends VerticalLayout {

    private static final long serialVersionUID = 1L;
    
	private final Map<Integer, Row> rows = new LinkedHashMap<>();
    private boolean isUpdating = false;
    private Consumer<Set<UnidadConCantidad>> changeListener;

    private static class Row {
        Checkbox checkbox;
        IntegerField cantidadField;
        Unidad unidad;
    }

    public UnidadSelector() {
        setPadding(false);
        setSpacing(false);
        setWidthFull();
    }

    public UnidadSelector(String label) {
        this();
        if (label != null && !label.isEmpty()) {
            Span labelSpan = new Span(label);
            labelSpan.getStyle().set("font-weight", "bold");
            labelSpan.getStyle().set("margin-bottom", "4px");
            addComponentAsFirst(labelSpan);
        }
    }

    public void setItems(List<Unidad> items) {
        removeAll();
        rows.clear();

        // Use a wrapping row layout so items auto-flow within container width
        FlexLayout wrapper = new FlexLayout();
        wrapper.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        wrapper.setWidthFull();
        wrapper.getStyle().set("padding", "0");
        wrapper.getStyle().set("gap", "1px");

        for (Unidad u : items) {
            Row row = new Row();
            row.unidad = u;
            row.checkbox = new Checkbox();
            row.cantidadField = new IntegerField();
            row.cantidadField.setValue(1);
            row.cantidadField.setMin(1);
            row.cantidadField.setWidth("45px");
            row.checkbox.getElement().getStyle().set("--vaadin-checkbox-size", "12px");
            row.checkbox.setWidth("15px");

            row.checkbox.addValueChangeListener(e -> {
                row.cantidadField.setEnabled(e.getValue());
                if (!isUpdating) fireChange();
            });
            row.cantidadField.addValueChangeListener(_ -> {
                if (!isUpdating && row.checkbox.getValue()) fireChange();
            });

            HorizontalLayout rowLayout = new HorizontalLayout();
            rowLayout.setAlignItems(FlexComponent.Alignment.CENTER);
            rowLayout.setMargin(false);
            rowLayout.setPadding(false);
            rowLayout.setWidth("140px");

            Span medidaSpan = new Span(formatMedida(u.getMedida()));
            medidaSpan.getStyle().set("margin-left", "2px");
            medidaSpan.getStyle().set("margin-right", "3px");

            rowLayout.add(row.checkbox, medidaSpan, row.cantidadField);
            wrapper.add(rowLayout);
            rows.put(u.getId(), row);
        }

        add(wrapper);
    }

    public void setValue(Set<UnidadConCantidad> value) {
        isUpdating = true;
        try {
            for (Row row : rows.values()) {
                row.checkbox.setValue(false);
                row.cantidadField.setValue(1);
                row.cantidadField.setEnabled(false);
            }
            if (value != null) {
                for (UnidadConCantidad uc : value) {
                    if (uc.getUnidad() != null) {
                        Row row = rows.get(uc.getUnidad().getId());
                        if (row != null) {
                            row.checkbox.setValue(true);
                            row.cantidadField.setValue(uc.getCantidad() != null ? uc.getCantidad() : 1);
                            row.cantidadField.setEnabled(true);
                        }
                    }
                }
            }
        } finally {
            isUpdating = false;
        }
    }

    public Set<UnidadConCantidad> getValue() {
        Set<UnidadConCantidad> selected = new LinkedHashSet<>();
        for (Row row : rows.values()) {
            if (row.checkbox.getValue()) {
                selected.add(new UnidadConCantidad(row.unidad, row.cantidadField.getValue()));
            }
        }
        return selected;
    }

    public void addValueChangeListener(Consumer<Set<UnidadConCantidad>> listener) {
        this.changeListener = listener;
    }

    public void clear() {
        isUpdating = true;
        try {
            for (Row row : rows.values()) {
                row.checkbox.setValue(false);
                row.cantidadField.setValue(1);
                row.cantidadField.setEnabled(false);
            }
        } finally {
            isUpdating = false;
        }
    }

    private void fireChange() {
        if (changeListener != null) {
            changeListener.accept(getValue());
        }
    }

    private String formatMedida(BigDecimal medida) {
        if (medida == null) return "";
        if (medida.stripTrailingZeros().scale() <= 0) {
            return String.valueOf(medida.intValue());
        }
        return medida.toString();
    }
}
