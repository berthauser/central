package com.visus.central.ui.component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.data.validator.BigDecimalRangeValidator;
import com.visus.central.domain.model.Articulo;
import com.visus.central.domain.port.in.ActualizarPreciosUseCase;
import com.visus.central.domain.port.out.ArticuloRepository;

public class ActualizacionPreciosCodigoBarraForm extends AbstractProcessForm {

	private static final long serialVersionUID = 1L;

	private final ActualizarPreciosUseCase actualizarPreciosUseCase;
	private final ArticuloRepository articuloRepository;

	private TextField codigoBarraField;
	private Button buscarButton;
	private TextField descripcionField;
	private TextField precioActualField;
	private TextField porcentajeField;
	private TextField nuevoPrecioField;

	private Binder<PorcentajeBean> binder;
	private Articulo articuloActual;

	public ActualizacionPreciosCodigoBarraForm(
			ActualizarPreciosUseCase actualizarPreciosUseCase,
			ArticuloRepository articuloRepository) {

		this.actualizarPreciosUseCase = actualizarPreciosUseCase;
		this.articuloRepository = articuloRepository;

		//        setTitulo("Actualización de Precio por Código de Barras");
	}

	@Override
	protected void configurarContenido() {
		// Configurar campos del formulario

		// Campo para código de barras
		codigoBarraField = new TextField("Código de Barras");
		codigoBarraField.setWidth("300px");
		codigoBarraField.setRequired(true);
		codigoBarraField.setPlaceholder("Ingrese el código de barras");

		// Botón para buscar
		buscarButton = new Button("Buscar", VaadinIcon.SEARCH.create());
		buscarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		buscarButton.addClickListener(_ -> buscarArticulo());

		HorizontalLayout busquedaLayout = new HorizontalLayout(codigoBarraField, buscarButton);
		busquedaLayout.setAlignItems(Alignment.BASELINE);

		// Campo para descripción (solo lectura)
		descripcionField = new TextField("Descripción");
		descripcionField.setWidth("400px");
		descripcionField.setReadOnly(true);
		descripcionField.setVisible(false);
		
		// Campo para precio actual (solo lectura)
		precioActualField = new TextField("Precio Actual");
		precioActualField.setWidth("150px");
		precioActualField.setReadOnly(true);
		precioActualField.setVisible(false);

		// Campo para porcentaje
		porcentajeField = new TextField("Porcentaje (%)");
		porcentajeField.setWidth("120px");
		Tooltip.forComponent(porcentajeField)
		.withText("Ej: 10 (aumento) o -5 (disminución)")
		.withPosition(Tooltip.TooltipPosition.TOP);
		porcentajeField.setRequired(true);
		
		// Campo para nuevo precio (solo lectura)
		nuevoPrecioField = new TextField("Nuevo Precio");
		nuevoPrecioField.setWidth("150px");
		nuevoPrecioField.setReadOnly(true);
		nuevoPrecioField.setVisible(false);

		// Configurar binder para validación
		binder = new Binder<>();
		binder.forField(porcentajeField)
		.withConverter(new StringToBigDecimalConverter("Debe ser un número válido"))
		.withValidator(new BigDecimalRangeValidator(
				"El porcentaje debe estar entre -99 y 1000",
				new BigDecimal("-99"),
				new BigDecimal("1000")))
		.withValidator(porcentaje -> {
			if (porcentaje == null) return false;
			return !porcentaje.equals(BigDecimal.ZERO);
		}, "El porcentaje no puede ser cero")
		.bind(PorcentajeBean::getPorcentaje, PorcentajeBean::setPorcentaje);

		// Listener para calcular nuevo precio en tiempo real
		porcentajeField.addValueChangeListener(_ -> calcularNuevoPrecio());

		// Agregar componentes al layout
		getContenidoLayout().add(
				busquedaLayout,
				descripcionField,
				precioActualField,
				porcentajeField,
				nuevoPrecioField
				);
	}

