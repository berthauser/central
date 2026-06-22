package com.visus.central.ui.view;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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
import com.visus.central.domain.model.Cliente;
import com.visus.central.domain.model.NotaCredito;
import com.visus.central.domain.model.PlanPago;
import com.visus.central.domain.port.out.ClienteRepository;
import com.visus.central.domain.port.out.NotaCreditoRepository;
import com.visus.central.domain.port.out.PlanPagoRepository;
import com.visus.central.infraestructure.util.PagoBroadcaster;
import com.visus.central.ui.component.CentralLayout;

import jakarta.annotation.security.PermitAll;

@Route(value = "cuenta-corriente", layout = CentralLayout.class)
@PageTitle("Cuenta Corriente")
@PermitAll
public class CuentaCorrienteView extends VerticalLayout implements Runnable {

	private static final long serialVersionUID = 1L;

	private final ClienteRepository clienteRepository;
	private final PlanPagoRepository planPagoRepository;
	private final NotaCreditoRepository notaCreditoRepository;

	private ComboBox<Cliente> clienteCombo;
	private Grid<PlanPago> grid;
	private Span saldoValor;
	private Span saldoFavorValor;
	private Span deudaNetaValor;
	private Button btnBuscar;
	private Button btnInicio;
	private Button btnDetalleDeuda;

	@Autowired
	public CuentaCorrienteView(ClienteRepository clienteRepository, PlanPagoRepository planPagoRepository,
			NotaCreditoRepository notaCreditoRepository) {
		this.clienteRepository = clienteRepository;
		this.planPagoRepository = planPagoRepository;
		this.notaCreditoRepository = notaCreditoRepository;

		initUI();
		cargarCombos();

	}

	private void initUI() {
		setSizeFull();
		setPadding(true);
		setSpacing(true);

		// Título
		H2 titulo = new H2("Cuenta Corriente de Clientes");
		titulo.getStyle().set("margin", "0 0 20px 0");

		// Selector de cliente
		clienteCombo = new ComboBox<>("Cliente");
		clienteCombo.setItemLabelGenerator(Cliente::getNombreCliente);
		clienteCombo.setWidth("400px");
		clienteCombo.setClearButtonVisible(true);

		// Botón buscar
		btnBuscar = new Button("Buscar", _ -> buscarCuentas());
		btnBuscar.addClassName("btn-nuevo");

		// Botón Inicio con ícono Home
		btnInicio = new Button("Inicio");
		btnInicio.setIcon(new Icon(VaadinIcon.HOME));
		btnInicio.addClickListener(_ -> {
			clienteCombo.clear();
			grid.setItems();
			grid.deselectAll();
			saldoValor.setText("$0.00");
			btnDetalleDeuda.setEnabled(false);
			UI.getCurrent().navigate("");
		});

		btnInicio.addClassName("btn-volver-home");
		btnInicio.setTooltipText("Volver al menú principal");

		// Saldo actual
		saldoValor = new Span("$0.00");
		saldoValor.getStyle().set("font-weight", "bold").set("font-size", "18px");

		saldoFavorValor = new Span("$0.00");
		saldoFavorValor.getStyle().set("font-weight", "bold").set("font-size", "18px").set("color", "var(--lumo-success-text-color)");

		deudaNetaValor = new Span("$0.00");
		deudaNetaValor.getStyle().set("font-weight", "bold").set("font-size", "18px");

		btnDetalleDeuda = new Button("Ver Detalle de Deuda");
		btnDetalleDeuda.addClassName("btn-crear");
		btnDetalleDeuda.setEnabled(false);
		btnDetalleDeuda.addClickListener(_ -> {
			var selected = grid.getSelectedItems();
			if (!selected.isEmpty()) {
				UI.getCurrent().navigate("plan-pago-detalle/" + selected.iterator().next().getIdVenta());
			}
		});

		HorizontalLayout saldoLayout = new HorizontalLayout(new Span("Deuda total:"), saldoValor);
		saldoLayout.setAlignItems(Alignment.BASELINE);
		HorizontalLayout creditoLayout = new HorizontalLayout(new Span("Saldo a favor:"), saldoFavorValor);
		creditoLayout.setAlignItems(Alignment.BASELINE);
		HorizontalLayout netoLayout = new HorizontalLayout(new Span("Deuda neta:"), deudaNetaValor, btnDetalleDeuda);
		netoLayout.setAlignItems(Alignment.BASELINE);

		// Grid de cuentas
		grid = new Grid<>(PlanPago.class, false);
		grid.addClassName("grid-documentacion-dark");
		grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		grid.setSelectionMode(Grid.SelectionMode.SINGLE);
		grid.addSelectionListener(e -> btnDetalleDeuda.setEnabled(!e.getFirstSelectedItem().isEmpty()));
		configurarGrid();
		grid.setHeight("400px");

		// Layout de búsqueda
		HorizontalLayout searchLayout = new HorizontalLayout(clienteCombo, btnBuscar, btnInicio);
		searchLayout.setAlignItems(Alignment.BASELINE);
		searchLayout.setSpacing(true);

		add(titulo, searchLayout, saldoLayout, creditoLayout, netoLayout, grid);
	}
	
