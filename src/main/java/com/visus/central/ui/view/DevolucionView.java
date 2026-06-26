package com.visus.central.ui.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.visus.central.domain.model.ClienteComboDTO;
import com.visus.central.domain.model.Item;
import com.visus.central.domain.model.Venta;
import com.visus.central.domain.port.in.DevolucionUseCase;
import com.visus.central.domain.port.out.ClienteRepository;
import com.visus.central.domain.port.out.UserRepository;
import com.visus.central.infraestructure.security.SecurityUtils;
import com.visus.central.ui.component.CentralLayout;

import jakarta.annotation.security.PermitAll;

@Route(value = "devoluciones", layout = CentralLayout.class)
@PageTitle("Devoluciones")
@PermitAll
public class DevolucionView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final DevolucionUseCase devolucionUseCase;
	private final ClienteRepository clienteRepository;
	private final UserRepository userRepository;

	private final TextField txtCodigoBarra = new TextField("Código de Barra");
	private final ComboBox<ClienteComboDTO> cmbCliente = new ComboBox<>("Cliente");
	private final ComboBox<Integer> cmbDiasDevolucion = new ComboBox<>("Política devolución (días)");
	private final Grid<Venta> gridVentas = new Grid<>(Venta.class, false);
	private final Grid<ItemReturnRow> gridItems = new Grid<>(ItemReturnRow.class, false);
	private final Button btnBuscarBarra = new Button("Buscar");
	private final Button btnBuscarCliente = new Button("Buscar");
	private final Button btnProcesar = new Button("Procesar Devolución");
	private final Button btnInicio = new Button("Inicio");

	private Venta ventaSeleccionada;
	private int diasPolitica = 30;

	@Autowired
	public DevolucionView(DevolucionUseCase devolucionUseCase, ClienteRepository clienteRepository,
			UserRepository userRepository) {
		this.devolucionUseCase = devolucionUseCase;
		this.clienteRepository = clienteRepository;
		this.userRepository = userRepository;

		initUI();
		configurarListeners();
	}

	private void initUI() {
		setSizeFull();
		setPadding(true);
		setSpacing(true);

		H2 titulo = new H2("Devoluciones");
		titulo.getStyle().set("margin", "0 0 20px 0");

		txtCodigoBarra.setWidth("200px");
		txtCodigoBarra.setPlaceholder("Ingrese código de barra");
		btnBuscarBarra.addClassName("btn-nuevo");

		cmbCliente.setItems(clienteRepository.findClientesParaCombo());
		cmbCliente.setItemLabelGenerator(c -> c.getNombre() + " (" + c.getTipo() + ")");
		cmbCliente.setWidth("400px");
		cmbCliente.setClearButtonVisible(true);
		btnBuscarCliente.addClassName("btn-nuevo");

		cmbDiasDevolucion.setItems(15, 20, 30);
		cmbDiasDevolucion.setValue(30);
		cmbDiasDevolucion.setWidth("180px");

		HorizontalLayout searchBar = new HorizontalLayout(txtCodigoBarra, btnBuscarBarra, cmbCliente, cmbDiasDevolucion, btnBuscarCliente, btnInicio);
		searchBar.setAlignItems(Alignment.BASELINE);
		searchBar.setSpacing(true);

		gridVentas.addClassName("grid-documentacion-dark");
		gridVentas.addComponentColumn(v -> {
			Span s = new Span(v.getId() != null ? v.getId().toString() : "");
			if (v.getFechaVenta() != null && excedePolitica(v.getFechaVenta())) {
				s.getStyle().set("color", "red").set("font-weight", "bold");
			}
			return s;
		}).setHeader("N° Venta").setWidth("100px").setSortable(true);
		gridVentas.addComponentColumn(v -> {
			var c = v.getCliente();
			Span s = new Span(c != null ? c.getNombreCliente() : "");
			if (v.getFechaVenta() != null && excedePolitica(v.getFechaVenta())) {
				s.getStyle().set("color", "red").set("font-weight", "bold");
			}
			return s;
		}).setHeader("Cliente").setWidth("250px");
		DateTimeFormatter fmtFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		gridVentas.addComponentColumn(v -> {
			Span s = new Span(v.getFechaVenta() != null ? v.getFechaVenta().format(fmtFecha) : "");
			if (excedePolitica(v.getFechaVenta())) {
				s.getStyle().set("color", "red").set("font-weight", "bold");
			}
			return s;
		}).setHeader("Fecha").setWidth("120px");
		gridVentas.addComponentColumn(v -> {
			Span s = new Span(v.getTotal() != null ? String.format("$%.2f", v.getTotal()) : "$0.00");
			if (v.getFechaVenta() != null && excedePolitica(v.getFechaVenta())) {
				s.getStyle().set("color", "red").set("font-weight", "bold");
			}
			return s;
		}).setHeader("Total").setWidth("120px");
		gridVentas.addComponentColumn(v -> {
			Span s = new Span(v.getEstado() != null ? v.getEstado().getLabel() : "");
			if (v.getFechaVenta() != null && excedePolitica(v.getFechaVenta())) {
				s.getStyle().set("color", "red").set("font-weight", "bold");
			}
			return s;
		}).setHeader("Estado").setWidth("120px");
		gridVentas.addClassName("grid-documentacion-dark");
		gridVentas.setHeight("250px");
		gridVentas.setSelectionMode(Grid.SelectionMode.SINGLE);
		gridVentas.addItemClickListener(e -> {
			if (e.getItem() != null && e.getItem().getFechaVenta() != null
					&& excedePolitica(e.getItem().getFechaVenta())) {
				Notification.show("Esta venta excede la política de devolución de " + diasPolitica + " días",
						3000, Notification.Position.MIDDLE);
			}
		});

		gridItems.addColumn(ItemReturnRow::getArticuloDescripcion).setHeader("Artículo").setWidth("220px");
		gridItems.addColumn(ItemReturnRow::getCodigoBarra).setHeader("Código Barra").setWidth("120px");
		gridItems.addColumn(ItemReturnRow::getCantidadOriginal).setHeader("Cant. Original").setWidth("100px");
		gridItems.addColumn(ItemReturnRow::getPrecioUnitario).setHeader("Precio Unit.").setWidth("100px");
		gridItems.addComponentColumn(ItemReturnRow::getCantidadDevolver).setHeader("Cant. a Devolver").setWidth("120px");
		gridItems.addComponentColumn(ItemReturnRow::getReturnCheckbox).setHeader("Devolver").setWidth("70px");
		gridItems.addComponentColumn(ItemReturnRow::getMalEstadoCheckbox).setHeader("Mal Estado").setWidth("90px");
		gridItems.addClassName("grid-documentacion-dark");
		gridItems.setHeight("250px");
		gridItems.setSelectionMode(Grid.SelectionMode.NONE);

		btnProcesar.addClassName("btn-grabar");
		btnProcesar.setEnabled(false);

		VerticalLayout itemsPanel = new VerticalLayout(new Span("Artículos de la venta seleccionada"),
				gridItems, btnProcesar);
		itemsPanel.setSpacing(true);
		itemsPanel.setPadding(false);

		btnInicio.setIcon(new Icon(VaadinIcon.HOME));
		btnInicio.addClassName("btn-volver-home");
		btnInicio.addClickListener(_ -> UI.getCurrent().navigate(""));

		add(titulo, searchBar, gridVentas, itemsPanel);
	}

	private void configurarListeners() {
		btnBuscarBarra.addClickListener(_ -> buscarPorCodigoBarra());
		btnBuscarCliente.addClickListener(_ -> buscarPorCliente());

		cmbDiasDevolucion.addValueChangeListener(e -> {
			if (e.getValue() != null) {
				diasPolitica = e.getValue();
				gridVentas.getDataProvider().refreshAll();
			}
		});

		txtCodigoBarra.addKeyPressListener(e -> {
			if (e.getKey().equals("Enter")) {
				buscarPorCodigoBarra();
			}
		});

		gridVentas.asSingleSelect().addValueChangeListener(e -> {
			ventaSeleccionada = e.getValue();
			if (ventaSeleccionada != null) {
				cargarItemsVenta(ventaSeleccionada);
			} else {
				gridItems.setItems();
				btnProcesar.setEnabled(false);
			}
		});

		btnProcesar.addClickListener(_ -> procesarDevolucion());
	}

	private boolean excedePolitica(LocalDate fechaVenta) {
		return ChronoUnit.DAYS.between(fechaVenta, LocalDate.now()) > diasPolitica;
	}

	private void buscarPorCodigoBarra() {
		String barra = txtCodigoBarra.getValue();
		if (barra == null || barra.isBlank()) {
			Notification.show("Ingrese un código de barra", 2000, Notification.Position.MIDDLE);
			return;
		}
		List<Venta> ventas = devolucionUseCase.buscarVentasPorCodigoBarra(barra.trim());
		gridVentas.setItems(ventas);
		if (ventas.isEmpty()) {
			Notification.show("No se encontraron ventas con ese código de barra", 3000, Notification.Position.MIDDLE);
		}
	}

	private void buscarPorCliente() {
		ClienteComboDTO cliente = cmbCliente.getValue();
		if (cliente == null) {
			Notification.show("Seleccione un cliente", 2000, Notification.Position.MIDDLE);
			return;
		}
		List<Venta> ventas = devolucionUseCase.buscarVentasPorCliente(cliente.getId());
		gridVentas.setItems(ventas);
		if (ventas.isEmpty()) {
			Notification.show("El cliente no tiene ventas registradas", 3000, Notification.Position.MIDDLE);
		}
	}

	private void cargarItemsVenta(Venta venta) {
		List<ItemReturnRow> rows = new ArrayList<>();
		if (venta.getItems() != null) {
			for (Item item : venta.getItems()) {
				rows.add(new ItemReturnRow(item, this::actualizarBtnProcesar));
			}
		}
		if (rows.isEmpty()) {
			Notification.show("Venta #" + venta.getId() + " [" + venta.getEstado()
					+ "] no tiene ítems cargados (" + (venta.getItems() != null ? venta.getItems().size() : "null") + ")",
					4000, Notification.Position.MIDDLE);
		}
		gridItems.setItems(rows);
	}

	private void actualizarBtnProcesar() {
		boolean algunoSeleccionado = gridItems.getListDataView().getItems()
				.anyMatch(ItemReturnRow::isSelected);
		btnProcesar.setEnabled(algunoSeleccionado);
	}

	private void procesarDevolucion() {
		List<ItemReturnRow> items = new ArrayList<>(gridItems.getListDataView().getItems().toList());
		List<ItemReturnRow> aDevolver = items.stream().filter(ItemReturnRow::isSelected).toList();

		if (aDevolver.isEmpty()) {
			Notification.show("Seleccione al menos un artículo para devolver", 3000, Notification.Position.MIDDLE);
			return;
		}

		String username = SecurityUtils.getUsername();
		Integer idUsuario = userRepository.buscarPorUsername(username)
				.map(u -> u.getId())
				.orElse(null);

		if (idUsuario == null) {
			Notification.show("No se pudo identificar al usuario", 3000, Notification.Position.MIDDLE);
			return;
		}

		StringBuilder errors = new StringBuilder();
		for (ItemReturnRow row : aDevolver) {
			int cantOriginal = row.getItem().getCantidad();
			if (row.getCantidadDevolverValue() > cantOriginal) {
				errors.append(row.getArticuloDescripcion())
					.append(": la cantidad a devolver (").append(row.getCantidadDevolverValue())
					.append(") supera la comprada (").append(cantOriginal).append(")\n");
				continue;
			}
			try {
				devolucionUseCase.procesarDevolucion(
						ventaSeleccionada.getId(),
						row.getItem().getArticulo().getId(),
						row.getCantidadDevolverValue(),
						row.getSubtotalValue(),
						row.isMalEstado(),
						"Devolución manual",
						idUsuario);
			} catch (Exception e) {
				errors.append(row.getArticuloDescripcion()).append(": ").append(e.getMessage()).append("\n");
			}
		}

		if (errors.length() > 0) {
			Notification.show("Errores:\n" + errors, 5000, Notification.Position.MIDDLE);
		} else {
			Notification.show("Devolución procesada correctamente", 3000, Notification.Position.MIDDLE);
			gridVentas.setItems();
			gridItems.setItems();
			btnProcesar.setEnabled(false);
			ventaSeleccionada = null;
		}
	}

	public static class ItemReturnRow {
		private final Item item;
		private final NumberField cantidadDevolver = new NumberField();
		private final Checkbox checkDevolver = new Checkbox();
		private final Checkbox checkMalEstado = new Checkbox();

		public ItemReturnRow(Item item, Runnable onCambio) {
			this.item = item;
			cantidadDevolver.setValue(item.getCantidad().doubleValue());
			cantidadDevolver.setMin(0);
			cantidadDevolver.setMax(item.getCantidad().doubleValue());
			cantidadDevolver.setStep(1);
			cantidadDevolver.setWidth("80px");
			cantidadDevolver.addValueChangeListener(_ -> {
				if (onCambio != null) onCambio.run();
			});
			checkDevolver.addValueChangeListener(e -> {
				if (e.getValue() && (cantidadDevolver.getValue() == null || cantidadDevolver.getValue() <= 0)) {
					cantidadDevolver.setValue(item.getCantidad().doubleValue());
				}
				if (onCambio != null) onCambio.run();
			});
		}

		public Item getItem() { return item; }
		public String getArticuloDescripcion() {
			return item.getArticulo() != null ? item.getArticulo().getDescripcion() : "";
		}
		public String getCodigoBarra() {
			return item.getArticulo() != null ? item.getArticulo().getCodigo_barra() : "";
		}
		public Integer getCantidadOriginal() { return item.getCantidad(); }
		public String getPrecioUnitario() {
			return item.getPrecioUnitario() != null ? String.format("$%.2f", item.getPrecioUnitario()) : "$0.00";
		}
		public BigDecimal getSubtotalValue() {
			return item.getPrecioUnitario() != null
					? item.getPrecioUnitario().multiply(BigDecimal.valueOf(getCantidadDevolverValue()))
					: BigDecimal.ZERO;
		}
		public NumberField getCantidadDevolver() { return cantidadDevolver; }
		public Integer getCantidadDevolverValue() {
			return cantidadDevolver.getValue() != null ? cantidadDevolver.getValue().intValue() : 0;
		}
		public Checkbox getReturnCheckbox() { return checkDevolver; }
		public Checkbox getMalEstadoCheckbox() { return checkMalEstado; }
		public boolean isSelected() { return checkDevolver.getValue() && getCantidadDevolverValue() > 0; }
		public boolean isMalEstado() { return checkMalEstado.getValue(); }
	}
}