	private void buscarArticulo() {
		String codigoBarra = codigoBarraField.getValue();

		if (codigoBarra == null || codigoBarra.trim().isEmpty()) {
			Notification.show("Debe ingresar un código de barras", 3000, Notification.Position.MIDDLE);
			return;
		}

		try {
			Optional<Articulo> articuloOpt = articuloRepository.findByCodigoBarra(codigoBarra.trim());

			if (articuloOpt.isPresent()) {
				articuloActual = articuloOpt.get();

				// Mostrar información del artículo
				descripcionField.setValue(articuloActual.getDescripcion());
				descripcionField.setVisible(true);

				if (articuloActual.getPrecioCosto() != null) {
					precioActualField.setValue(String.format("%.2f", articuloActual.getPrecioCosto()));
					precioActualField.setVisible(true);
				} else {
					precioActualField.setValue("Sin precio");
					precioActualField.setVisible(true);
				}

				// Habilitar campo de porcentaje
				porcentajeField.setEnabled(true);
				porcentajeField.focus();

				// Calcular nuevo precio si ya hay un porcentaje
				if (porcentajeField.getValue() != null && !porcentajeField.getValue().isEmpty()) {
					calcularNuevoPrecio();
				}

				Notification.show("Artículo encontrado", 3000, Notification.Position.MIDDLE);
			} else {
				Notification.show("No se encontró ningún artículo con ese código de barras", 
						5000, Notification.Position.MIDDLE);
				limpiarCamposArticulo();
			}
		} catch (Exception e) {
			Notification.show("Error al buscar artículo: " + e.getMessage(), 
					5000, Notification.Position.MIDDLE);
			limpiarCamposArticulo();
		}
	}

	private void calcularNuevoPrecio() {
		if (articuloActual == null || articuloActual.getPrecioCosto() == null) {
			nuevoPrecioField.setVisible(false);
			return;
		}

		String porcentajeTexto = porcentajeField.getValue();
		if (porcentajeTexto == null || porcentajeTexto.trim().isEmpty()) {
			nuevoPrecioField.setVisible(false);
			return;
		}

		try {
			BigDecimal porcentaje = new BigDecimal(porcentajeTexto);
			BigDecimal precioActual = articuloActual.getPrecioCosto();

			// Calcular factor
			BigDecimal factor = BigDecimal.ONE.add(
					porcentaje.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
					);

			// Calcular nuevo precio
			BigDecimal nuevoPrecio = precioActual.multiply(factor)
					.setScale(2, RoundingMode.HALF_UP);

			// Mostrar nuevo precio
			nuevoPrecioField.setValue(String.format("%.2f", nuevoPrecio));
			nuevoPrecioField.setVisible(true);

			// Cambiar color según si es aumento o disminución
			if (porcentaje.compareTo(BigDecimal.ZERO) > 0) {
				nuevoPrecioField.getStyle().set("color", "var(--lumo-error-color)");
			} else {
				nuevoPrecioField.getStyle().set("color", "var(--lumo-success-color)");
			}

		} catch (NumberFormatException e) {
			nuevoPrecioField.setVisible(false);
		}
	}

	private void limpiarCamposArticulo() {
		articuloActual = null;
		descripcionField.clear();
		descripcionField.setVisible(false);
		precioActualField.clear();
		precioActualField.setVisible(false);
		nuevoPrecioField.clear();
		nuevoPrecioField.setVisible(false);
		porcentajeField.setEnabled(false);
	}

	@Override
	protected boolean validarFormulario() {
		// Validar que se haya encontrado un artículo
		if (articuloActual == null) {
			Notification.show("Debe buscar y seleccionar un artículo", 
					3000, Notification.Position.MIDDLE);
			codigoBarraField.focus();
			return false;
		}

		// Validar que el artículo tenga precio
		if (articuloActual.getPrecioCosto() == null) {
			Notification.show("El artículo no tiene precio de costo", 
					3000, Notification.Position.MIDDLE);
			return false;
		}

		// Validar el porcentaje
		PorcentajeBean bean = new PorcentajeBean();
		try {
			binder.writeBean(bean);
		} catch (ValidationException e) {
			Notification.show("Porcentaje inválido: " + e.getMessage(),
					3000, Notification.Position.MIDDLE);
			porcentajeField.focus();
			return false;
		}

		return true;
	}

	@Override
	protected void ejecutarProceso() {
		// Obtener el porcentaje
		PorcentajeBean bean = new PorcentajeBean();
		try {
			binder.writeBean(bean);
		} catch (ValidationException e) {
			Notification.show("Error inesperado al obtener el porcentaje", 
					3000, Notification.Position.MIDDLE);
			return;
		}

		BigDecimal porcentaje = bean.getPorcentaje();

		// Mostrar diálogo de confirmación
		mostrarDialogoConfirmacion(porcentaje);
	}

