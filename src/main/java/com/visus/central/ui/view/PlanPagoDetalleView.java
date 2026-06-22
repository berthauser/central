package com.visus.central.ui.view;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.visus.central.domain.model.AplicacionPago;
import com.visus.central.domain.model.Cliente;
import com.visus.central.domain.model.NotaCredito;
import com.visus.central.domain.model.Domicilio;
import com.visus.central.domain.model.EstadoPlanPago;
import com.visus.central.domain.model.EstadoVenta;
import com.visus.central.domain.model.Pago;
import com.visus.central.domain.model.PlanPago;
import com.visus.central.domain.model.TipoPago;
import com.visus.central.domain.model.Venta;
import com.visus.central.domain.port.out.AplicacionPagoRepository;
import com.visus.central.domain.port.out.NotaCreditoRepository;
import com.visus.central.domain.port.out.PagoRepository;
import com.visus.central.domain.port.out.PlanPagoRepository;
import com.visus.central.domain.port.out.VentaRepository;
import com.visus.central.domain.report.AplicacionReporteBean;
import com.visus.central.domain.report.VencimientoReporteBean;
import com.visus.central.infraestructure.util.DateTimeUtils;
import com.visus.central.infraestructure.util.PagoBroadcaster;
import com.visus.central.ui.component.CentralLayout;
import com.visus.central.domain.port.out.ReportGenerator;
import com.visus.central.domain.port.out.TipoPagoRepository;

import jakarta.annotation.security.PermitAll;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import pl.allegro.finance.tradukisto.MoneyConverters;

@Route(value = "plan-pago-detalle/:idVenta?", layout = CentralLayout.class)
@PageTitle("Detalle del Plan de Pago")
@PermitAll
public class PlanPagoDetalleView extends VerticalLayout implements BeforeEnterObserver, Runnable {

	private static final long serialVersionUID = 1L;

	private final VentaRepository ventaRepository;
	private final PlanPagoRepository planPagoRepository;
	private final TipoPagoRepository tipoPagoRepository;
	private final AplicacionPagoRepository aplicacionPagoRepository;
	private final PagoRepository pagoRepository;
	private final ReportGenerator reportGenerator;
	private final NotaCreditoRepository notaCreditoRepository;

	private Integer idVenta;
	private Venta venta;
	private List<PlanPago> planesPago;
	private Cliente cliente;

	private Span infoVenta;
	private Grid<PlanPago> gridCuotas;
	private Grid<AplicacionPago> gridAplicaciones;


	@Autowired
	public PlanPagoDetalleView(VentaRepository ventaRepository, PlanPagoRepository planPagoRepository,
			AplicacionPagoRepository aplicacionPagoRepository, PagoRepository pagoRepository,
			TipoPagoRepository tipoPagoRepository, ReportGenerator reportGenerator,
			NotaCreditoRepository notaCreditoRepository) {
		this.ventaRepository = ventaRepository;
		this.planPagoRepository = planPagoRepository;
		this.tipoPagoRepository = tipoPagoRepository;
		this.aplicacionPagoRepository = aplicacionPagoRepository;
		this.pagoRepository = pagoRepository;
		this.reportGenerator = reportGenerator;
		this.notaCreditoRepository = notaCreditoRepository;
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
		getUI().ifPresent(ui -> ui.access(this::recargarDatos));
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		Optional<String> param = event.getRouteParameters().get("idVenta");
		if (param.isPresent()) {
			try {
				idVenta = Integer.valueOf(param.get());
				cargarDatos();
				initUI();
			} catch (NumberFormatException e) {
				Notification.show("ID de venta inválido", 3000, Notification.Position.MIDDLE);
				event.forwardTo(CuentaCorrienteView.class);
			}
		} else {
			Notification.show("No se especificó una venta", 3000, Notification.Position.MIDDLE);
			event.forwardTo(CuentaCorrienteView.class);
		}
	}

	private void cargarDatos() {
		venta = ventaRepository.findById(idVenta).orElseThrow(() -> new RuntimeException("Venta no encontrada"));
		planesPago = planPagoRepository.findByVentaId(idVenta);
		cliente = venta.getCliente();
	}

