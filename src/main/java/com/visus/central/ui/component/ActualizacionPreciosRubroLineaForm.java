package com.visus.central.ui.component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.combobox.ComboBox;
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
import com.visus.central.domain.model.Linea;
import com.visus.central.domain.model.Rubro;
import com.visus.central.domain.port.in.ActualizarPreciosUseCase;
import com.visus.central.domain.port.in.LineaUseCase;
import com.visus.central.domain.port.in.RubroUseCase;
import com.visus.central.domain.port.out.ArticuloRepository;

public class ActualizacionPreciosRubroLineaForm extends AbstractProcessForm {

	private static final long serialVersionUID = 1L;

	private RubroUseCase obtenerRubrosUseCase;
	private LineaUseCase obtenerLineasUseCase;
	private ActualizarPreciosUseCase actualizarPreciosUseCase;
	private ArticuloRepository articuloRepository;

	private ComboBox<Rubro> rubroComboBox;
	private CheckboxGroup<Linea> lineaCheckboxGroup;
	private TextField porcentajeField;
	private Span contadorArticulos;
	private Binder<PorcentajeBean> binder;
	// Listas completas para filtrar
	private List<Rubro> todosLosRubros = new ArrayList<>();

	public ActualizacionPreciosRubroLineaForm(RubroUseCase obtenerRubrosUseCase, LineaUseCase obtenerLineasUseCase,
			ActualizarPreciosUseCase actualizarPreciosUseCase, ArticuloRepository articuloRepository) {

		this.obtenerLineasUseCase = obtenerLineasUseCase;
		this.obtenerRubrosUseCase = obtenerRubrosUseCase;
		this.actualizarPreciosUseCase = actualizarPreciosUseCase;
		this.articuloRepository = articuloRepository;

		// Configurar título específico
		//    	setTitulo("Actualización Masiva de Precios");
		cargarRubros();
	}

	@Override
	protected void configurarContenido() {

		// Inicializar componentes
		rubroComboBox = new ComboBox<>("Seleccione un Rubro");

		System.out.println("Estoy en configurarContenido() antes de cargarRubros() ");

		rubroComboBox.setItemLabelGenerator(Rubro::getDescripcion);
		rubroComboBox.setWidth("250px");
		rubroComboBox.setRequired(true);

		lineaCheckboxGroup = new CheckboxGroup<>("Líneas (puede elegir varias)");
		lineaCheckboxGroup.setItemLabelGenerator(Linea::getDescripcion);
		lineaCheckboxGroup.setWidth("300px");
		lineaCheckboxGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);

		porcentajeField = new TextField("Porcentaje (%)");
		porcentajeField.setWidth("120px");
		Tooltip.forComponent(porcentajeField)
		.withText("Ej: 10 (aumento) o -5 (disminución)")
		.withPosition(Tooltip.TooltipPosition.TOP);
		porcentajeField.setRequired(true);

		contadorArticulos = new Span();
		contadorArticulos.getStyle()
		.set("font-weight", "bold")
		.set("color", "var(--lumo-primary-color)")
		.set("margin-top", "1em");

		// Configurar validación
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

		// Eventos
		rubroComboBox.addValueChangeListener(e -> {
			if (e.getValue() != null) {
				cargarLineasPorRubro(e.getValue().getId());
				actualizarContador();
			} else {
				lineaCheckboxGroup.setItems(List.of());
				contadorArticulos.setText("");
			}
		});

		lineaCheckboxGroup.addValueChangeListener(_ -> actualizarContador());

