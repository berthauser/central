package com.visus.central.ui.component;

import java.math.BigDecimal;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.visus.central.domain.model.Alicuota;

public class AlicuotaForm extends AbstractForm<Alicuota> {
	
	private static final long serialVersionUID = 1L;

	private TextField descripcion = new TextField("Descripción");
	private BigDecimalField gravamen = new BigDecimalField("Gravamen");

	public AlicuotaForm() {
		super(Alicuota.class);
		buildLayout();
	}

	private void buildLayout() {
		String campoStyle = "campo-estilo-imagen";

		descripcion.addClassName(campoStyle);
		descripcion.setRequired(true);
		descripcion.setWidth("400px");
		descripcion.setMaxLength(50);

		gravamen.addClassName(campoStyle);
		gravamen.setRequired(true);
		gravamen.setWidth("150px");
		gravamen.setSuffixComponent(new Span("x"));

		add(descripcion, gravamen, buildBotonera());
		bindFields();
	}

	@Override
	protected void bindFields() {
		binder.forField(descripcion)
		.asRequired("La Descripción es obligatoria")
		.bind(Alicuota::getDescripcion, Alicuota::setDescripcion);

		binder.forField(gravamen)
		.asRequired("El Gravamen es obligatorio")
		.withValidator(value -> value != null && value.compareTo(BigDecimal.ZERO) >= 0, 
				"El Gravamen no puede ser negativo")
		.withValidator(value -> value != null && value.compareTo(new BigDecimal("1000")) <= 0,
				"El Gravamen no puede ser mayor a 1000")
		// Se pueden añadir más validadores según se necesite
		.withValidator(value -> value != null && value.scale() <= 2,
				"No puede tener más de 2 decimales")
		.bind(Alicuota::getGravamen, Alicuota::setGravamen);

	}

	@Override
	protected void setFormValues(Alicuota entity) {
		if (entity == null) {
			descripcion.clear();
			gravamen.clear();
			return;
		}
		descripcion.setValue(entity.getDescripcion() != null ? entity.getDescripcion() : "");
		gravamen.setValue(entity.getGravamen());
	}

	@Override
	protected void updateCurrentFromBinder() {
		// Todos los campos están bindados
	}

}
