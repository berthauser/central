package com.visus.central.ui.component;

import java.math.BigDecimal;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.visus.central.domain.model.TipoPago;

public class TipoPagoForm extends AbstractForm<TipoPago> {

	private static final long serialVersionUID = 1L;

	private TextField descripcion = new TextField("Descripción");
	private Checkbox requiereCoeficiente = new Checkbox("Requiere Coeficiente");
	private Checkbox esProntoPago = new Checkbox("Habilitar Pronto Pago");
	private BigDecimalField dtoProntoPago = new BigDecimalField("Dto. Pronto Pago");
	private Checkbox generaDeuda = new Checkbox("Genera Deuda");
	private Checkbox afectaCaja = new Checkbox("Afecta a Caja");

	// Variables de estado internas

	// originalDtoProntoPago: valor traído desde la fila seleccionada en la grilla
	private BigDecimal originalDtoProntoPago = null;

	// Nueva bandera para evitar bucles
	private boolean updatingFromCode = false;

	public TipoPagoForm() {
		super(TipoPago.class);
		buildLayout();
	}

	private void buildLayout() {
		String campoStyle = "campo-estilo-imagen";

		descripcion.addClassName(campoStyle);
		descripcion.setRequired(true);
		descripcion.setWidth("450px");
		descripcion.setMaxLength(60);

		// CheckBox
		requiereCoeficiente.addClassName(campoStyle);
		requiereCoeficiente.setWidth("150px");
		requiereCoeficiente.setTooltipText("Coeficiente de Financiación, si corresponde");

		esProntoPago.addClassName(campoStyle);
		esProntoPago.setWidth("150px");
		esProntoPago.setTooltipText("Habilita la opción de descuento por Pronto Pago");

		dtoProntoPago.addClassName(campoStyle);
		dtoProntoPago.setWidth("150px");
		dtoProntoPago.setSuffixComponent(new Span("%"));
		dtoProntoPago
				.setTooltipText("Porcentaje de descuento que se aplicará si se paga antes de la fecha de vencimiento");

		generaDeuda.addClassName(campoStyle);
		generaDeuda.setWidth("150px");
		generaDeuda.setTooltipText("Chequear si el Tipo de Pago genera deuda en Cuenta Corriente.");

		afectaCaja.addClassName(campoStyle);
		afectaCaja.setWidth("150px");
		afectaCaja.setTooltipText("Chequear si Tipo de Pago genera movimiento automático en Caja.");

		// Configurar lógica de habilitación condicional
		// Listener del checkbox: al deschequear limpia la UI, al chequear restaura
		// stagedDtoProntoPago
		esProntoPago.addValueChangeListener(event -> {
			// Evitar bucle: si la actualización viene del código, no hacer nada
			if (updatingFromCode) {
				return;
			}

			boolean tieneDto = Boolean.TRUE.equals(event.getValue());
			dtoProntoPago.setReadOnly(!tieneDto); // lo pone en FALSE

			if (!tieneDto) {
				// Guardar el valor actual antes de limpiar
				if (dtoProntoPago.getValue() != null) {
					originalDtoProntoPago = dtoProntoPago.getValue();
				}
				dtoProntoPago.clear();
				if (current != null) {
					current.setEs_pronto_pago(null);
					current.setDto_pronto_pago(null);
				}
				// Usar binder.readBean en lugar de setBean para evitar bucles
				binder.readBean(current);
			} else {
				if (originalDtoProntoPago != null) {
					// Establecer la bandera antes de actualizar
					updatingFromCode = true;
					try {
						dtoProntoPago.setValue(originalDtoProntoPago);
						if (current != null) {
							current.setDto_pronto_pago(originalDtoProntoPago);
							current.setEs_pronto_pago(true);
						}
						binder.readBean(current);
					} finally {
						updatingFromCode = false; // Siempre resetear la bandera
					}
				}
			}
		});

		// ALINEACIÓN HORIZONTAL: Usar HorizontalLayout para organizar en filas
		HorizontalLayout fila1 = new HorizontalLayout(descripcion, requiereCoeficiente, esProntoPago, dtoProntoPago,
				generaDeuda, afectaCaja);
		fila1.setSpacing(true);
		fila1.setAlignItems(Alignment.END); // Alinear al bottom para que se vea mejor

		add(fila1, buildBotonera());
		bindFields();
	}

	@Override
	protected void bindFields() {

		binder.forField(descripcion).asRequired("La descripción es obligatoria").bind(TipoPago::getDescripcion,
				TipoPago::setDescripcion);

		binder.forField(requiereCoeficiente).bind(TipoPago::getRequiere_coeficiente, TipoPago::setRequiere_coeficiente);

		binder.forField(generaDeuda).bind(TipoPago::getGenera_deuda, TipoPago::setGenera_deuda);

		binder.forField(afectaCaja).bind(TipoPago::getAfecta_caja, TipoPago::setAfecta_caja);

		binder.forField(esProntoPago).bind(TipoPago::getEs_pronto_pago, TipoPago::setEs_pronto_pago);

		// BINDING con validadores que solo aplican cuando el checkbox está activo.
		// Observación: consideramos inválido NULL o ZERO cuando el checkbox es true.
		binder.forField(dtoProntoPago).withValidator(value -> {
			// No validar cuando el descuento no aplica
			if (!Boolean.TRUE.equals(esProntoPago.getValue())) {
				return true;
			}
			// Cuando aplica, value no puede ser null ni cero
			if (value == null)
				return false;
			return value.compareTo(BigDecimal.ZERO) > 0; // >0 (cero inválido)
		}, "El descuento por pronto pago es requerido y no puede ser cero").withValidator(value -> {
			if (!Boolean.TRUE.equals(esProntoPago.getValue())) {
				return true;
			}
			if (value == null)
				return false;
			return value.compareTo(new BigDecimal("100")) <= 0;
		}, "El descuento por pronto pago no puede ser mayor a 100").withValidator(value -> {
			if (!Boolean.TRUE.equals(esProntoPago.getValue())) {
				return true;
			}
			if (value == null)
				return false;
			return value.scale() <= 2;
		}, "No puede tener más de 2 decimales").bind(TipoPago::getDto_pronto_pago, TipoPago::setDto_pronto_pago);

	}

	@Override
	protected void setFormValues(TipoPago entity) {
		if (entity == null) {
			descripcion.clear();
			requiereCoeficiente.clear();
			esProntoPago.clear();
			dtoProntoPago.clear();
			generaDeuda.clear();
			afectaCaja.clear();
			originalDtoProntoPago = null;
			return;
		}
		esProntoPago.setValue(entity.getEs_pronto_pago());

		// Guardar el valor original
		originalDtoProntoPago = entity.getDto_pronto_pago();
		dtoProntoPago.setValue(entity.getDto_pronto_pago());

	}

	@Override
	protected void updateCurrentFromBinder() {
		// Todos los campos están bindados menos este que lo hago manual
		// Cuando se guarda, actualizar el valor temporal
		if (current != null && current.getDto_pronto_pago() != null) {
			originalDtoProntoPago = current.getDto_pronto_pago();
		}
	}

}