	private void initUI() {
		removeAll();
		setSizeFull();
		setPadding(true);
		setSpacing(true);

		H2 titulo = new H2("Plan de Pagos - Venta N° " + venta.getNumeroComprobante());
		titulo.getStyle().set("margin", "0");

		Button btnVolver = new Button("Volver");
		btnVolver.setIcon(new Icon(VaadinIcon.ARROW_LEFT));
		btnVolver.addClickListener(_ -> UI.getCurrent().navigate("cuenta-corriente"));
		btnVolver.addClassName("btn-volver");

		Button btnInicio = new Button("Inicio");
		btnInicio.setIcon(new Icon(VaadinIcon.HOME));
		btnInicio.addClickListener(_ -> UI.getCurrent().navigate(""));
		btnInicio.addClassName("btn-volver-home");

		Button btnPagarTodo = new Button("Pagar Todo");
		btnPagarTodo.setIcon(new Icon(VaadinIcon.MONEY));
		btnPagarTodo.addClassName("btn-nuevo");
		btnPagarTodo.setEnabled(planesPago.stream().anyMatch(p -> p.getEstado() != EstadoPlanPago.PAGADA));
		btnPagarTodo.addClickListener(_ -> abrirDialogoPagarTodo());

		Button btnImprimirResumen = new Button("Imprimir Resumen");
		btnImprimirResumen.setIcon(new Icon(VaadinIcon.PRINT));
		btnImprimirResumen.addClassName("btn-nuevo");
		btnImprimirResumen.addClickListener(_ -> imprimirResumen());

		HorizontalLayout buttonsLayout = new HorizontalLayout(btnVolver, btnInicio, btnPagarTodo, btnImprimirResumen);
		buttonsLayout.setSpacing(true);
		buttonsLayout.setAlignItems(Alignment.CENTER);

		HorizontalLayout headerLayout = new HorizontalLayout(titulo, buttonsLayout);
		headerLayout.setAlignItems(Alignment.CENTER);
		headerLayout.setSpacing(true);
		headerLayout.setWidthFull();
		headerLayout.setJustifyContentMode(JustifyContentMode.START);

		infoVenta = new Span();
		actualizarInfoVenta();

		gridCuotas = new Grid<>(PlanPago.class, false);
		gridCuotas.addClassName("grid-documentacion-dark");
		gridCuotas.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		configurarGridCuotas();
		gridCuotas.setHeight("250px");

		gridAplicaciones = new Grid<>(AplicacionPago.class, false);
		gridAplicaciones.addClassName("grid-documentacion-dark");
		gridAplicaciones.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		configurarGridAplicaciones();
		gridAplicaciones.setHeight("200px");

		add(headerLayout, infoVenta, new H2("Cuotas"), gridCuotas, new H2("Pagos Aplicados"), gridAplicaciones);

		cargarCuotas();
		cargarAplicaciones();
	}

	private void actualizarInfoVenta() {
		BigDecimal total = planesPago.stream().map(PlanPago::getMontoOriginal).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal pagado = planesPago.stream().map(PlanPago::getMontoPagado).reduce(BigDecimal.ZERO, BigDecimal::add);
		infoVenta.setText(String.format("Cliente: %s | Fecha Venta: %s | Total: $%.2f | Pagado: $%.2f | Saldo: $%.2f",
				cliente.getNombreCliente(), venta.getFechaVenta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
				total, pagado, total.subtract(pagado)));
		infoVenta.getStyle().set("font-size", "14px").set("padding", "10px");
	}

	private void configurarGridCuotas() {
		gridCuotas.addColumn(PlanPago::getNroCuota).setHeader("N° Cuota").setWidth("100px");
		gridCuotas.addColumn(p -> {
			if (p.getFechaVencimiento() != null) {
				return p.getFechaVencimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			} else {
				return "Contado"; // o "---" o "Sin vencimiento"
			}
		}).setHeader("Fecha Vencimiento").setWidth("120px");
		gridCuotas.addColumn(p -> String.format("$%.2f", p.getMontoOriginal())).setHeader("Monto").setWidth("120px");
		gridCuotas.addColumn(p -> String.format("$%.2f", p.getMontoPagado())).setHeader("Pagado").setWidth("120px");
		gridCuotas.addColumn(p -> String.format("$%.2f", p.getMontoOriginal().subtract(p.getMontoPagado())))
				.setHeader("Saldo").setWidth("120px");
		gridCuotas.addColumn(p -> p.getEstado().getLabel()).setHeader("Estado").setWidth("100px");

		gridCuotas.addComponentColumn(plan -> {
			Button btnPagar = new Button("Pagar");
			btnPagar.addClassName("btn-nuevo");
			btnPagar.setEnabled(plan.getEstado() != EstadoPlanPago.PAGADA);
			btnPagar.addClickListener(_ -> abrirDialogoPago(plan, btnPagar));
			return btnPagar;
		}).setHeader("Acción").setWidth("100px");
	}

