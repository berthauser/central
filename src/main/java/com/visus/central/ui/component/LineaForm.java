package com.visus.central.ui.component;

import java.util.List;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.visus.central.domain.model.Linea;
import com.visus.central.domain.model.Rubro;

public class LineaForm extends AbstractForm<Linea>{
	
	private static final long serialVersionUID = 1L;
	
	private final TextField descripcion = new TextField("Descripción");
    private final ComboBox<Rubro> rubro = new ComboBox<>("Rubros");

    public LineaForm(List<Rubro> rubros) {
        super(Linea.class);

        descripcion.addClassName("campo-estilo-imagen");
        rubro.addClassName("campo-estilo-imagen");

        rubro.setItems(rubros);
        rubro.setItemLabelGenerator(Rubro::getDescripcion);

        HorizontalLayout acciones = new HorizontalLayout(crear, modificar, eliminar, volver);
        acciones.setSpacing(true);
		acciones.setAlignItems(Alignment.START); // Alineación a la izquierda
        add(descripcion, rubro, acciones);
        
        bindFields();
        
    }

    @Override
    protected void setFormValues(Linea entity) {
        descripcion.setValue(entity.getDescripcion() != null ? entity.getDescripcion() : "");
        rubro.setValue(entity.getRubro());
    }

    @Override
    protected void updateCurrentFromBinder() {
        current.setDescripcion(descripcion.getValue());
        current.setRubro(rubro.getValue());
    }

    @Override
    protected void bindFields() {
        binder.forField(descripcion)
            .asRequired("El nombre es obligatorio")
            .bind(Linea::getDescripcion, Linea::setDescripcion);

        binder.forField(rubro)
            .bind(Linea::getRubro, Linea::setRubro);
    }

}
