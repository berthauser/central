package com.visus.central.ui.component;

import com.vaadin.flow.component.textfield.TextField;
import com.visus.central.domain.model.Concepto;

public class ConceptoForm extends AbstractForm<Concepto> {
	
private static final long serialVersionUID = 1L;
    
    private TextField descripcion = new TextField("Descripción");

    public ConceptoForm() {
        super(Concepto.class);
        buildLayout();
    }

    private void buildLayout() {
        String campoStyle = "campo-estilo-imagen";
        
        descripcion.addClassName(campoStyle);
        descripcion.setRequired(true);
        descripcion.setWidth("400px");
        descripcion.setMaxLength(40); // Según la DDL: varchar(40)
        
        add(descripcion, buildBotonera());
        bindFields();
    }
    
    @Override
    protected void bindFields() {
        binder.forField(descripcion)
            .asRequired("La descripción es obligatoria")
            .bind(Concepto::getDescripcion, Concepto::setDescripcion);
    }

    @Override
    protected void setFormValues(Concepto entity) {
        if (entity == null) {
            descripcion.clear();
            return;
        }
        descripcion.setValue(entity.getDescripcion() != null ? entity.getDescripcion() : "");
    }

    @Override
    protected void updateCurrentFromBinder() {
        // Todos los campos están bindados, no hay campos adicionales
    }

}