	private void configurarGridAplicaciones() {
		gridAplicaciones.addColumn(a -> {
			if (a.getIdPlanPago() != null) {
				return planPagoRepository.findById(a.getIdPlanPago()).map(PlanPago::getNroCuota).orElse(null);
			}
			return "TOTAL";
		}).setHeader("N° Cuota").setWidth("100px");

		// Como AplicacionPago no tiene fecha, mostramos la fecha del pago asociado
		gridAplicaciones.addColumn(a -> {
			return pagoRepository.findById(a.getIdPago()).map(Pago::getFecha)
					.map(f -> f.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).orElse("N/A");
		}).setHeader("Fecha Aplicación").setWidth("150px");

		gridAplicaciones.addColumn(a -> String.format("$%.2f", a.getMontoNetoPagado())).setHeader("Monto Neto")
				.setWidth("120px");

	}

	private void cargarCuotas() {
		planesPago.sort(Comparator.comparing(PlanPago::getNroCuota));
		gridCuotas.setItems(planesPago);
	}

	private void cargarAplicaciones() {
		List<AplicacionPago> aplicaciones = aplicacionPagoRepository.findByVentaId(idVenta);
		gridAplicaciones.setItems(aplicaciones);
	}

	private void abrirDialogoPago(PlanPago plan, Button btnPagar) {
		BigDecimal saldo = plan.getMontoOriginal().subtract(plan.getMontoPagado());

		Dialog dialog = new Dialog();
		dialog.setHeaderTitle("Pagar Cuota N° " + plan.getNroCuota());
		dialog.setWidth("450px");

		NumberField montoField = new NumberField("Monto a Pagar");
		montoField.setValue(saldo.doubleValue());
		montoField.setWidthFull();

		RadioButtonGroup<TipoPago> formaPagoGroup = new RadioButtonGroup<>("Forma de Pago");
		formaPagoGroup.setItems(tipoPagoRepository.findAll());
		formaPagoGroup.setItemLabelGenerator(TipoPago::getDescripcion);
		formaPagoGroup.setRequired(true);
		formaPagoGroup.setWidthFull();

		Checkbox imprimirReciboCheck = new Checkbox("Imprimir Recibo de Pago");
		imprimirReciboCheck.setValue(true);

		Button btnAceptar = new Button("Aceptar");
		btnAceptar.addClassName("btn-nuevo");
		btnAceptar.setEnabled(false);
		formaPagoGroup.addValueChangeListener(e -> btnAceptar.setEnabled(e.getValue() != null));

		btnAceptar.addClickListener(_ -> {
			TipoPago tipoPago = formaPagoGroup.getValue();
			if (tipoPago == null)
				return;

			BigDecimal montoPagar = BigDecimal.valueOf(montoField.getValue());
			if (montoPagar == null || montoPagar.compareTo(BigDecimal.ZERO) <= 0) {
				Notification.show("Ingrese un monto válido mayor a cero", 3000, Notification.Position.MIDDLE);
				return;
			}

			try {
				BigDecimal creditoGenerado = procesarPago(plan, tipoPago, montoPagar);
				dialog.close();
				if (imprimirReciboCheck.getValue()) {
					imprimirRecibo(venta.getNumeroComprobante().toString(), cliente.getNumero().toString(), cliente.getNombreCliente(),
							cliente.getSituacionFiscal().toString(), montoPagar.doubleValue(),
							"cuota N° " + plan.getNroCuota());
				}
				if (creditoGenerado.compareTo(BigDecimal.ZERO) > 0) {
					imprimirNotaCredito(cliente.getNumero() != null ? cliente.getNumero().toString() : "N/A",
							creditoGenerado, cliente.getNombreCliente(),
							cliente.getSituacionFiscal() != null ? cliente.getSituacionFiscal().toString() : "N/A");
				}
				recargarDatos();
				if (plan.getEstado() == EstadoPlanPago.PAGADA) {
					btnPagar.setEnabled(false);
				}
				Notification.show("Pago registrado", 3000, Notification.Position.MIDDLE);
			} catch (Exception ex) {
				Notification.show("Error: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
			}
		});

		Button btnCancelar = new Button("Cancelar", _ -> dialog.close());
		btnCancelar.addClassName("btn-volver");

		dialog.add(montoField, formaPagoGroup, imprimirReciboCheck);
		dialog.getFooter().add(btnCancelar, btnAceptar);
		dialog.open();
	};

	/**
	 * Procesa el pago de una cuota. Soporta pago exacto, parcial (menor al saldo) y
	 * con excedente (mayor al saldo, el sobrante se descuenta de cuotas
	 * siguientes).
	 */
	private BigDecimal procesarPago(PlanPago plan, TipoPago tipoPago, BigDecimal montoPagar) {
		BigDecimal saldo = plan.getMontoOriginal().subtract(plan.getMontoPagado());

		// 1. Crear el pago con el monto real ingresado
		Pago pago = new Pago();
		pago.setIdCliente(cliente.getId());
		pago.setIdVenta(idVenta);
		pago.setFecha(LocalDate.now());
		pago.setMontoTotal(montoPagar);
		pago.setIdTipoPago(tipoPago.getId());
		pago.setObservaciones("Pago cuota N° " + plan.getNroCuota());
		pago.setAplicado(true);
		pago = pagoRepository.save(pago);

		// 2. Aplicar el pago a la cuota actual
		BigDecimal montoAplicadoCuota;
		BigDecimal montoADistribuir = BigDecimal.ZERO;

		if (montoPagar.compareTo(saldo) >= 0) {
			// Pago completo o con excedente
			montoAplicadoCuota = saldo;
			montoADistribuir = montoPagar.subtract(saldo);
			plan.setMontoPagado(plan.getMontoOriginal());
			plan.setEstado(EstadoPlanPago.PAGADA);
			plan.setFechaPago(LocalDate.now());
		} else {
			// Pago parcial
			montoAplicadoCuota = montoPagar;
			plan.setMontoPagado(plan.getMontoPagado().add(montoPagar));
			if (plan.getMontoDescuentoTotal() == null) {
				plan.setMontoDescuentoTotal(BigDecimal.ZERO);
			}
		}
		planPagoRepository.save(plan);

		// 3. Crear la aplicación de pago para la cuota actual
		AplicacionPago aplicacion = new AplicacionPago();
		aplicacion.setIdPago(pago.getIdPago());
		aplicacion.setIdPlanPago(plan.getIdPlanPago());
		aplicacion.setMontoAplicado(montoAplicadoCuota);
		aplicacion.setMontoNetoPagado(montoAplicadoCuota);
		aplicacion.setPctProntoPago(BigDecimal.ZERO);
		aplicacion.setMontoDescuentoAplicado(BigDecimal.ZERO);
		aplicacionPagoRepository.save(aplicacion);

		// 4. Distribuir excedente a cuotas siguientes de la misma venta
		if (montoADistribuir.compareTo(BigDecimal.ZERO) > 0) {
			montoADistribuir = distribuirExcedente(montoADistribuir, pago,
					planPagoRepository.findByPendienteByVentaId(idVenta).stream()
							.filter(p -> !p.getIdPlanPago().equals(plan.getIdPlanPago()))
							.sorted(Comparator.comparing(PlanPago::getNroCuota))
							.collect(Collectors.toList()));
		}

		// 4b. Si aún sobra, distribuir a cuotas pendientes de otras ventas del cliente
		if (montoADistribuir.compareTo(BigDecimal.ZERO) > 0) {
			montoADistribuir = distribuirExcedente(montoADistribuir, pago,
					planPagoRepository.findPendientesByClienteId(cliente.getId()).stream()
							.filter(p -> !p.getIdVenta().equals(idVenta))
							.sorted(Comparator.comparing(PlanPago::getIdVenta)
									.thenComparing(PlanPago::getNroCuota))
							.collect(Collectors.toList()));
		}

		// 4c. Si aún sobra saldo, generar nota de crédito
		if (montoADistribuir.compareTo(BigDecimal.ZERO) > 0) {
			NotaCredito nc = new NotaCredito(cliente.getId(), montoADistribuir, LocalDate.now(),
					"Saldo a favor generado por pago en exceso en venta " + idVenta);
			notaCreditoRepository.save(nc);

			pago.setObservaciones(
					pago.getObservaciones() + " | Saldo a favor: $" + montoADistribuir);
			pagoRepository.save(pago);

			Notification.show(
					"Saldo a favor de $" + montoADistribuir + " generado para "
							+ cliente.getNombreCliente(),
					5000, Notification.Position.MIDDLE);
		}

		// 5. Verificar si todas las cuotas de la venta están pagadas
		List<PlanPago> cuotasRestantes = planPagoRepository.findByPendienteByVentaId(idVenta);
		if (cuotasRestantes.isEmpty()) {
			venta.setEstado(EstadoVenta.COBRADA);
			ventaRepository.save(venta);
		}

		PagoBroadcaster.broadcastPagoRealizado();

		return montoADistribuir;
	}

	/**
	 * Abre el diálogo para pagar toda la deuda
	 */
	private void abrirDialogoPagarTodo() {
		List<PlanPago> pendientes = planesPago.stream().filter(p -> p.getEstado() != EstadoPlanPago.PAGADA).toList();
		if (pendientes.isEmpty()) {
			Notification.show("No hay deuda pendiente", 3000, Notification.Position.MIDDLE);
			return;
		}

		BigDecimal totalPendiente = pendientes.stream().map(p -> p.getMontoOriginal().subtract(p.getMontoPagado()))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		Dialog dialog = new Dialog();
		dialog.setHeaderTitle("Pagar Total de la Deuda");
		dialog.setWidth("450px");

		NumberField montoField = new NumberField("Monto total a pagar");
		montoField.setValue(totalPendiente.doubleValue());
		montoField.setWidthFull();

		RadioButtonGroup<TipoPago> formaPagoGroup = new RadioButtonGroup<>("Forma de pago");
		formaPagoGroup.setItems(tipoPagoRepository.findAll());
		formaPagoGroup.setItemLabelGenerator(TipoPago::getDescripcion);
		formaPagoGroup.setRequired(true);

		Checkbox imprimirReciboCheck = new Checkbox("Imprimir recibo de pago");
		imprimirReciboCheck.setValue(true);

		Button btnAceptar = new Button("Aceptar");
		btnAceptar.addClassName("btn-nuevo");
		btnAceptar.setEnabled(false);
		formaPagoGroup.addValueChangeListener(e -> btnAceptar.setEnabled(e.getValue() != null));

		btnAceptar.addClickListener(_ -> {
			TipoPago tipoPago = formaPagoGroup.getValue();
			if (tipoPago == null)
				return;

			BigDecimal montoPagar = BigDecimal.valueOf(montoField.getValue());
			if (montoPagar == null || montoPagar.compareTo(BigDecimal.ZERO) <= 0) {
				Notification.show("Ingrese un monto válido mayor a cero", 3000, Notification.Position.MIDDLE);
				return;
			}

			try {
				BigDecimal creditoGenerado = procesarPagoTotal(pendientes, tipoPago, montoPagar);
				dialog.close();
				if (imprimirReciboCheck.getValue()) {
					imprimirRecibo(venta.getNumeroComprobante().toString(), cliente.getNumero().toString(), cliente.getNombreCliente(),
							cliente.getSituacionFiscal().toString(), montoPagar.doubleValue(), "pago a cuenta");
				}
				if (creditoGenerado.compareTo(BigDecimal.ZERO) > 0) {
					imprimirNotaCredito(cliente.getNumero() != null ? cliente.getNumero().toString() : "N/A",
							creditoGenerado, cliente.getNombreCliente(),
							cliente.getSituacionFiscal() != null ? cliente.getSituacionFiscal().toString() : "N/A");
				}
				recargarDatos();
				Notification.show("Pago registrado", 3000, Notification.Position.MIDDLE);
			} catch (Exception ex) {
				Notification.show("Error: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
			}
		});

		Button btnCancelar = new Button("Cancelar", _ -> dialog.close());
		btnCancelar.addClassName("btn-volver");

		dialog.add(montoField, formaPagoGroup, imprimirReciboCheck);
		dialog.getFooter().add(btnCancelar, btnAceptar);
		dialog.open();
	}

	/**
	 * Procesa el pago distribuyendo el monto ingresado entre las cuotas pendientes,
	 * en orden de nroCuota. Si alcanza para todas, se marca la venta como COBRADA.
	 */
	private BigDecimal procesarPagoTotal(List<PlanPago> pendientes, TipoPago tipoPago, BigDecimal montoPagar) {
		List<PlanPago> ordenadas = pendientes.stream().sorted(Comparator.comparing(PlanPago::getNroCuota))
				.collect(Collectors.toList());

		// 1. Registrar pago único con el monto real ingresado
		Pago pago = new Pago();
		pago.setIdCliente(cliente.getId());
		pago.setIdVenta(idVenta);
		pago.setFecha(LocalDate.now());
		pago.setMontoTotal(montoPagar);
		pago.setIdTipoPago(tipoPago.getId());
		pago.setObservaciones("Pago a cuenta");
		pago.setAplicado(true);
		pago = pagoRepository.save(pago);

		BigDecimal montoADistribuir = montoPagar;

		// 2. Distribuir el monto entre las cuotas pendientes de esta venta
		montoADistribuir = distribuirExcedente(montoADistribuir, pago, ordenadas);

		// 2b. Si aún sobra saldo, generar nota de crédito
		if (montoADistribuir.compareTo(BigDecimal.ZERO) > 0) {
			NotaCredito nc = new NotaCredito(cliente.getId(), montoADistribuir, LocalDate.now(),
					"Saldo a favor generado por pago en exceso en venta " + idVenta);
			notaCreditoRepository.save(nc);

			pago.setObservaciones(
					pago.getObservaciones() + " | Saldo a favor: $" + montoADistribuir);
			pagoRepository.save(pago);

			Notification.show(
					"Saldo a favor de $" + montoADistribuir + " generado para "
							+ cliente.getNombreCliente(),
					5000, Notification.Position.MIDDLE);
		}

		// 3. Verificar si todas las cuotas están pagadas
		List<PlanPago> restantes = planPagoRepository.findByPendienteByVentaId(idVenta);
		if (restantes.isEmpty()) {
			venta.setEstado(EstadoVenta.COBRADA);
			ventaRepository.save(venta);
		}

		PagoBroadcaster.broadcastPagoRealizado();

		return montoADistribuir;
	}

	private BigDecimal distribuirExcedente(BigDecimal montoADistribuir, Pago pago, List<PlanPago> planes) {
		for (PlanPago sigPlan : planes) {
			if (montoADistribuir.compareTo(BigDecimal.ZERO) <= 0)
				break;

			BigDecimal sigSaldo = sigPlan.getMontoOriginal().subtract(sigPlan.getMontoPagado());
			if (sigSaldo.compareTo(BigDecimal.ZERO) <= 0)
				continue;

			BigDecimal aplicar;
			if (montoADistribuir.compareTo(sigSaldo) >= 0) {
				aplicar = sigSaldo;
				sigPlan.setMontoPagado(sigPlan.getMontoOriginal());
				sigPlan.setEstado(EstadoPlanPago.PAGADA);
				sigPlan.setFechaPago(LocalDate.now());
			} else {
				aplicar = montoADistribuir;
				sigPlan.setMontoPagado(sigPlan.getMontoPagado().add(aplicar));
			}
			planPagoRepository.save(sigPlan);
			montoADistribuir = montoADistribuir.subtract(aplicar);

			AplicacionPago ap = new AplicacionPago();
			ap.setIdPago(pago.getIdPago());
			ap.setIdPlanPago(sigPlan.getIdPlanPago());
			ap.setMontoAplicado(aplicar);
			ap.setMontoNetoPagado(aplicar);
			ap.setPctProntoPago(BigDecimal.ZERO);
			ap.setMontoDescuentoAplicado(BigDecimal.ZERO);
			aplicacionPagoRepository.save(ap);
		}
		return montoADistribuir;
	}

	private void recargarDatos() {
		cargarDatos();
		planesPago.sort(Comparator.comparing(PlanPago::getNroCuota));
		gridCuotas.setItems(planesPago);
		gridCuotas.getDataProvider().refreshAll();
		cargarAplicaciones();
		actualizarInfoVenta();
	}

	/**
	 * Imprime el resumen de la cuenta corriente actual
	 */
	private void imprimirResumen() {
		try {
			// 1. Convertir cuotas a beans
			List<VencimientoReporteBean> vencimientosBeans = planesPago.stream()
					.map(p -> new VencimientoReporteBean(p.getNroCuota(), DateTimeUtils.toDate(p.getFechaVencimiento()),
							p.getMontoOriginal(), p.getMontoOriginal().subtract(p.getMontoPagado()),
							p.getEstado().getLabel()))
					.collect(Collectors.toList());

			// 2. Obtener aplicaciones de pago y convertirlas a beans
			List<AplicacionReporteBean> aplicacionesBeans = aplicacionPagoRepository.findByVentaId(idVenta).stream()
					.map(a -> {
						// Obtener el pago asociado
						Pago pago = pagoRepository.findById(a.getIdPago()).orElse(null);
						// Obtener la fecha del pago (desde Pago, no desde AplicacionPago)
						Date fechaPago = pago != null ? DateTimeUtils.toDate(pago.getFecha()) : new Date();
						// Obtener la forma de pago (descripción desde TipoPago)
						String formaPago = "N/A";
						if (pago != null && pago.getIdTipoPago() != null) {
							formaPago = tipoPagoRepository.findById(pago.getIdTipoPago()).map(TipoPago::getDescripcion)
									.orElse("N/A");
						}
						return new AplicacionReporteBean(fechaPago, a.getMontoNetoPagado(), formaPago, "");
					}).collect(Collectors.toList());

			BigDecimal totalPagado = aplicacionesBeans.stream().map(AplicacionReporteBean::getMontoPagado)
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			byte[] pdfBytes = reportGenerator.generarResumenCuentaCorriente(DateTimeUtils.toDate(venta.getFechaVenta()),
					null, planesPago.stream().map(PlanPago::getMontoOriginal).reduce(BigDecimal.ZERO, BigDecimal::add),
					planesPago.stream().map(p -> p.getMontoOriginal().subtract(p.getMontoPagado()))
							.reduce(BigDecimal.ZERO, BigDecimal::add),
					"PLAN DE PAGOS", BigDecimal.ONE, cliente.getNombreCliente(),
					cliente.getNumero() != null ? cliente.getNumero().toString() : "N/A", obtenerDomicilioCliente(),
					obtenerTelefonoCliente(), totalPagado, new Date(),
					new JRBeanCollectionDataSource(vencimientosBeans),
					new JRBeanCollectionDataSource(aplicacionesBeans));

			StreamResource resumenResource = new StreamResource("resumen_plan_pago_" + idVenta + ".pdf",
					() -> new ByteArrayInputStream(pdfBytes));
			resumenResource.setContentType("application/pdf");
			resumenResource.setCacheTime(0);
			com.vaadin.flow.server.VaadinSession sesion2 = com.vaadin.flow.server.VaadinSession.getCurrent();
			String url2 = sesion2.getResourceRegistry().registerResource(resumenResource).getResourceUri().toString();

			UI.getCurrent().getPage().open(url2, "_blank");
			Notification.show("Resumen generado", 3000, Notification.Position.MIDDLE);
		} catch (Exception e) {
			Notification.show("Error al generar resumen: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
		}
	}

	private String obtenerDomicilioCliente() {
		if (cliente == null)
			return "N/A";

		// Si el cliente tiene domicilios cargados (relación EAGER o ya inicializada)
		if (cliente.getDomicilios() != null && !cliente.getDomicilios().isEmpty()) {
			Domicilio dom = cliente.getDomicilios().get(0); // Tomamos el primero (podría haber varios)
			String calle = dom.getCalle() != null ? dom.getCalle() : "";
			String numero = dom.getNumero() != null ? dom.getNumero().toString() : "";
			String barrio = dom.getBarrio() != null ? dom.getBarrio() : "";
			String localidad = (dom.getLocalidad() != null && dom.getLocalidad().getNombre() != null)
					? dom.getLocalidad().getNombre()
					: "";

			StringBuilder direccion = new StringBuilder();
			direccion.append(calle).append(" ").append(numero);
			if (!barrio.isEmpty())
				direccion.append(", ").append(barrio);
			if (!localidad.isEmpty())
				direccion.append(", ").append(localidad);
			return direccion.toString();
		}

		// Si no hay domicilios cargados, intentar buscarlos explícitamente (evitar
		// LazyInitialization)
		// Opcional: si tienes DomicilioRepository inyectado, puedes hacer una consulta
		// aquí
		// List<Domicilio> domicilios =
		// domicilioRepository.findByClienteId(cliente.getId());
		// if (!domicilios.isEmpty()) { ... }

		return "Domicilio no registrado";
	}

	private String obtenerTelefonoCliente() {
		if (cliente == null)
			return "N/A";

		// Priorizar teléfono móvil, luego fijo
		if (cliente.getTelefonoMovil() != null && !cliente.getTelefonoMovil().isBlank()) {
			return cliente.getTelefonoMovil();
		}
		if (cliente.getTelefonoFijo() != null && !cliente.getTelefonoFijo().isBlank()) {
			return cliente.getTelefonoFijo();
		}
		return "N/A";
	}

	/**
	 * Genera e imprime el recibo de pago.
	 */
	private void imprimirRecibo(String nroComprobante, String dniCliente, String nombreCliente, String situacionFiscal, Double monto,
			String detalleCuotas) {
		try {
			BigDecimal montoBig = BigDecimal.valueOf(monto);
			String montoTexto = MoneyConverters.SPANISH_BANKING_MONEY_VALUE.asWords(montoBig)
					.replaceAll("\\s?[€$£]\\s?", " ");
			montoTexto = montoTexto.substring(0, 1).toUpperCase() + montoTexto.substring(1);

			Map<String, Object> params = new HashMap<>();
			params.put("fechaPago", new Date());
			params.put("dniCliente", dniCliente);
			params.put("razonSocialCliente", nombreCliente);
			params.put("condicionIVACliente", situacionFiscal);
			params.put("montoPagado", montoBig);
			params.put("montoTexto", montoTexto);
			params.put("detalleCuotas", detalleCuotas);

			InputStream reportStream = getClass().getResourceAsStream("/reports/rptRecibo.jrxml");
			JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
			byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
			StreamResource resource = new StreamResource("reciboPago_" + nroComprobante + ".pdf", () -> new ByteArrayInputStream(pdfBytes));
			resource.setContentType("application/pdf");
			resource.setCacheTime(0);
			com.vaadin.flow.server.VaadinSession session = com.vaadin.flow.server.VaadinSession.getCurrent();
			String url = session.getResourceRegistry().registerResource(resource).getResourceUri().toString();

			UI.getCurrent().getPage().executeJs(
					"var a = document.createElement('a'); a.href = $0; a.download = $1; a.style.display = 'none'; document.body.appendChild(a); a.click(); document.body.removeChild(a);",
					url, "reciboPago_" + nroComprobante + ".pdf");

			Notification.show("Recibo generado", 3000, Notification.Position.MIDDLE);
		} catch (Exception e) {
			Notification.show("Error al generar recibo: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
		}
	}

	private void imprimirNotaCredito(String dniCliente, BigDecimal monto, String nombreCliente, String situacionFiscal) {
		try {
			String montoTexto = MoneyConverters.SPANISH_BANKING_MONEY_VALUE.asWords(monto)
					.replaceAll("\\s?[€$£]\\s?", " ");
			montoTexto = montoTexto.substring(0, 1).toUpperCase() + montoTexto.substring(1);

			Map<String, Object> params = new HashMap<>();
			params.put("fecha", new Date());
			params.put("dniCliente", dniCliente);
			params.put("razonSocialCliente", nombreCliente);
			params.put("condicionIVACliente", situacionFiscal);
			params.put("monto", monto);
			params.put("montoTexto", montoTexto);
			params.put("detalle", " de su Cuenta Corriente.");

			InputStream reportStream = getClass().getResourceAsStream("/reports/rptNotaCredito.jrxml");
			if (reportStream == null) {
				Notification.show("El reporte de nota de crédito no está disponible aún",
						3000, Notification.Position.MIDDLE);
				return;
			}

			JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
			byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
			StreamResource resource = new StreamResource("notaCredito_" + monto + ".pdf",
					() -> new ByteArrayInputStream(pdfBytes));
			resource.setContentType("application/pdf");
			resource.setCacheTime(0);
			com.vaadin.flow.server.VaadinSession session = com.vaadin.flow.server.VaadinSession.getCurrent();
			String url = session.getResourceRegistry().registerResource(resource).getResourceUri().toString();

			UI.getCurrent().getPage().executeJs(
					"var a = document.createElement('a'); a.href = $0; a.download = $1; a.style.display = 'none'; document.body.appendChild(a); a.click(); document.body.removeChild(a);",
					url, "notaCredito_" + monto + ".pdf");

			Notification.show("Nota de crédito generada", 3000, Notification.Position.MIDDLE);
		} catch (Exception e) {
			Notification.show("Error al generar nota de crédito: " + e.getMessage(),
					5000, Notification.Position.MIDDLE);
		}
	}

}