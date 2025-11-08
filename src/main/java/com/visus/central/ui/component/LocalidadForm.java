package com.visus.central.ui.component;

import java.text.ParseException;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;


import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.visus.central.domain.model.Departamento;
import com.visus.central.domain.model.Localidad;

public class LocalidadForm extends AbstractForm<Localidad> {
	
	private static final long serialVersionUID = 1L;
	
	private final TextField nombre = new TextField("Nombre");
    private final TextField codigoPostal = new TextField("Código Postal");
    private final ComboBox<Departamento> departamento = new ComboBox<>("Departamento");

    public LocalidadForm(List<Departamento> departamentos) {
        super(Localidad.class);

        nombre.addClassName("campo-estilo-imagen");
        codigoPostal.addClassName("campo-estilo-imagen");
        departamento.addClassName("campo-estilo-imagen");

        departamento.setItems(departamentos);
        departamento.setItemLabelGenerator(Departamento::getNombre);

        HorizontalLayout acciones = new HorizontalLayout(crear, modificar, eliminar, volver);
        acciones.setSpacing(true);
		acciones.setAlignItems(Alignment.START); // Alineación a la izquierda
        add(nombre, codigoPostal, departamento, acciones);
        
        bindFields();
        
    }

    @Override
    protected void setFormValues(Localidad entity) {
        nombre.setValue(entity.getNombre() != null ? entity.getNombre() : "");
        codigoPostal.setValue(entity.getCodigoPostal() != null ? entity.getCodigoPostal().toString() : "");
        departamento.setValue(entity.getDepartamento());
    }

    @Override
    protected void updateCurrentFromBinder() {
        current.setNombre(nombre.getValue());
        current.setCodigoPostal(Integer.parseInt(codigoPostal.getValue()));
        current.setDepartamento(departamento.getValue());
    }

    @Override
    protected void bindFields() {
        binder.forField(nombre)
            .asRequired("El nombre es obligatorio")
            .bind(Localidad::getNombre, Localidad::setNombre);

        binder.forField(codigoPostal)
        .asRequired("El código postal es obligatorio")
        .withConverter(
            text -> {
                try {
                    return NumberFormat.getIntegerInstance(Locale.US).parse(text).intValue();
                } catch (ParseException e) {
                    return null;
                }
            },
            value -> value != null ? value.toString() : ""
        )
        .bind(Localidad::getCodigoPostal, Localidad::setCodigoPostal);

        binder.forField(departamento)
            .bind(Localidad::getDepartamento, Localidad::setDepartamento);
    }
    
}