		// Agregar componentes al layout de contenido
		getContenidoLayout().add(
				rubroComboBox,
				lineaCheckboxGroup,
				porcentajeField,
				contadorArticulos
				);
	}

	private void cargarRubros() {
		try {
			// Cargar rubros y ordenarlos alfabéticamente
			todosLosRubros = obtenerRubrosUseCase.listar();
			List<Rubro> rubrosOrdenados = todosLosRubros.stream()
					.sorted((r1, r2) -> r1.getDescripcion().compareToIgnoreCase(r2.getDescripcion()))
					.collect(Collectors.toList());
			rubroComboBox.setItems(rubrosOrdenados);
		} catch (Exception e) {
			Notification.show("Error al cargar rubros: " + e.getMessage(),
					5000, Notification.Position.MIDDLE);
		}
	}

	private void cargarLineasPorRubro(Integer rubroId) {
		try {
			List<Linea> lineas = obtenerLineasUseCase.findByRubroId(rubroId);
			List<Linea> lineasOrdenadas = lineas.stream()
					.sorted((l1, l2) -> l1.getDescripcion().compareToIgnoreCase(l2.getDescripcion()))
					.collect(Collectors.toList());
			lineaCheckboxGroup.setItems(lineasOrdenadas);
			lineaCheckboxGroup.deselectAll();
		} catch (Exception e) {
			Notification.show("Error al cargar líneas: " + e.getMessage(),
					5000, Notification.Position.MIDDLE);
		}
	}

	private void actualizarContador() {
		if (rubroComboBox.getValue() == null || lineaCheckboxGroup.getValue().isEmpty()) {
			contadorArticulos.setText("");
			return;
		}

		try {
			Rubro rubro = rubroComboBox.getValue();
			List<Integer> lineasIds = lineaCheckboxGroup.getValue().stream()
					.map(Linea::getIdLinea)
					.collect(Collectors.toList());

			int cantidad = articuloRepository.countByRubroIdAndLineaIdIn(rubro.getId(), lineasIds);

			contadorArticulos.setText(
					String.format("Se afectarán %d artículos con los criterios seleccionados", cantidad)
					);
		} catch (Exception e) {
			contadorArticulos.setText("Error al calcular cantidad de artículos");
		}
	}

	@Override
	protected boolean validarFormulario() {
		// Validar que se haya seleccionado un rubro
		if (rubroComboBox.getValue() == null) {
			Notification.show("Debe seleccionar un rubro", 3000, Notification.Position.MIDDLE);
			rubroComboBox.focus();
			return false;
		}

		// Validar que se haya seleccionado al menos una línea
		if (lineaCheckboxGroup.getValue().isEmpty()) {
			Notification.show("Debe seleccionar al menos una línea", 3000, Notification.Position.MIDDLE);
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
		// Este método se llama cuando se presiona Aceptar y la validación es exitosa
		// Aquí mostramos el diálogo de confirmación

		Rubro rubro = rubroComboBox.getValue();
		List<Linea> lineasSeleccionadas = lineaCheckboxGroup.getValue().stream()
				.collect(Collectors.toList());
		List<Integer> lineasIds = lineasSeleccionadas.stream()
				.map(Linea::getIdLinea)
				.collect(Collectors.toList());

		// Obtener el porcentaje
		PorcentajeBean bean = new PorcentajeBean();
		try {
			binder.writeBean(bean);
		} catch (ValidationException e) {
			// Esto no debería ocurrir porque ya se validó, pero por seguridad
			Notification.show("Error inesperado al obtener el porcentaje", 3000, Notification.Position.MIDDLE);
			return;
		}

		BigDecimal porcentaje = bean.getPorcentaje();

		// Mostrar diálogo de confirmación
		mostrarDialogoConfirmacion(rubro, lineasSeleccionadas, lineasIds, porcentaje);
	}

	private void mostrarDialogoConfirmacion(Rubro rubro, List<Linea> lineasSeleccionadas, 
			List<Integer> lineasIds, BigDecimal porcentaje) {

		Dialog confirmDialog = new Dialog();
		confirmDialog.setCloseOnEsc(true);
		confirmDialog.setCloseOnOutsideClick(false);

		// Crear header personalizado con icono - FORMA CORRECTA
		Icon warningIcon = VaadinIcon.WARNING.create();
		warningIcon.getStyle()
		.set("color", "var(--lumo-error-color)")
		.set("margin-right", "0.5em");

		Span title = new Span("Confirmar Actualización de Precios");
		title.getStyle()
		.set("font-weight", "bold")
		.set("font-size", "var(--lumo-font-size-l)");

		HorizontalLayout headerLayout = new HorizontalLayout(warningIcon, title);
		headerLayout.setAlignItems(Alignment.CENTER);
		headerLayout.setSpacing(false);
		headerLayout.setPadding(true);

		// FORMA 1: Usar setHeaderTitle para el texto y agregar icono después
		confirmDialog.setHeaderTitle("Confirmar Actualización de Precios");
		// No podemos modificar fácilmente el header existente, así que mejor usamos Forma 2

		// FORMA 2: Crear un Div personalizado como header
		confirmDialog.removeAll();
		confirmDialog.add(headerLayout);

		// Formatear el porcentaje
		String signo = porcentaje.compareTo(BigDecimal.ZERO) > 0 ? "+" : "";
		String porcentajeTexto = signo + porcentaje.toString() + "%";

		// Lista de líneas
		String nombresLineas = lineasSeleccionadas.stream()
				.map(Linea::getDescripcion)
				.collect(Collectors.joining(", "));


		// Obtener cantidad de artículos
		int cantidadArticulos = articuloRepository.countByRubroIdAndLineaIdIn(rubro.getId(), lineasIds);

		// Contenido del diálogo
		VerticalLayout contenido = new VerticalLayout();
		contenido.setSpacing(true);
		contenido.setPadding(false);

		// Texto principal
		Span textoPrincipal = new Span("Está a punto de actualizar los precios de los artículos.");
		textoPrincipal.getStyle()
		.set("font-weight", "600")
		.set("color", "var(--lumo-header-text-color)");

		// Detalles en una lista
		UnorderedList lista = new UnorderedList(
				new ListItem(String.format("Rubro: %s", rubro.getDescripcion())),
				new ListItem(String.format("Líneas seleccionadas: %s", nombresLineas)),
				new ListItem(String.format("Porcentaje de cambio: %s", porcentajeTexto)),
				new ListItem(String.format("Total de artículos afectados: %d", cantidadArticulos))
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
			ejecutarActualizacion(rubro.getId(), lineasIds, porcentaje);
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

	private void ejecutarActualizacion(Integer rubroId, List<Integer> lineasIds, BigDecimal porcentaje) {
		try {
			int actualizados = actualizarPreciosUseCase.actualizarPreciosPorRubroYLineas(
					rubroId, lineasIds, porcentaje);

			// NOTIFICACIÓN DE ÉXITO REAL - mejorada
			Notification successNotification = Notification.show(
					String.format("✓ Se actualizaron exitosamente %d artículos", actualizados),
					5000,
					Notification.Position.BOTTOM_END
					);
			successNotification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

			limpiarFormulario();
		} catch (Exception e) {
			// NOTIFICACIÓN DE ERROR - mejorada
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
		rubroComboBox.clear();
		lineaCheckboxGroup.setItems(List.of());
		lineaCheckboxGroup.deselectAll();
		porcentajeField.clear();
		contadorArticulos.setText("");
		binder.readBean(new PorcentajeBean());
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
