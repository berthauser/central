package com.visus.central.ui.component;

import com.vaadin.flow.component.textfield.TextField;
import com.visus.central.domain.model.Medida;

public class MedidaForm extends AbstractForm<Medida> {
	
	private static final long serialVersionUID = 1L;

	private final TextField descripcion = new TextField("Descripción");
	private final TextField abreviatura = new TextField("Abreviatura");
	
	public MedidaForm() {
		super(Medida.class);
		buildLayout();
	}
	
	private void buildLayout() {
		String campoStyle = "campo-estilo-imagen";

		descripcion.addClassName(campoStyle);
		descripcion.setRequired(true);
		descripcion.setWidth("450px");
		descripcion.setMaxLength(60);
		
		abreviatura.addClassName(campoStyle);
		abreviatura.setRequired(true);
		abreviatura.setWidth("450px");
		abreviatura.setMaxLength(2);

		add(descripcion, abreviatura, buildBotonera());
		bindFields();
	}
	
	@Override
	protected void setFormValues(Medida entity) {
		descripcion.setValue(entity.getDescripcion() != null ? entity.getDescripcion() : "");
		abreviatura.setValue(entity.getAbreviatura() != null ? entity.getAbreviatura() : "");
	}

	@Override
	protected void updateCurrentFromBinder() {
		current.setDescripcion(descripcion.getValue());
		current.setAbreviatura(abreviatura.getValue());
	}

	@Override
	protected void bindFields() {
		binder.forField(descripcion)
		.asRequired("La Descripción es obligatoria")
		.bind(Medida::getDescripcion, Medida::setDescripcion);

		binder.forField(abreviatura)
		.asRequired("La Abreviatura es obligatoria")
		.bind(Medida::getAbreviatura, Medida::setAbreviatura);

	}

}
