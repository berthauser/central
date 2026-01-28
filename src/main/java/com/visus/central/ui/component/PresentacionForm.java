package com.visus.central.ui.component;

import com.vaadin.flow.component.textfield.TextField;
import com.visus.central.domain.model.Presentacion;

public class PresentacionForm extends AbstractForm<Presentacion> {
	
	private static final long serialVersionUID = 1L;

	private TextField descripcion = new TextField("Descripción");

	public PresentacionForm() {
		super(Presentacion.class);
		buildLayout();
	}

	private void buildLayout() {
		String campoStyle = "campo-estilo-imagen";

		descripcion.addClassName(campoStyle);
		descripcion.setRequired(true);
		descripcion.setWidth("450px");
		descripcion.setMaxLength(60);

		add(descripcion, buildBotonera());
		bindFields();
	}

	@Override
	protected void bindFields() {
		binder.forField(descripcion)
		.asRequired("La descripción es obligatoria")
		.bind(Presentacion::getDescripcion, Presentacion::setDescripcion);
	}

	@Override
	protected void setFormValues(Presentacion entity) {
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