	private void mostrarDialogoConfirmacion(BigDecimal porcentaje) {
		Dialog confirmDialog = new Dialog();
		confirmDialog.setCloseOnEsc(true);
		confirmDialog.setCloseOnOutsideClick(false);

		// Crear header con icono
		Icon warningIcon = VaadinIcon.WARNING.create();
		warningIcon.getStyle()
		.set("color", "var(--lumo-error-color)")
		.set("margin-right", "0.5em");

		Span title = new Span("Confirmar Actualización de Precio");
		title.getStyle()
		.set("font-weight", "bold")
		.set("font-size", "var(--lumo-font-size-l)");

		HorizontalLayout headerLayout = new HorizontalLayout(warningIcon, title);
		headerLayout.setAlignItems(Alignment.CENTER);
		headerLayout.setSpacing(false);
		headerLayout.setPadding(true);

		confirmDialog.setHeaderTitle("Confirmar Actualización de Precio");

		// Formatear el porcentaje
		String signo = porcentaje.compareTo(BigDecimal.ZERO) > 0 ? "+" : "";
		String porcentajeTexto = signo + porcentaje.toString() + "%";

		// Calcular nuevo precio
		BigDecimal precioActual = articuloActual.getPrecioCosto();
		BigDecimal factor = BigDecimal.ONE.add(
				porcentaje.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
				);
		BigDecimal nuevoPrecio = precioActual.multiply(factor)
				.setScale(2, RoundingMode.HALF_UP);

		// Contenido del diálogo
		VerticalLayout contenido = new VerticalLayout();
		contenido.setSpacing(true);
		contenido.setPadding(false);

		// Texto principal
		Span textoPrincipal = new Span("Está a punto de actualizar el precio del siguiente artículo:");
		textoPrincipal.getStyle()
		.set("font-weight", "600")
		.set("color", "var(--lumo-header-text-color)");


		// Detalles del artículo
		UnorderedList lista = new UnorderedList(
				new ListItem(String.format("Código de Barras: %s", codigoBarraField.getValue())),
				new ListItem(String.format("Descripción: %s", articuloActual.getDescripcion())),
				new ListItem(String.format("Precio Actual: $%.2f", precioActual)),
				new ListItem(String.format("Porcentaje de Cambio: %s", porcentajeTexto)),
				new ListItem(String.format("Nuevo Precio: $%.2f", nuevoPrecio))
				);
		lista.getStyle()
		.set("margin-left", "1.5em")
		.set("color", "var(--lumo-secondary-text-color)");

		// Pregunta final
		Span pregunta = new Span("¿Está seguro que desea continuar?");
		pregunta.getStyle()
		.set("font-weight", "500")
		.set("color", "var(--lumo-error-color)")
		.set("margin-top", "1em");

		contenido.add(textoPrincipal, lista, pregunta);

		// Botones del diálogo
		Button confirmButton = new Button("Confirmar Actualización", _ -> {
			ejecutarActualizacion(porcentaje);
			confirmDialog.close();
		});
		confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

		Button cancelButton = new Button("Cancelar", _ -> confirmDialog.close());
		cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		HorizontalLayout dialogButtons = new HorizontalLayout(confirmButton, cancelButton);
		dialogButtons.setSpacing(true);

		confirmDialog.add(contenido, dialogButtons);
		confirmDialog.open();
	}

	private void ejecutarActualizacion(BigDecimal porcentaje) {
		try {
			int actualizados = actualizarPreciosUseCase.actualizarPrecioPorCodigoBarra(
					codigoBarraField.getValue().trim(), 
					porcentaje
					);

			if (actualizados > 0) {
				Notification successNotification = Notification.show(
						"✓ Precio actualizado exitosamente",
						5000,
						Notification.Position.BOTTOM_END
						);
				successNotification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

				limpiarFormulario();
			} else {
				Notification errorNotification = Notification.show(
						"✗ No se pudo actualizar el precio",
						5000,
						Notification.Position.BOTTOM_END
						);
				errorNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		} catch (Exception e) {
			Notification errorNotification = Notification.show(
					String.format("✗ Error: %s", e.getMessage()),
					5000,
					Notification.Position.BOTTOM_END
					);
			errorNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
	}

	@Override
	public void limpiarFormulario() {
		codigoBarraField.clear();
		limpiarCamposArticulo();
		porcentajeField.clear();
		porcentajeField.setEnabled(false);
		nuevoPrecioField.clear();
		nuevoPrecioField.setVisible(false);

		if (binder != null) {
			binder.readBean(new PorcentajeBean());
		}
	}

	// Clase interna para validación
	private static class PorcentajeBean {
		private BigDecimal porcentaje = BigDecimal.ZERO;

		public BigDecimal getPorcentaje() {
			return porcentaje;
		}

		public void setPorcentaje(BigDecimal porcentaje) {
			this.porcentaje = porcentaje;
		}
	}

}
