package com.visus.central.ui.view;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.visus.central.domain.model.Cliente;
import com.visus.central.domain.model.ClienteComboDTO;
import com.visus.central.domain.model.Coeficiente;
import com.visus.central.domain.model.Item;
import com.visus.central.domain.model.ItemPago;
import com.visus.central.domain.model.TipoPago;
import com.visus.central.domain.model.Vendedor;
import com.visus.central.domain.model.Venta;
import com.visus.central.domain.port.in.FacturacionUseCase;
import com.visus.central.domain.port.out.ClienteRepository;
import com.visus.central.domain.port.out.CoeficienteRepository;
import com.visus.central.domain.port.out.GrupoFamRepository;
import com.visus.central.domain.port.out.ReportGenerator;
import com.visus.central.domain.port.out.TipoPagoRepository;
import com.visus.central.domain.port.out.VendedorRepository;
import com.visus.central.ui.component.CentralLayout;

import jakarta.annotation.security.PermitAll;

@Route(value = "facturacion", layout = CentralLayout.class)
@PageTitle("Facturación")
@PermitAll
public class FacturacionView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final FacturacionUseCase facturacionService;
	private final ReportGenerator reportGenerator;
	private final ClienteRepository clienteRepository;
	private final TipoPagoRepository tipoPagoRepository;
	private final GrupoFamRepository grupoFamRepository;
	private final VendedorRepository vendedorRepository;
	private final CoeficienteRepository coeficienteRepository;

	private Venta ventaActual;

	// Componentes UI
	private Button grabarBtn;
	private Button cancelarBtn;
	private Button agregarBtn;
	private Button dtoBtn;
	private Button notaBtn;
	private DatePicker fechaField;
	private ComboBox<ClienteComboDTO> clientesCombo;
	private ComboBox<Vendedor> vendedorCombo;
	private Checkbox incluirFamiliaCheckbox;
	private Checkbox imprimirCheck;
	private IntegerField cantidadField;
	private Grid<Item> grid;
	private TextField subTotalField;
	private TextField porcentajeField;
	private TextField totalField;
	private TextField codigoBarraField;
	private Span subtotalLabel;
	private Span descuentoLabel;
	private Span totalLabel;
	private Span cajaStatusLabel;

	@Autowired
	public FacturacionView(FacturacionUseCase facturacionService, ClienteRepository clienteRepository,
			GrupoFamRepository grupoFamRepository, VendedorRepository vendedorRepository,
			ReportGenerator reportGenerator, CoeficienteRepository coeficienteRepository,
			TipoPagoRepository tipoPagoRepository) {
		this.facturacionService = facturacionService;
		this.clienteRepository = clienteRepository;
		this.tipoPagoRepository = tipoPagoRepository;
		this.grupoFamRepository = grupoFamRepository;
		this.vendedorRepository = vendedorRepository;
		this.reportGenerator = reportGenerator;
		this.coeficienteRepository = coeficienteRepository;

		initUI();
		cargarCombos();
		nuevaVenta();
		validarEstadoCaja();
	}

	private void initUI() {
		setSizeFull();
		setWidthFull();
		setPadding(true);
		setMargin(false);
		setSpacing(true);

		cajaStatusLabel = new Span();
		cajaStatusLabel.getStyle().set("font-weight", "bold").set("margin-left", "20px");

		// Fecha
		fechaField = new DatePicker("Fecha");
		fechaField.setValue(LocalDate.now());
		fechaField.setRequired(true);
		fechaField.setWidth("180px");
		fechaField.addValueChangeListener(_ -> validarFecha());

		// Checkbox para grupo familiar
		incluirFamiliaCheckbox = new Checkbox("Ver Familiares");
		incluirFamiliaCheckbox.addValueChangeListener(_ -> cargarComboClientes());

		// Combo de clientes (tipo Object para aceptar Cliente y GrupoFam)
		clientesCombo = new ComboBox<>("Cliente/Grupo Familiar");
		clientesCombo.setWidth("380px");
		clientesCombo.setRequired(true);
		clientesCombo.setItemLabelGenerator(dto -> dto.getNombre() + " (" + dto.getTipo() + ")");

		clientesCombo.addValueChangeListener(event -> {
			ClienteComboDTO dto = event.getValue();
			if (dto != null) {
				if ("FAMILIAR".equals(dto.getTipo())) {
					// Para un familiar: el cliente es el TITULAR, y guardamos el ID del familiar
					Cliente titular = clienteRepository.findById(dto.getIdClienteAsociado()).orElse(null);
					ventaActual.setCliente(titular);
					ventaActual.setGrupoFamiliarId(dto.getId()); // ID del familiar
				} else {
					// Para un cliente normal: no hay grupo familiar
					Cliente cliente = clienteRepository.findById(dto.getIdClienteAsociado()).orElse(null);
					ventaActual.setCliente(cliente);
					ventaActual.setGrupoFamiliarId(null);
				}
				validarClienteHabilitado();
			}
		});

		// Vendedor
		vendedorCombo = new ComboBox<>("Vendedor");
		vendedorCombo.setItemLabelGenerator(Vendedor::getNombre);
		vendedorCombo.setRequired(true);
		vendedorCombo.setWidth("320px");

		Button inicioBtn = new Button("Inicio", new Icon(VaadinIcon.HOME));
		inicioBtn.addClassName("btn-volver-home");
		inicioBtn.addClickListener(_ -> UI.getCurrent().navigate(""));

		HorizontalLayout leftSide = new HorizontalLayout(fechaField, clientesCombo, incluirFamiliaCheckbox,
				vendedorCombo, inicioBtn);
		leftSide.setAlignItems(Alignment.END);
		
		Div filler = new Div();
		filler.setWidthFull();
		filler.getStyle().set("flex-grow", "1"); // fuerza a ocupar todo el espacio

		HorizontalLayout headerLayout = new HorizontalLayout(leftSide, filler, cajaStatusLabel);
		headerLayout.setWidthFull();
		headerLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
		headerLayout.setFlexGrow(1, filler); // filler crece para empujar comprobanteVertical a la derecha
		headerLayout.setMargin(false);
		headerLayout.setPadding(false);
		headerLayout.getStyle().set("gap", "0"); // elimina espacio entre elementos
		add(headerLayout);

		// Sección de artículos
		codigoBarraField = new TextField("Código de barras");
		codigoBarraField.setWidth("300px");

		cantidadField = new IntegerField("Cantidad");
		cantidadField.setValue(1); // Valor por defecto
		cantidadField.setStep(1); // Incremento/decremento de 1 unidad
		cantidadField.setStepButtonsVisible(true); // Muestra botones + y -
		cantidadField.setMin(1); // Valor mínimo 1 (evitar cantidades negativas o cero)
		cantidadField.setWidth("100px");
		cantidadField.addClassName("custom-integer-field");

		agregarBtn = new Button("Agregar Item", _ -> agregarItem());
		agregarBtn.setIcon(new Icon(VaadinIcon.PLUS));
		agregarBtn.addClassName("btn-nuevo");

		// Navegación por Enter (simula Tab)
		clientesCombo.getElement().addEventListener("keydown", _ -> {
			if (!clientesCombo.isOpened()) vendedorCombo.focus();
		}).setFilter("event.key === 'Enter'");
		vendedorCombo.getElement().addEventListener("keydown", _ -> {
			if (!vendedorCombo.isOpened()) codigoBarraField.focus();
		}).setFilter("event.key === 'Enter'");
		codigoBarraField.addKeyPressListener(Key.ENTER, _ -> cantidadField.focus());
		cantidadField.addKeyPressListener(Key.ENTER, _ -> agregarBtn.focus());

		HorizontalLayout articuloLayout = new HorizontalLayout(codigoBarraField, cantidadField, agregarBtn);
		articuloLayout.setAlignItems(Alignment.BASELINE);
		articuloLayout.setSpacing(true);

		// Grid de ítems
		grid = new Grid<>(Item.class, false);
		grid.addColumn(item -> item.getArticulo().getDescripcion()).setHeader("Producto").setWidth("1000px")
				.setFlexGrow(0) // no se expande
				.setResizable(true);
		grid.addColumn(Item::getCantidad).setHeader("Cantidad").setWidth("110px").setFlexGrow(0).setResizable(true)
				.setTextAlign(ColumnTextAlign.END);
		grid.addColumn(item -> String.format("%.2f", item.getPrecioUnitario())).setHeader("Precio Unitario")
				.setWidth("160px").setFlexGrow(0).setResizable(true).setTextAlign(ColumnTextAlign.END);
		grid.addColumn(item -> String.format("%.2f",
				item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad())))).setHeader("Total")
				.setWidth("160px").setFlexGrow(1).setResizable(true).setTextAlign(ColumnTextAlign.END);
		grid.addComponentColumn(item -> {
			Button deleteBtn = new Button(new Icon(VaadinIcon.TRASH));
			deleteBtn.addClassName("btn-accion-icono");
			deleteBtn.getStyle().set("margin", "0 auto"); // centrar el botón dentro de la celda
			deleteBtn.addClickListener(_ -> {
				int index = ventaActual.getItems().indexOf(item);
				facturacionService.eliminarItem(ventaActual, index);
				actualizarUI();
				Notification.show("Ítem eliminado", 2000, Notification.Position.MIDDLE);
			});
			return deleteBtn;
		}).setHeader("Acciones").setWidth("100px") // ancho fijo pequeño
				.setFlexGrow(1).setResizable(true).setTextAlign(ColumnTextAlign.CENTER); // centra el contenido de la
																							// celda

		// Opcional: evitar que el usuario reordene las columnas (así Acciones siempre
		// queda a la derecha)
		grid.setColumnReorderingAllowed(false);

		grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		grid.setSelectionMode(Grid.SelectionMode.NONE);
		grid.setHeight("300px");

		// Aplicar estilos CSS
		grid.addClassName("grid-documentacion-dark");

		// Subtotal
		subtotalLabel = new Span("Subtotal:");
		subtotalLabel.getStyle().set("font-weight", "bold");
		subTotalField = new TextField();
		subTotalField.getStyle().set("margin-right", "8px");
		subTotalField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);

		Div spacerSub = new Div();
		HorizontalLayout areaSubTotales = new HorizontalLayout(spacerSub, subtotalLabel, subTotalField);
		areaSubTotales.setWidthFull();
		areaSubTotales.setFlexGrow(1, spacerSub); // spacer se expande, empuja el resto a la derecha
		areaSubTotales.setAlignItems(Alignment.BASELINE); // alinea verticalmente

		// Descuento
		descuentoLabel = new Span("Descuento: 0%");
		descuentoLabel.getStyle().set("font-weight", "bold");
		porcentajeField = new TextField();
		porcentajeField.getStyle().set("margin-right", "8px");
		porcentajeField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);

		Div spacerDesc = new Div();
		HorizontalLayout areaDescuentos = new HorizontalLayout(spacerDesc, descuentoLabel, porcentajeField);
		areaDescuentos.setWidthFull();
		areaDescuentos.setFlexGrow(1, spacerDesc);
		areaDescuentos.setAlignItems(Alignment.BASELINE);

		// A Pagar
		totalLabel = new Span("A Pagar:");
		totalLabel.getStyle().set("font-weight", "bold");
		totalField = new TextField();
		totalField.getStyle().set("margin-right", "8px");
		totalField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
		totalField.setWidth("160px");
		Div spacerTot = new Div();
		HorizontalLayout areaTotales = new HorizontalLayout(spacerTot, totalLabel, totalField);
		areaTotales.setWidthFull();
		areaTotales.setFlexGrow(1, spacerTot);
		areaTotales.setAlignItems(Alignment.BASELINE);

		// Contenedor vertical para los tres labels y sus textfields
		VerticalLayout totalesVertical = new VerticalLayout(areaSubTotales, areaDescuentos, areaTotales);
		totalesVertical.setSpacing(false);
		totalesVertical.setPadding(true);
		totalesVertical.setJustifyContentMode(JustifyContentMode.END);

		Div spacer = new Div();
		spacer.setSizeFull();

		// Botón Descuento
		dtoBtn = new Button();
		dtoBtn.setIcon(new Icon(VaadinIcon.BOOK_PERCENT));
		dtoBtn.setTooltipText("Aplicar descuento a la venta");
		dtoBtn.addClassName("btn-accion-icono"); // opcional: estilo sin bordes
		dtoBtn.addClickListener(_ -> abrirDialogoDescuento());

		// Botón Observaciones
		notaBtn = new Button();
		notaBtn.setIcon(new Icon(VaadinIcon.COMMENT));
		notaBtn.setTooltipText("Agregar observaciones a la venta");
		notaBtn.addClassName("btn-accion-icono");
		notaBtn.addClickListener(_ -> abrirDialogoNota());

		HorizontalLayout footerTotales = new HorizontalLayout(dtoBtn, notaBtn, spacer, totalesVertical);
		footerTotales.setWidthFull();
		footerTotales.setFlexGrow(1, spacer); // el spacer se expande
		footerTotales.setMargin(false);
		footerTotales.setPadding(false);

		// Checkbox, grabar y cancelar
		imprimirCheck = new Checkbox("Imprimir");
		imprimirCheck.setValue(true);

		grabarBtn = new Button("Grabar", _ -> grabarVenta());
		grabarBtn.addClassName("btn-volver");
		grabarBtn.setTooltipText("Graba la Venta");

		cancelarBtn = new Button("Cancelar", _ -> nuevaVenta());
		cancelarBtn.addClassName("btn-volver-home");
		cancelarBtn.setTooltipText("Cancela la Venta");

		HorizontalLayout footer = new HorizontalLayout(spacer, imprimirCheck, grabarBtn, cancelarBtn);
		footer.setWidthFull();
		footer.setFlexGrow(1, spacer); // el spacer se expande
		footer.setMargin(false);
		footer.setPadding(false);

		// Línea separadora (solo un segmento a la derecha)
		Hr separator = new Hr();
		separator.setWidth("300px"); // ancho fijo o porcentual
		separator.getStyle().set("margin", "6px 0"); // márgenes verticales

		Div separatorContainer = new Div(separator);
		separatorContainer.getStyle().set("display", "flex").set("justify-content", "flex-end").set("width", "100%")
				.set("border", "none").set("height", "2px").set("background-color", "var(--lumo-contrast-20pct)")
				.set("border-radius", "2px");

		// Organización del layout
		add(headerLayout, articuloLayout, grid, footerTotales, separatorContainer, footer);
	}

	private void cargarCombos() {
		vendedorCombo.setItems(vendedorRepository.findAll());
		// Cargar combo inicial (clientes)
		cargarComboClientes();
	}

	private void cargarComboClientes() {
		if (incluirFamiliaCheckbox.getValue()) {
			List<ClienteComboDTO> familiares = grupoFamRepository.findFamiliaresParaCombo();
			clientesCombo.setItems(familiares);
		} else {
			List<ClienteComboDTO> clientes = clienteRepository.findClientesParaCombo();
			clientesCombo.setItems(clientes);
		}
		clientesCombo.clear();
	}

	private void nuevaVenta() {
		ventaActual = facturacionService.iniciarNuevaVenta();
		actualizarUI();
		// Limpiar campos existentes...
		codigoBarraField.clear();
		cantidadField.setValue(1);
		clientesCombo.clear();
		vendedorCombo.clear();
		fechaField.setValue(LocalDate.now());

		Notification.show("Nueva venta iniciada", 2000, Notification.Position.MIDDLE);
		clientesCombo.focus();
	}

	private void agregarItem() {
		String codigo = codigoBarraField.getValue();
		if (codigo == null || codigo.isBlank()) {
			Notification.show("Ingrese un código de barras", 3000, Notification.Position.MIDDLE);
			return;
		}
		Integer cantidad;
		try {
			cantidad = cantidadField.getValue();
			if (cantidad <= 0)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			Notification.show("Cantidad inválida", 3000, Notification.Position.MIDDLE);
			return;
		}

		try {
			facturacionService.agregarItem(ventaActual, codigo, cantidad);
			actualizarUI();
			codigoBarraField.clear();
			cantidadField.setValue(1);
			Notification.show("Artículo agregado", 2000, Notification.Position.MIDDLE);
			codigoBarraField.focus();
		} catch (RuntimeException ex) {
			Notification.show(ex.getMessage(), 4000, Notification.Position.MIDDLE);
		}
	}

	private void abrirDialogoDescuento() {
		Dialog dialog = new Dialog();
		dialog.setHeaderTitle("Aplicar descuento");

		NumberField porcentajeFieldDialog = new NumberField("Porcentaje (%)");
		porcentajeFieldDialog.setMin(0);
		porcentajeFieldDialog.setMax(80);
		porcentajeFieldDialog.setStep(1);
		porcentajeFieldDialog.setStepButtonsVisible(true);
		porcentajeFieldDialog.addClassName("custom-integer-field");

		// Cargar el porcentaje actual si existe
		BigDecimal porcentajeActual = ventaActual.getBonificacion() != null ? ventaActual.getBonificacion()
				: BigDecimal.ZERO;
		porcentajeFieldDialog.setValue(porcentajeActual.doubleValue());

		Button aplicar = new Button("Aplicar", _ -> {
			try {
				Double pctDouble = porcentajeFieldDialog.getValue();
				if (pctDouble == null)
					return;
				BigDecimal pct = BigDecimal.valueOf(pctDouble);
				if (pct.compareTo(BigDecimal.ZERO) < 0 || pct.compareTo(new BigDecimal(100)) > 0) {
					Notification.show("Porcentaje debe estar entre 0 y 100", 3000, Notification.Position.MIDDLE);
					return;
				}
				facturacionService.aplicarDescuento(ventaActual, pct);
				actualizarUI();
				dialog.close();
			} catch (Exception ex) {
				Notification.show("Ingrese un número válido", 3000, Notification.Position.MIDDLE);
			}
		});
		Button cancelar = new Button("Cancelar", _ -> dialog.close());

		dialog.add(porcentajeFieldDialog);
		dialog.getFooter().add(cancelar, aplicar);
		dialog.open();
	}

	private void abrirDialogoNota() {
		Dialog dialog = new Dialog();
		dialog.setHeaderTitle("Observaciones");

		TextArea notaArea = new TextArea("Nota");
		notaArea.setWidthFull();
		Button guardar = new Button("Guardar", _ -> {
			ventaActual.setObservaciones(notaArea.getValue());
			dialog.close();
			Notification.show("Nota guardada", 2000, Notification.Position.MIDDLE);
		});
		Button cancelar = new Button("Cancelar", _ -> dialog.close());

		dialog.add(notaArea);
		dialog.getFooter().add(cancelar, guardar);
		dialog.open();
	}

	private void grabarVenta() {
		// Validar campos obligatorios
		ClienteComboDTO selected = clientesCombo.getValue();
		if (selected == null) {
			Notification.show("Seleccione un cliente o familiar", 3000, Notification.Position.MIDDLE);
			return;
		}

		if (vendedorCombo.getValue() == null) {
			Notification.show("Seleccione un vendedor", 3000, Notification.Position.MIDDLE);
			return;
		}

		if (ventaActual.getItems().isEmpty()) {
			Notification.show("Agregue al menos un ítem", 3000, Notification.Position.MIDDLE);
			return;
		}

		// Asignar cliente
		if ("FAMILIAR".equals(selected.getTipo())) {
			Cliente titular = clienteRepository.findById(selected.getIdClienteAsociado()).orElse(null);
			if (titular == null) {
				Notification.show("Titular no encontrado", 3000, Notification.Position.MIDDLE);
				return;
			}
			ventaActual.setCliente(titular);
			ventaActual.setGrupoFamiliarId(selected.getIdGrupoFam());
		} else {
			Cliente cliente = clienteRepository.findById(selected.getIdClienteAsociado()).orElse(null);
			if (cliente == null) {
				Notification.show("Cliente no encontrado", 3000, Notification.Position.MIDDLE);
				return;
			}
			ventaActual.setCliente(cliente);
			ventaActual.setGrupoFamiliarId(null);
		}

		ventaActual.setVendedor(vendedorCombo.getValue());
		ventaActual.setFechaVenta(fechaField.getValue());

		// Abrir diálogo de medios de pago
		abrirDialogoMediosPago();
	}

	private void actualizarUI() {
		grid.setItems(ventaActual.getItems());
		BigDecimal subtotal = facturacionService.calcularSubtotal(ventaActual);
		BigDecimal total = facturacionService.calcularTotal(ventaActual);
		BigDecimal bonificacion = ventaActual.getBonificacion() != null ? ventaActual.getBonificacion()
				: BigDecimal.ZERO;

		// Calcular monto del descuento
		BigDecimal montoDescuento = subtotal.multiply(bonificacion.divide(BigDecimal.valueOf(100)));

		subtotalLabel.setText("Subtotal:");
		subTotalField.setValue(String.format("%.2f", subtotal));
		descuentoLabel.setText("Descuento: " + bonificacion + "%");
		porcentajeField.setValue(String.format("%.2f", montoDescuento));
		totalLabel.setText("A Pagar:");
		totalField.setValue(String.format("%.2f", total));
	}

	private void validarFecha() {
		LocalDate fecha = fechaField.getValue();
		if (fecha.isAfter(LocalDate.now())) {
			Notification.show("La fecha no puede ser mayor a hoy", 3000, Notification.Position.MIDDLE);
			fechaField.setValue(LocalDate.now());
		}
		// La validación con la última fecha de factura se hará al guardar (en el caso
		// de uso)
	}

	private void validarClienteHabilitado() {
		ClienteComboDTO selected = clientesCombo.getValue(); // CORREGIDO
		if (selected != null && "CLIENTE".equals(selected.getTipo())) {
			Cliente c = clienteRepository.findById(selected.getIdClienteAsociado()).orElse(null);
			if (c != null && !c.isHabilitado()) {
				Notification.show("El cliente no está habilitado para comprar", 4000, Notification.Position.MIDDLE);
				clientesCombo.clear();
			}
		}
		// Los familiares no requieren validación de habilitado porque dependen del
		// titular
	}

	private void abrirDialogoMediosPago() {
		Dialog dialog = new Dialog();
		dialog.setHeaderTitle("Medios de Pago");
		dialog.setWidth("800px");
		dialog.setHeight("550px");
		dialog.setResizable(true);

		BigDecimal totalVenta = facturacionService.calcularTotal(ventaActual).setScale(2, RoundingMode.HALF_UP);
		List<ItemPago> mediosPago = new ArrayList<>();

		// Layout vertical para el contenido del diálogo
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		content.setPadding(true);

		// Mostrar total de la venta
		Span totalVentaLabel = new Span("Total de la venta: $" + String.format("%.2f", totalVenta));
		totalVentaLabel.getStyle().set("font-weight", "bold").set("font-size", "16px");
		content.add(totalVentaLabel);

		// Mostrar pendiente
		Span pendienteLabel = new Span("Pendiente: $" + String.format("%.2f", totalVenta));
		pendienteLabel.getStyle().set("font-weight", "bold");
		content.add(pendienteLabel);

		// Grid de medios de pago dentro del diálogo
		Grid<ItemPago> gridMediosPagoDialog = new Grid<>(ItemPago.class, false);
		gridMediosPagoDialog.addColumn(ip -> ip.getTipoPago().getDescripcion()).setHeader("Tipo de Pago")
				.setAutoWidth(true);
		gridMediosPagoDialog.addColumn(ip -> ip.getMonto()).setHeader("Monto").setAutoWidth(true);
		gridMediosPagoDialog.addColumn(ip -> ip.getCoeficiente() != null ? ip.getCoeficiente().getDescripcion() : "-")
				.setHeader("Coeficiente").setAutoWidth(true);
		gridMediosPagoDialog.addColumn(ip -> ip.getCantidadCuotas()).setHeader("Cuotas").setAutoWidth(true)
				.setFlexGrow(0);
		gridMediosPagoDialog.addComponentColumn(item -> {
			Button deleteBtn = new Button(new Icon(VaadinIcon.TRASH));
			deleteBtn.addClassName("btn-accion-icono");
			deleteBtn.addClickListener(_ -> {
				mediosPago.remove(item);
				gridMediosPagoDialog.setItems(mediosPago);
				// Actualizar pendiente directamente
				BigDecimal totalAsignado = mediosPago.stream().map(ItemPago::getMonto).reduce(BigDecimal.ZERO,
						BigDecimal::add);
				BigDecimal nuevoPendiente = totalVenta.subtract(totalAsignado);
				pendienteLabel.setText("Pendiente: $" + String.format("%.2f", nuevoPendiente));
				if (nuevoPendiente.compareTo(BigDecimal.ZERO) == 0) {
					pendienteLabel.getStyle().set("color", "green");
				} else {
					pendienteLabel.getStyle().set("color", "red");
				}
			});
			return deleteBtn;
		}).setHeader("Acciones").setAutoWidth(true);

		gridMediosPagoDialog.setHeight("180px");
		gridMediosPagoDialog.addClassName("grid-documentacion-dark");
		gridMediosPagoDialog.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		content.add(gridMediosPagoDialog);

		// Formulario para agregar medios de pago
		HorizontalLayout formLayout = new HorizontalLayout();
		formLayout.setAlignItems(Alignment.BASELINE);
		formLayout.setSpacing(true);
		formLayout.setWidthFull();

		ComboBox<TipoPago> tipoPagoCombo = new ComboBox<>("Tipo de Pago");
		tipoPagoCombo.setItemLabelGenerator(TipoPago::getDescripcion);
		tipoPagoCombo.setWidth("200px");
		tipoPagoCombo.setItems(tipoPagoRepository.findAll());

		ComboBox<Coeficiente> coeficienteCombo = new ComboBox<>("Coeficiente");
		coeficienteCombo.setItemLabelGenerator(c -> c.getDescripcion() + " - " + c.getCoeficiente() + "x");
		coeficienteCombo.setWidth("200px");
		coeficienteCombo.setVisible(false);

		BigDecimalField montoField = new BigDecimalField("Monto");
		montoField.setWidth("120px");
		montoField.setPrefixComponent(new Span("$"));

		Button btnAgregar = new Button("Agregar");
		btnAgregar.addClassName("btn-nuevo");

		btnAgregar.addClickListener(_ -> {
			TipoPago tipoPago = tipoPagoCombo.getValue();
			BigDecimal monto = montoField.getValue();
			Coeficiente coeficiente = coeficienteCombo.getValue();

			if (tipoPago == null || monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
				Notification.show("Complete tipo de pago y monto válido", 3000, Notification.Position.MIDDLE);
				return;
			}

			// Redondeo el monto a 2 decimales para evitar errores de precisión
			monto = monto.setScale(2, RoundingMode.HALF_UP);

			BigDecimal totalAsignadoActual = mediosPago.stream().map(ItemPago::getMonto).reduce(BigDecimal.ZERO,
					BigDecimal::add);

			BigDecimal pendiente = totalVenta.subtract(totalAsignadoActual);

			if (monto.compareTo(pendiente) > 0) {
				Notification.show("El monto excede el saldo pendiente ($" + String.format("%.2f", pendiente) + ")",
						3000, Notification.Position.MIDDLE);
				return;
			}

			ItemPago itemPago = new ItemPago(tipoPago, monto,
					(Boolean.TRUE.equals(tipoPago.getRequiere_coeficiente()) && coeficiente != null) ? coeficiente
							: null);
			mediosPago.add(itemPago);
			gridMediosPagoDialog.setItems(mediosPago);

			// Actualizar pendiente y estado del botón
			actualizarPendienteYBotones(totalVenta, mediosPago, pendienteLabel, btnAgregar);

			// Limpiar campos
			tipoPagoCombo.clear();
			coeficienteCombo.clear();
			coeficienteCombo.setVisible(false);
			montoField.clear();

			// Si aún hay pendiente, preseleccionar el monto pendiente
			BigDecimal nuevoPendiente = totalVenta
					.subtract(mediosPago.stream().map(ItemPago::getMonto).reduce(BigDecimal.ZERO, BigDecimal::add));
			if (nuevoPendiente.compareTo(BigDecimal.ZERO) > 0) {
				montoField.setValue(nuevoPendiente.setScale(2, RoundingMode.HALF_UP));
			} else {
				montoField.clear();
			}
		});
		btnAgregar.addClassName("btn-nuevo");

		// Listener para preseleccionar el monto pendiente cuando se selecciona un tipo
		// de pago
		tipoPagoCombo.addValueChangeListener(e -> {
			TipoPago tp = e.getValue();

			BigDecimal totalAsignado = mediosPago.stream().map(ItemPago::getMonto).reduce(BigDecimal.ZERO,
					BigDecimal::add);
			BigDecimal pendienteActual = totalVenta.subtract(totalAsignado);

			// Si el pendiente es mayor a 0, preseleccionar ese monto
			if (pendienteActual.compareTo(BigDecimal.ZERO) > 0) {
				// Formatear la visualización del campo a 2 decimales
				montoField.setValue(pendienteActual.setScale(2, RoundingMode.HALF_UP));
			}

			// Mostrar coeficiente si aplica
			if (tp != null && Boolean.TRUE.equals(tp.getRequiere_coeficiente())) {
				List<Coeficiente> coefs = coeficienteRepository.findByTipoPagoId(tp.getId());
				coeficienteCombo.setItems(coefs);
				coeficienteCombo.setVisible(true);
				coeficienteCombo.setRequired(true);
				coeficienteCombo.clear();
			} else {
				coeficienteCombo.setVisible(false);
				coeficienteCombo.setRequired(false);
				coeficienteCombo.clear();
			}
		});

		formLayout.add(tipoPagoCombo, coeficienteCombo, montoField, btnAgregar);
		content.add(formLayout);

		// Botones de acción del diálogo
		Button btnCancelar = new Button("Cancelar", _ -> dialog.close());
		btnCancelar.addClassName("btn-volver");

		Button btnGuardar = new Button("Guardar Venta", _ -> {
			if (mediosPago.isEmpty()) {
				Notification.show("Agregue al menos un medio de pago", 3000, Notification.Position.MIDDLE);
				return;
			}

			BigDecimal totalAsignadoFinal = mediosPago.stream().map(ItemPago::getMonto).reduce(BigDecimal.ZERO,
					BigDecimal::add);

			if (totalAsignadoFinal.compareTo(totalVenta) != 0) {
				Notification.show(
						"El total de los medios de pago ($" + String.format("%.2f", totalAsignadoFinal)
								+ ") no coincide con el total de la venta ($" + String.format("%.2f", totalVenta) + ")",
						5000, Notification.Position.MIDDLE);
				return;
			}

			ventaActual.setMediosPago(mediosPago);

			try {
				facturacionService.guardarVenta(ventaActual, imprimirCheck.getValue());

				if (imprimirCheck.getValue()) {
					try {
						byte[] pdfBytes = reportGenerator.generarReporteRemito(ventaActual);
						if (pdfBytes == null || pdfBytes.length == 0) {
							Notification.show("Error: El reporte generado está vacío", 5000, Position.MIDDLE);
							return;
						}

						// Convertir el array de bytes a una cadena Base64 para pasarlo a JavaScript
						String base64Pdf = java.util.Base64.getEncoder().encodeToString(pdfBytes);
						String fileName = "remito_" + ventaActual.getNumeroComprobante() + ".pdf";

						// Ejecutar JavaScript para crear el blob y forzar la descarga
						UI.getCurrent().getPage().executeJs(
								"var byteCharacters = atob($0);" + "var byteNumbers = new Array(byteCharacters.length);"
										+ "for (var i = 0; i < byteCharacters.length; i++) {"
										+ "    byteNumbers[i] = byteCharacters.charCodeAt(i);" + "}"
										+ "var byteArray = new Uint8Array(byteNumbers);"
										+ "var blob = new Blob([byteArray], {type: 'application/pdf'});"
										+ "var link = document.createElement('a');"
										+ "link.href = URL.createObjectURL(blob);" + "link.download = $1;"
										+ "document.body.appendChild(link);" + "link.click();"
										+ "document.body.removeChild(link);" + "URL.revokeObjectURL(link.href);",
								base64Pdf, fileName);

						Notification.show("Venta registrada y comprobante descargado", 3000, Position.MIDDLE);
					} catch (Exception ex) {
						Notification.show("Error al generar el comprobante: " + ex.getMessage(), 5000, Position.MIDDLE);
						ex.printStackTrace();
					}
				}

				dialog.close();
				nuevaVenta();

			} catch (RuntimeException ex) {
				Notification.show("Error al guardar: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
			}
		});
		btnGuardar.addClassName("btn-nuevo");

		dialog.getFooter().add(btnCancelar, btnGuardar);
		dialog.add(content);
		dialog.open();
	}

	/**
	 * Actualiza el label de pendiente y el estado del botón Agregar
	 */
	private void actualizarPendienteYBotones(BigDecimal totalVenta, List<ItemPago> mediosPago, Span pendienteLabel,
			Button btnAgregar) {

		BigDecimal totalAsignado = mediosPago.stream().map(ItemPago::getMonto).reduce(BigDecimal.ZERO, BigDecimal::add)
				.setScale(2, RoundingMode.HALF_UP);

		BigDecimal nuevoPendiente = totalVenta.subtract(totalAsignado).setScale(2, RoundingMode.HALF_UP);

		pendienteLabel.setText("Pendiente: $" + String.format("%.2f", nuevoPendiente));

		if (nuevoPendiente.compareTo(BigDecimal.ZERO) == 0) {
			pendienteLabel.getStyle().set("color", "green");
			btnAgregar.setEnabled(false); // Deshabilitar botón cuando no hay pendiente
			btnAgregar.setTooltipText("No hay saldo pendiente para asignar");
		} else {
			pendienteLabel.getStyle().set("color", "red");
			btnAgregar.setEnabled(true); // Habilitar botón
			btnAgregar.setTooltipText("Agregar medio de pago");
		}
	}

	private void validarEstadoCaja() {
		boolean hayCaja = facturacionService.hayCajaAbierta();
		boolean habilitado = hayCaja;

		agregarBtn.setEnabled(habilitado);
		grabarBtn.setEnabled(habilitado);
		dtoBtn.setEnabled(habilitado);
		notaBtn.setEnabled(habilitado);
		codigoBarraField.setEnabled(habilitado);
		cantidadField.setEnabled(habilitado);
		clientesCombo.setEnabled(habilitado);
		vendedorCombo.setEnabled(habilitado);
		imprimirCheck.setEnabled(habilitado);

		if (hayCaja) {
			cajaStatusLabel.setText("CAJA ABIERTA");
			cajaStatusLabel.getStyle().set("color", "#2e7d32");
		} else {
			cajaStatusLabel.setText("CAJA CERRADA");
			cajaStatusLabel.getStyle().set("color", "#c62828");
			Notification
					.show("No hay caja abierta. No se pueden registrar ventas.", 5000, Notification.Position.TOP_CENTER)
					.addThemeVariants(NotificationVariant.LUMO_WARNING);
		}

	}

}
