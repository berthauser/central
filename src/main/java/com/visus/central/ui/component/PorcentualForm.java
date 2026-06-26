package com.visus.central.ui.component;

import java.math.RoundingMode;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.visus.central.domain.model.Porcentual;
import com.visus.central.domain.model.TipoPorcentual;

public class PorcentualForm extends AbstractForm<Porcentual> {

    private static final long serialVersionUID = 1L;

    private final TextField descripcion = new TextField("Descripción");
    private final BigDecimalField porcentual = new BigDecimalField("Porcentaje");
    private final ComboBox<TipoPorcentual> clasificacion = new ComboBox<>("Clasificación");
    private final DatePicker inicioVigencia = new DatePicker("Inicio Vigencia");
    private final DatePicker finVigencia = new DatePicker("Fin Vigencia");

    public PorcentualForm() {
        super(Porcentual.class);
        buildLayout();
    }

    private void buildLayout() {
        String style = "campo-estilo-imagen";

        descripcion.addClassName(style);
        descripcion.setWidth("400px");
        descripcion.setMaxLength(100);

        porcentual.addClassName(style);
        porcentual.setWidth("150px");
        porcentual.addBlurListener(_ -> {
            if (porcentual.getValue() != null) {
                porcentual.setValue(porcentual.getValue().setScale(2, RoundingMode.HALF_UP));
            }
        });
        porcentual.addValueChangeListener(e -> {
            if (e.getValue() != null && e.getValue().scale() > 2) {
                porcentual.setValue(e.getValue().setScale(2, RoundingMode.HALF_UP));
            }
        });

        clasificacion.addClassName(style);
        clasificacion.setItems(TipoPorcentual.values());
        clasificacion.setWidth("200px");

        inicioVigencia.addClassName(style);
        finVigencia.addClassName(style);

        HorizontalLayout row1 = new HorizontalLayout(descripcion, porcentual, clasificacion);
        row1.setAlignItems(Alignment.END);

        HorizontalLayout row2 = new HorizontalLayout(inicioVigencia, finVigencia);
        row2.setAlignItems(Alignment.END);

        add(row1, row2, buildBotonera());
        bindFields();
    }

    @Override
    protected void bindFields() {
        binder.forField(descripcion)
            .asRequired("La descripción es obligatoria")
            .bind(Porcentual::getDescripcion, Porcentual::setDescripcion);

        binder.forField(porcentual)
            .asRequired("El porcentaje es obligatorio")
            .bind(Porcentual::getPorcentual, Porcentual::setPorcentual);

        binder.forField(clasificacion)
            .asRequired("La clasificación es obligatoria")
            .bind(Porcentual::getClasificacion, Porcentual::setClasificacion);

        binder.forField(inicioVigencia)
            .bind(Porcentual::getInicioVigencia, Porcentual::setInicioVigencia);

        binder.forField(finVigencia)
            .bind(Porcentual::getFinVigencia, Porcentual::setFinVigencia);
    }

    @Override
    protected void setFormValues(Porcentual entity) {
        if (entity == null) {
            descripcion.clear();
            porcentual.clear();
            clasificacion.clear();
            inicioVigencia.clear();
            finVigencia.clear();
            return;
        }
        descripcion.setValue(entity.getDescripcion() != null ? entity.getDescripcion() : "");
        porcentual.setValue(entity.getPorcentual() != null ? entity.getPorcentual().setScale(2, RoundingMode.HALF_UP) : null);
        clasificacion.setValue(entity.getClasificacion());
        inicioVigencia.setValue(entity.getInicioVigencia());
        finVigencia.setValue(entity.getFinVigencia());
    }

    @Override
    protected void updateCurrentFromBinder() {
    }
}
