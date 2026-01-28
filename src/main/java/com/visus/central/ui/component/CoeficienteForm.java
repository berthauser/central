package com.visus.central.ui.component;

import java.math.BigDecimal;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.visus.central.domain.model.Coeficiente;

public class CoeficienteForm extends AbstractForm<Coeficiente> {
	
	private static final long serialVersionUID = 1L;

	private TextField descripcion = new TextField("Descripción");
	private BigDecimalField coeficiente = new BigDecimalField("Coeficiente");
	private IntegerField cuotas = new IntegerField("Cuotas");

	public CoeficienteForm() {
		super(Coeficiente.class);
		buildLayout();
	}

	private void buildLayout() {
		String campoStyle = "campo-estilo-imagen";

		descripcion.addClassName(campoStyle);
		descripcion.setRequired(true);
		descripcion.setWidth("400px");
		descripcion.setMaxLength(50);

		coeficiente.addClassName(campoStyle);
		coeficiente.setRequired(true);
		coeficiente.setWidth("150px");
		coeficiente.setSuffixComponent(new Span("x"));

		cuotas.addClassName(campoStyle);
		cuotas.setRequired(true);
		cuotas.setWidth("100px");

		add(descripcion, coeficiente, cuotas, buildBotonera());
		bindFields();
	}

	@Override
	protected void bindFields() {
		binder.forField(descripcion)
		.asRequired("La descripción es obligatoria")
		.bind(Coeficiente::getDescripcion, Coeficiente::setDescripcion);

		binder.forField(coeficiente)
		.asRequired("El coeficiente es obligatorio")
		.withValidator(value -> value != null && value.compareTo(BigDecimal.ZERO) >= 0, 
				"El coeficiente no puede ser negativo")
		.withValidator(value -> value != null && value.compareTo(new BigDecimal("1000")) <= 0,
				"El coeficiente no puede ser mayor a 1000")
		// Puedes añadir más validadores según necesites
		.withValidator(value -> value != null && value.scale() <= 2,
				"No puede tener más de 2 decimales")
		.bind(Coeficiente::getCoeficiente, Coeficiente::setCoeficiente);

		cuotas.setMin(0); // No negativos
		cuotas.setMax(Short.MAX_VALUE); // Límite máximo de short

		binder.forField(cuotas)
		.asRequired("Las cuotas son obligatorias")
		.withConverter(
				Integer::shortValue,  // Convierte Integer a Short
				Short::intValue       // Convierte Short a Integer  
				)
		.withValidator(value -> value == null || value >= 0, 
				"Las cuotas no pueden ser negativas")
		.withNullRepresentation((short) 0)
		.bind(Coeficiente::getCuotas, Coeficiente::setCuotas);
	}

	@Override
	protected void setFormValues(Coeficiente entity) {
		if (entity == null) {
			descripcion.clear();
			coeficiente.clear();
			cuotas.clear();
			return;
		}
		descripcion.setValue(entity.getDescripcion() != null ? entity.getDescripcion() : "");
		coeficiente.setValue(entity.getCoeficiente());
		cuotas.setValue(entity.getCuotas() != null ? (int) entity.getCuotas() : 0);
	}

	@Override
	protected void updateCurrentFromBinder() {
		// Todos los campos están bindados
	}

}
