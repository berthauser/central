package com.visus.central.ui.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.visus.central.domain.model.ComisionVenta;
import com.visus.central.domain.model.Vendedor;
import com.visus.central.domain.port.in.ComisionUseCase;
import com.visus.central.domain.port.out.VendedorRepository;
import com.visus.central.ui.component.CentralLayout;

import jakarta.annotation.security.PermitAll;

@Route(value = "comisiones", layout = CentralLayout.class)
@PageTitle("Comisiones por Ventas")
@PermitAll
public class ComisionView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final ComisionUseCase comisionUseCase;
	private final VendedorRepository vendedorRepository;

	private Grid<ComisionVenta> grid;
	private ComboBox<Vendedor> vendedorFiltro;
	private DatePicker fechaDesde;
	private DatePicker fechaHasta;
	private Button btnFiltrar;
	private Button btnCalcularPendientes;
	private Button btnCalcularMes;
	private Button btnReglas;
	private Button btnInicio;
	private Span totalComisiones;

	@Autowired
	public ComisionView(ComisionUseCase comisionUseCase, VendedorRepository vendedorRepository) {
		this.comisionUseCase = comisionUseCase;
		this.vendedorRepository = vendedorRepository;

		initUI();
		cargarCombos();
		recargarDatos();
	}

	private void initUI() {
		setSizeFull();
		setPadding(true);
		setSpacing(true);

		H2 titulo = new H2("Comisiones por Ventas");
		titulo.getStyle().set("margin", "0 0 20px 0");

		vendedorFiltro = new ComboBox<>("Vendedor");
		vendedorFiltro.setItemLabelGenerator(Vendedor::getNombre);
		vendedorFiltro.setWidth("300px");
		vendedorFiltro.setClearButtonVisible(true);

		fechaDesde = new DatePicker("Desde");
		fechaDesde.setValue(LocalDate.now().withDayOfMonth(1));

		fechaHasta = new DatePicker("Hasta");
		fechaHasta.setValue(LocalDate.now());

		btnFiltrar = new Button("Filtrar", _ -> recargarDatos());
		btnFiltrar.addClassName("btn-nuevo");

		btnCalcularPendientes = new Button("Calcular Pendientes", _ -> calcularPendientes());
		btnCalcularPendientes.addClassName("btn-crear");

		btnCalcularMes = new Button("Calcular Mes", _ -> {
			try {
				var generadas = comisionUseCase.calcularComisionesPeriodo(
						LocalDate.now().getMonthValue(), LocalDate.now().getYear());
				Notification.show(generadas.size() + " comisiones del mes calculadas", 3000,
						Notification.Position.MIDDLE);
				recargarDatos();
			} catch (Exception e) {
				Notification.show("Error: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
			}
		});
		btnCalcularMes.addClassName("btn-grabar");

		btnReglas = new Button("Reglas");
		btnReglas.setIcon(new Icon(VaadinIcon.COG));
		btnReglas.addClassName("btn-modificar");
		btnReglas.addClickListener(_ -> UI.getCurrent().navigate("reglas-comision"));

		btnInicio = new Button("Inicio");
		btnInicio.setIcon(new Icon(VaadinIcon.HOME));
		btnInicio.addClassName("btn-volver-home");
		btnInicio.addClickListener(_ -> UI.getCurrent().navigate(""));

		totalComisiones = new Span("Total: $0.00");
		totalComisiones.getStyle().set("font-weight", "bold").set("font-size", "16px");

		grid = new Grid<>(ComisionVenta.class, false);
		grid.addClassName("grid-documentacion-dark");
		grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		grid.setHeight("500px");
		configurarGrid();

		HorizontalLayout filtrosLayout = new HorizontalLayout(vendedorFiltro, fechaDesde, fechaHasta, btnFiltrar,
				btnCalcularPendientes, btnCalcularMes, btnReglas, btnInicio);
		filtrosLayout.setAlignItems(Alignment.BASELINE);
		filtrosLayout.setSpacing(true);

		add(titulo, filtrosLayout, totalComisiones, grid);
	}

	private void configurarGrid() {
		grid.addColumn(c -> c.getVendedor().getNombre()).setHeader("Vendedor").setWidth("200px")
				.setSortable(true).setResizable(true);
		grid.addColumn(c -> c.getVenta().getId()).setHeader("N° Venta").setWidth("100px")
				.setSortable(true).setResizable(true);
		grid.addColumn(c -> c.getVenta().getCliente().getNombreCliente()).setHeader("Cliente")
				.setWidth("250px").setResizable(true);
		grid.addColumn(c -> c.getFechaCalculo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
				.setHeader("Fecha Cálculo").setWidth("120px").setResizable(true);
		grid.addColumn(c -> String.format("$%.2f", c.getBaseComisionable()))
				.setHeader("Base Comisionable").setWidth("130px").setResizable(true);
		grid.addColumn(c -> String.format("%.2f%%", c.getPorcentaje()))
				.setHeader("%").setWidth("70px").setResizable(true);
		grid.addColumn(c -> String.format("$%.2f", c.getComisionBruta()))
				.setHeader("Comisión Bruta").setWidth("120px").setResizable(true);
		grid.addColumn(c -> String.format("$%.2f", c.getAjustes()))
				.setHeader("Ajustes").setWidth("90px").setResizable(true);
		grid.addColumn(c -> String.format("$%.2f", c.getComisionFinal()))
				.setHeader("Comisión Final").setWidth("120px").setResizable(true);
		grid.addColumn(c -> c.getEstado().getLabel()).setHeader("Estado").setWidth("100px").setResizable(true);
	}

	private void cargarCombos() {
		vendedorFiltro.setItems(vendedorRepository.findAll());
	}

	private void recargarDatos() {
		Vendedor vendedor = vendedorFiltro.getValue();
		LocalDate desde = fechaDesde.getValue();
		LocalDate hasta = fechaHasta.getValue();

		List<ComisionVenta> comisiones;

		if (vendedor != null) {
			comisiones = comisionUseCase.findByVendedorId(vendedor.getId());
			comisiones = comisiones.stream()
					.filter(c -> !c.getFechaCalculo().isBefore(desde) && !c.getFechaCalculo().isAfter(hasta))
					.toList();
		} else {
			comisiones = comisionUseCase.findByFechaBetween(desde, hasta);
		}

		grid.setItems(comisiones);
		actualizarTotal(comisiones);
	}

	private void actualizarTotal(List<ComisionVenta> comisiones) {
		double total = comisiones.stream()
				.mapToDouble(c -> c.getComisionFinal().doubleValue())
				.sum();
		totalComisiones.setText(String.format("Total: $%.2f", total));
	}

	private void calcularPendientes() {
		try {
			List<ComisionVenta> generadas = comisionUseCase.calcularComisionesPendientes();
			Notification.show(generadas.size() + " comisiones calculadas", 3000, Notification.Position.MIDDLE);
			recargarDatos();
		} catch (Exception e) {
			Notification.show("Error al calcular comisiones: " + e.getMessage(), 5000,
					Notification.Position.MIDDLE);
		}
	}
}
