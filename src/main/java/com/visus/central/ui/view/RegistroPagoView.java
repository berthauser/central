package com.visus.central.ui.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.visus.central.domain.model.AplicacionPago;
import com.visus.central.domain.model.Cliente;
import com.visus.central.domain.model.EstadoPlanPago;
import com.visus.central.domain.model.Pago;
import com.visus.central.domain.model.PlanPago;
import com.visus.central.domain.model.TipoPago;
import com.visus.central.domain.port.out.AplicacionPagoRepository;
import com.visus.central.domain.port.out.ClienteRepository;
import com.visus.central.domain.port.out.PagoRepository;
import com.visus.central.domain.port.out.PlanPagoRepository;
import com.visus.central.domain.port.out.TipoPagoRepository;
import com.visus.central.ui.component.CentralLayout;

import jakarta.annotation.security.PermitAll;

@Route(value = "registro-pago", layout = CentralLayout.class)
@PageTitle("Registro de Pago")
@PermitAll
public class RegistroPagoView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final ClienteRepository clienteRepository;
	private final PlanPagoRepository planPagoRepository;
	private final PagoRepository pagoRepository;
	private final TipoPagoRepository tipoPagoRepository;
	private final AplicacionPagoRepository aplicacionPagoRepository;

	private ComboBox<Cliente> clienteCombo;
	private Grid<PlanPago> gridCuotas;
	private Button btnCargarCuotas;
	private BigDecimalField montoField;
	private RadioButtonGroup<TipoPago> formaPagoGroup;
	private Button btnPagar;
	private Span totalSeleccionado;
	private List<PlanPago> cuotasSeleccionadas = new ArrayList<>();

	@Autowired
	public RegistroPagoView(ClienteRepository clienteRepository, PlanPagoRepository planPagoRepository,
			PagoRepository pagoRepository, AplicacionPagoRepository aplicacionPagoRepository,
			TipoPagoRepository tipoPagoRepository) {
		this.clienteRepository = clienteRepository;
		this.planPagoRepository = planPagoRepository;
		this.pagoRepository = pagoRepository;
		this.tipoPagoRepository = tipoPagoRepository;
		this.aplicacionPagoRepository = aplicacionPagoRepository;

		initUI();
		cargarClientes();
	}

	private void initUI() {
		setSizeFull();
		setPadding(true);
		setSpacing(true);

		H2 titulo = new H2("Registro Manual de Pagos");
		titulo.getStyle().set("margin", "0 0 20px 0");

		// Cliente
		clienteCombo = new ComboBox<>("Cliente");
		clienteCombo.setItemLabelGenerator(Cliente::getNombreCliente);
		clienteCombo.setWidth("400px");
		clienteCombo.setClearButtonVisible(true);

		// Botón cargar cuotas del cliente seleccionado
		btnCargarCuotas = new Button("Cargar Cuotas Pendientes", _ -> cargarCuotasCliente());
		btnCargarCuotas.addClassName("btn-nuevo");

		HorizontalLayout clienteLayout = new HorizontalLayout(clienteCombo, btnCargarCuotas);
		clienteLayout.setAlignItems(Alignment.BASELINE);
		clienteLayout.setSpacing(true);

		// Grid de cuotas pendientes (selección múltiple)
		gridCuotas = new Grid<>(PlanPago.class, false);
		gridCuotas.setSelectionMode(SelectionMode.MULTI);
		configurarGridCuotas();
		gridCuotas.setHeight("300px");
		gridCuotas.addClassName("grid-documentacion-dark");

		// Listener para actualizar el total seleccionado
		gridCuotas.addSelectionListener(event -> {
			cuotasSeleccionadas = new ArrayList<>(event.getAllSelectedItems());
			actualizarTotalSeleccionado();
		});

		// Monto a pagar (se preselecciona igual al total seleccionado, pero se puede
		// modificar)
		montoField = new BigDecimalField("Monto a pagar");
		montoField.setWidth("200px");
		montoField.setPrefixComponent(new Span("$"));
		montoField.addValueChangeListener(_ -> validarMonto());

		// Forma de pago
		formaPagoGroup = new RadioButtonGroup<>("Forma de pago");
		// Cargar los tipos de pago desde la base de datos (inyectar TipoPagoRepository)
		formaPagoGroup.setItems(tipoPagoRepository.findAll());
		formaPagoGroup.setItemLabelGenerator(TipoPago::getDescripcion);
		formaPagoGroup.setRequired(true);

		// Total seleccionado
		totalSeleccionado = new Span("Total seleccionado: $0.00");
		totalSeleccionado.getStyle().set("font-weight", "bold");

		// Botón pagar
		btnPagar = new Button("Registrar Pago", _ -> registrarPago());
		btnPagar.addClassName("btn-nuevo");
		btnPagar.setEnabled(false);

		// Layout
		HorizontalLayout pagoLayout = new HorizontalLayout(montoField, formaPagoGroup);
		pagoLayout.setAlignItems(Alignment.BASELINE);
		pagoLayout.setSpacing(true);

		add(titulo, clienteLayout, gridCuotas, totalSeleccionado, pagoLayout, btnPagar);

	}

	private void configurarGridCuotas() {
		gridCuotas.addColumn(PlanPago::getIdPlanPago).setHeader("N° Cuota").setWidth("80px");
		gridCuotas.addColumn(PlanPago::getIdVenta).setHeader("N° Venta").setWidth("100px");
		gridCuotas.addColumn(p -> p.getFechaVencimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
				.setHeader("Vencimiento").setWidth("120px");
		gridCuotas.addColumn(p -> String.format("$%.2f", p.getMontoOriginal())).setHeader("Monto Original")
				.setWidth("120px");
		gridCuotas.addColumn(p -> String.format("$%.2f", p.getMontoPagado())).setHeader("Pagado").setWidth("100px");
		gridCuotas.addColumn(p -> String.format("$%.2f", p.getMontoOriginal().subtract(p.getMontoPagado())))
				.setHeader("Saldo").setWidth("120px");
		gridCuotas.addColumn(p -> p.getEstado().getLabel()).setHeader("Estado").setWidth("100px");
	}

	private void cargarClientes() {
		clienteCombo.setItems(clienteRepository.findAll());
	}

	private void cargarCuotasCliente() {
		Cliente cliente = clienteCombo.getValue();
		if (cliente == null) {
			Notification.show("Seleccione un cliente", 3000, Notification.Position.MIDDLE);
			return;
		}
		List<PlanPago> pendientes = planPagoRepository.findPendientesByClienteId(cliente.getId());
		if (pendientes.isEmpty()) {
			Notification.show("El cliente no tiene deudas pendientes", 3000, Notification.Position.MIDDLE);
			gridCuotas.setItems();
		} else {
			gridCuotas.setItems(pendientes);
			Notification.show("Se cargaron " + pendientes.size() + " cuotas pendientes", 3000,
					Notification.Position.MIDDLE);
		}
		cuotasSeleccionadas.clear();
		actualizarTotalSeleccionado();
		montoField.clear();
		btnPagar.setEnabled(false);
	}

	private void actualizarTotalSeleccionado() {
		BigDecimal total = cuotasSeleccionadas.stream().map(p -> p.getMontoOriginal().subtract(p.getMontoPagado()))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		totalSeleccionado.setText("Total seleccionado: $" + String.format("%.2f", total));
		if (total.compareTo(BigDecimal.ZERO) > 0) {
			montoField.setValue(total);
			montoField.setEnabled(true);
		} else {
			montoField.clear();
			montoField.setEnabled(false);
			btnPagar.setEnabled(false);
		}
	}

	private void validarMonto() {
		BigDecimal monto = montoField.getValue();
		if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
			btnPagar.setEnabled(false);
			return;
		}
		BigDecimal totalDeuda = cuotasSeleccionadas.stream().map(p -> p.getMontoOriginal().subtract(p.getMontoPagado()))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		if (monto.compareTo(totalDeuda) > 0) {
			Notification.show("El monto ingresado supera la deuda seleccionada", 3000, Notification.Position.MIDDLE);
			btnPagar.setEnabled(false);
		} else {
			btnPagar.setEnabled(true);
		}
	}

	private void registrarPago() {
		if (cuotasSeleccionadas.isEmpty()) {
			Notification.show("No hay cuotas seleccionadas", 3000, Notification.Position.MIDDLE);
			return;
		}

		TipoPago tipoPago = formaPagoGroup.getValue();
		if (tipoPago == null) {
		    Notification.show("Seleccione una forma de pago", 3000, Notification.Position.MIDDLE);
		    return;
		}
		
		

		BigDecimal montoPagado = montoField.getValue();
		if (montoPagado == null || montoPagado.compareTo(BigDecimal.ZERO) <= 0) {
			Notification.show("Monto inválido", 3000, Notification.Position.MIDDLE);
			return;
		}

		Cliente cliente = clienteCombo.getValue();
		if (cliente == null)
			return;

		try {
			// 1. Crear el pago principal (cabeza)
			Pago pago = new Pago();
			pago.setIdCliente(cliente.getId());
			pago.setFecha(LocalDate.now());
			pago.setMontoTotal(montoPagado);
			pago.setIdTipoPago(tipoPago.getId()); // ✅ Usamos el ID real desde la base de datos
			pago.setObservaciones("Pago manual registrado en vista de pagos");
			pago.setAplicado(false);
			pago = pagoRepository.save(pago);

			// 2. Aplicar el pago a las cuotas seleccionadas (prorrateo simple)
			BigDecimal montoRestante = montoPagado;
			List<AplicacionPago> aplicaciones = new ArrayList<>();

			for (PlanPago plan : cuotasSeleccionadas) {
				if (montoRestante.compareTo(BigDecimal.ZERO) <= 0)
					break;
				BigDecimal saldoPlan = plan.getMontoOriginal().subtract(plan.getMontoPagado());
				BigDecimal aplicar = montoRestante.min(saldoPlan);

				AplicacionPago ap = new AplicacionPago();
				ap.setIdPago(pago.getIdPago());
				ap.setIdPlanPago(plan.getIdPlanPago());
				ap.setMontoAplicado(aplicar);
				ap.setMontoNetoPagado(aplicar);
				ap.setPctProntoPago(BigDecimal.ZERO);
				ap.setMontoDescuentoAplicado(BigDecimal.ZERO);
				aplicaciones.add(ap);

				// Actualizar cuota
				plan.setMontoPagado(plan.getMontoPagado().add(aplicar));
				if (plan.getMontoPagado().compareTo(plan.getMontoOriginal()) >= 0) {
					plan.setEstado(EstadoPlanPago.PAGADA);
					plan.setFechaPago(LocalDate.now());
				}
				planPagoRepository.save(plan);

				montoRestante = montoRestante.subtract(aplicar);
			}

			// Guardar todas las aplicaciones
			for (AplicacionPago ap : aplicaciones) {
				aplicacionPagoRepository.save(ap);
			}

			// Si después de aplicar sobró dinero (pago mayor a la deuda seleccionada), se
			// puede crear una nota de crédito o quedar como saldo a favor.
			if (montoRestante.compareTo(BigDecimal.ZERO) > 0) {
				Notification.show("Atención: Sobrante de $" + String.format("%.2f", montoRestante)
						+ " no aplicado. Se generará un saldo a favor.", 5000, Notification.Position.MIDDLE);
				// Aquí podrías crear un registro de "crédito" o ajuste.
			}

			// Marcar el pago como aplicado
			pago.setAplicado(true);
			pagoRepository.save(pago);

			Notification.show(
					"Pago registrado exitosamente. Monto aplicado: $"
							+ String.format("%.2f", montoPagado.subtract(montoRestante)),
					4000, Notification.Position.MIDDLE);

			// Recargar la grilla para actualizar saldos
			cargarCuotasCliente();
			formaPagoGroup.clear();
			montoField.clear();
			btnPagar.setEnabled(false);

		} catch (Exception ex) {
			Notification.show("Error al registrar pago: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
			ex.printStackTrace();
		}

	}

}
