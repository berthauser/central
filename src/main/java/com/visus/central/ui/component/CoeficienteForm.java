package com.visus.central.ui.component;

import java.math.BigDecimal;
import java.util.List;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.visus.central.domain.model.Coeficiente;
import com.visus.central.domain.model.TipoPago;

public class CoeficienteForm extends AbstractForm<Coeficiente> {

	private static final long serialVersionUID = 1L;

	private ComboBox<TipoPago> tipoPago = new ComboBox<>("Tipo de Pago");
	private TextField descripcion = new TextField("Descripción");
	private BigDecimalField coeficiente = new BigDecimalField("Coeficiente");
	private IntegerField cuotas = new IntegerField("Cuotas");

	// Método para cargar los tipos de pago desde el servicio
	private transient List<TipoPago> tiposPagoDisponibles;

	public CoeficienteForm() {
		super(Coeficiente.class);
		buildLayout();
	}

	private void buildLayout() {
		String campoStyle = "campo-estilo-imagen";

		tipoPago.addClassName(campoStyle);
		tipoPago.setWidth("300px");
		tipoPago.setRequired(true);
		tipoPago.setPlaceholder("Seleccione un Tipo de Pago");

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

		add(descripcion, tipoPago, coeficiente, cuotas, buildBotonera());
		bindFields();
	}

	@Override
	protected void bindFields() {
		binder.forField(descripcion).asRequired("La descripción es obligatoria").bind(Coeficiente::getDescripcion,
				Coeficiente::setDescripcion);

		// Binding para TipoPago
		binder.forField(tipoPago).asRequired("Debe seleccionar un Tipo de Pago").bind(Coeficiente::getTipo_pago,
				Coeficiente::setTipo_pago);

		binder.forField(coeficiente).asRequired("El coeficiente es obligatorio")
				.withValidator(value -> value != null && value.compareTo(BigDecimal.ZERO) >= 0,
						"El coeficiente no puede ser negativo")
				.withValidator(value -> value != null && value.compareTo(new BigDecimal("1000")) <= 0,
						"El coeficiente no puede ser mayor a 1000")
				// Puedes añadir más validadores según necesites
				.withValidator(value -> value != null && value.scale() <= 2, "No puede tener más de 2 decimales")
				.bind(Coeficiente::getCoeficiente, Coeficiente::setCoeficiente);

		cuotas.setMin(0); // No negativos
		cuotas.setMax(Short.MAX_VALUE); // Límite máximo de short

		binder.forField(cuotas).asRequired("Las cuotas son obligatorias")
				.withConverter(Integer::shortValue, Short::intValue // Convierte Short a Integer
				).withValidator(value -> value == null || value >= 0, "Las cuotas no pueden ser negativas")
				.withNullRepresentation((short) 0).bind(Coeficiente::getCuotas, Coeficiente::setCuotas);
	}

	@Override
	protected void setFormValues(Coeficiente entity) {
		if (entity == null) {
			descripcion.clear();
			tipoPago.clear();
			coeficiente.clear();
			cuotas.clear();
			return;
		}
		tipoPago.setValue(entity.getTipo_pago());
		descripcion.setValue(entity.getDescripcion() != null ? entity.getDescripcion() : "");
		coeficiente.setValue(entity.getCoeficiente());
		cuotas.setValue(entity.getCuotas() != null ? (int) entity.getCuotas() : 0);
	}

	@Override
	protected void updateCurrentFromBinder() {
		// Todos los campos están bindados
	}

	public void setTiposPago(List<TipoPago> tiposPago) {
		this.tiposPagoDisponibles = tiposPago;
		tipoPago.setItems(tiposPagoDisponibles);
		tipoPago.setItemLabelGenerator(TipoPago::getDescripcion);
		tipoPago.setRequired(true);
	}

}