	@Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        PagoBroadcaster.register(attachEvent.getUI());
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        PagoBroadcaster.unregister(detachEvent.getUI());
        super.onDetach(detachEvent);
    }

    @Override
    public void run() {
        getUI().ifPresent(ui -> ui.access(() -> {
            if (clienteCombo.getValue() != null) {
                buscarCuentas();
            }
        }));
    }

	private void configurarGrid() {
		grid.addColumn(PlanPago::getNroCuota).setHeader("N° Cuota").setWidth("100px");
		grid.addColumn(PlanPago::getIdVenta).setHeader("N° Venta").setWidth("100px");
		grid.addColumn(p -> p.getFechaVencimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
				.setHeader("Fecha Vencimiento").setWidth("120px");
		grid.addColumn(p -> String.format("$%.2f", p.getMontoOriginal())).setHeader("Monto Original").setWidth("120px");
		grid.addColumn(p -> String.format("$%.2f", p.getMontoOriginal().subtract(p.getMontoPagado())))
				.setHeader("Saldo Pendiente").setWidth("120px");
		grid.addColumn(p -> p.getEstado().getLabel()).setHeader("Estado").setWidth("100px");
	}

	private void cargarCombos() {
		clienteCombo.setItems(clienteRepository.findAll());
	}

	private void buscarCuentas() {
		Cliente cliente = clienteCombo.getValue();
		if (cliente == null) {
			Notification.show("Seleccione un cliente", 3000, Notification.Position.MIDDLE);
			return;
		}

		grid.deselectAll();

		// Obtener todas las cuotas pendientes (PENDIENTE o PARCIAL) del cliente
		List<PlanPago> cuotas = planPagoRepository.findPendientesByClienteId(cliente.getId());
		grid.setItems(cuotas);

		// Calcular saldo total pendiente
		BigDecimal saldo = cuotas.stream().map(p -> p.getMontoOriginal().subtract(p.getMontoPagado()))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		saldoValor.setText(String.format("$%.2f", saldo));

		// Obtener saldo a favor (notas de crédito no consumidas)
		List<NotaCredito> creditos = notaCreditoRepository.findNoConsumidosByClienteId(cliente.getId());
		BigDecimal saldoFavor = creditos.stream().map(NotaCredito::getMonto)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		saldoFavorValor.setText(String.format("$%.2f", saldoFavor));

		// Calcular deuda neta (deuda - crédito, mínimo 0)
		BigDecimal deudaNeta = saldo.subtract(saldoFavor);
		if (deudaNeta.compareTo(BigDecimal.ZERO) < 0) {
			deudaNeta = BigDecimal.ZERO;
		}
		deudaNetaValor.setText(String.format("$%.2f", deudaNeta));

		boolean tieneDeuda = saldo.compareTo(BigDecimal.ZERO) > 0;
		btnDetalleDeuda.setEnabled(deudaNeta.compareTo(BigDecimal.ZERO) > 0);
		if (tieneDeuda) {
			saldoValor.getStyle().set("color", "var(--lumo-error-text-color)");
		} else {
			saldoValor.getStyle().set("color", "var(--lumo-success-text-color)");
		}
	}

}
