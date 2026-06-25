package com.visus.central.ui.view;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import com.visus.central.domain.model.Caja;
import com.visus.central.domain.model.MovimientoCaja;
import com.visus.central.domain.model.OrigenMovimiento;
import com.visus.central.domain.port.in.CajaUseCase;
import com.visus.central.domain.port.in.MovimientoManualRec;
import com.visus.central.domain.port.out.CajaReadRepository;
import com.visus.central.domain.port.out.ReportGenerator;
import com.visus.central.domain.port.out.dto.ReporteCajaDiaria;
import com.visus.central.ui.component.CentralLayout;
import com.visus.central.infraestructure.util.PagoBroadcaster;
import com.visus.central.infraestructure.util.FormatoUtils;

@Route(value = "caja", layout = CentralLayout.class)
@PageTitle("Caja Diaria")
public class CajaView extends VerticalLayout implements Runnable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private CajaReadRepository cajaReportRepo;

	@Autowired
	private ReportGenerator reportGenerator;

	private final CajaUseCase cajaService;
	private Grid<MovimientoCaja> gridMovimientos;
	private Span saldoInicialLabel;
	private Span saldoActualLabel;
	private Button btnAbrirCaja, btnCerrarCaja, btnNuevoMovimiento;
	private Caja cajaActual;
	private final Button inicio = new Button("Inicio", new Icon(VaadinIcon.HOME));

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Autowired
	public CajaView(CajaUseCase cajaService) {
		this.cajaService = cajaService;
		buildUI();
		actualizarEstado();
	}

	private void buildUI() {
		setSizeFull();
		setPadding(true);
		setSpacing(true);

		// Panel superior
		HorizontalLayout infoLayout = new HorizontalLayout();
		infoLayout.setWidthFull();
		infoLayout.setSpacing(true);
		infoLayout.setAlignItems(Alignment.BASELINE);

		saldoInicialLabel = new Span();
		saldoInicialLabel.getStyle().set("font-size", "1.2em").set("font-weight", "bold");

		saldoActualLabel = new Span();
		saldoActualLabel.getStyle().set("font-size", "1.2em").set("font-weight", "bold");

		btnAbrirCaja = new Button("Abrir Caja", _ -> abrirCaja());
		btnAbrirCaja.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		btnCerrarCaja = new Button("Cerrar Caja", _ -> cerrarCaja());
		btnCerrarCaja.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);

		btnNuevoMovimiento = new Button("Registrar Movimiento", _ -> mostrarDialogoMovimiento());
		btnNuevoMovimiento.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		btnNuevoMovimiento.setIcon(new Icon(VaadinIcon.PLUS));

		inicio.addClassName("btn-volver-home");
		inicio.addClickListener(_ -> UI.getCurrent().navigate(HomeView.class));

		infoLayout.add(saldoInicialLabel, saldoActualLabel, btnAbrirCaja, btnNuevoMovimiento, btnCerrarCaja, inicio);
		add(infoLayout);

		// Configurar grilla
		configurarGrilla();
		add(gridMovimientos);
	}

	private void configurarGrilla() {
		gridMovimientos = new Grid<>(MovimientoCaja.class, false);

		gridMovimientos.addColumn(mov -> mov.getFecha() != null ? mov.getFecha().format(DATE_FORMATTER) : "")
				.setHeader("FECHA").setKey("fecha").setTextAlign(ColumnTextAlign.START).setResizable(true);

		gridMovimientos.addColumn(mov -> mov.getHora() != null ? mov.getHora().format(TIME_FORMATTER) : "")
				.setHeader("HORA").setKey("hora").setTextAlign(ColumnTextAlign.START).setResizable(true);

		gridMovimientos.addColumn(MovimientoCaja::getDescripcion).setHeader("DESCRIPCIÓN").setKey("descripcion")
				.setTextAlign(ColumnTextAlign.START).setResizable(true);

		gridMovimientos.addColumn(new ComponentRenderer<>(mov -> {
			String label = mov.getOrigen() != null ? mov.getOrigen().toString() : "";
			if (mov.getOrigen() == OrigenMovimiento.MANUAL) {
				label = "\u00a0\u00a0\u00a0\u00a0" + label + "\u00a0\u00a0\u00a0";
			}
			Span badge = new Span(label);
			var theme = badge.getElement().getThemeList();
			theme.add("badge");
			if (mov.getOrigen() == OrigenMovimiento.AUTOMATICO) {
				theme.add("success");
			} else {
				theme.add("contrast");
				badge.getStyle().set("color", "var(--lumo-warning-text-color)");
			}
			return badge;
		})).setHeader("ORIGEN").setKey("Origen").setTextAlign(ColumnTextAlign.START).setResizable(true);

		// Columna Debe usando FormatoUtils
		gridMovimientos.addColumn(mov -> mov.esIngreso() ? FormatoUtils.formatPesos(mov.getDebe()) : "")
				.setHeader("DEBE").setKey("debe").setTextAlign(ColumnTextAlign.END).setResizable(true);

		// Columna Haber usando FormatoUtils
		gridMovimientos.addColumn(mov -> mov.esEgreso() ? FormatoUtils.formatPesos(mov.getHaber()) : "")
				.setHeader("HABER").setKey("haber").setTextAlign(ColumnTextAlign.END).setResizable(true);

		gridMovimientos.setSizeFull();
		gridMovimientos.setHeight("500px");
		gridMovimientos.addClassName("grid-documentacion-dark");

		gridMovimientos.getElement().executeJs(
			"this.addEventListener('dblclick', function(e) {" +
			"  const path = e.composedPath();" +
			"  const cellContent = path.find(el => el.tagName === 'VAADIN-GRID-CELL-CONTENT');" +
			"  if (!cellContent) return;" +
			"  const slot = cellContent.assignedSlot;" +
			"  if (!slot) return;" +
			"  const col = slot.parentElement?._column;" +
			"  if (!col) return;" +
			"  function measure(el) {" +
			"    if (!el) return 50;" +
			"    var font = window.getComputedStyle(el).font || '16px sans-serif';" +
			"    var temp = document.createElement('span');" +
			"    temp.style.cssText = 'position:fixed;visibility:hidden;white-space:nowrap;font:' + font + ';padding:0 40px';" +
			"    temp.textContent = el.textContent || '';" +
			"    document.body.appendChild(temp);" +
			"    var w = temp.offsetWidth;" +
			"    document.body.removeChild(temp);" +
			"    return Math.max(w, 50);" +
			"  }" +
			"  var maxWidth = 0;" +
			"  (col._cells || []).concat(col._headerCell ? [col._headerCell] : []).concat(col._footerCell ? [col._footerCell] : []).forEach(function(cell) {" +
			"    var el = cell._content || cell.querySelector('vaadin-grid-cell-content');" +
			"    var w = measure(el);" +
			"    if (w > maxWidth) maxWidth = w;" +
			"  });" +
			"  if (maxWidth > 0) { col.width = maxWidth + 'px'; col.flexGrow = 0; }" +
			"});"
		);

		// Footer con totales resaltados
		FooterRow footerRow = gridMovimientos.appendFooterRow();

		// Columna Debe
		Grid.Column<MovimientoCaja> colDebe = gridMovimientos.getColumnByKey("debe");
		footerRow.getCell(colDebe).setText("Total Debe: " + FormatoUtils.formatPesos(BigDecimal.ZERO));
		colDebe.getElement().getStyle().set("font-weight", "bold").set("color", "#2e7d32");

		// Columna Haber
		Grid.Column<MovimientoCaja> colHaber = gridMovimientos.getColumnByKey("haber");
		footerRow.getCell(colHaber).setText("Total Haber: " + FormatoUtils.formatPesos(BigDecimal.ZERO));
		colHaber.getElement().getStyle().set("font-weight", "bold").set("color", "#c62828");
	}

	// ==================== Broadcaster ====================

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
		getUI().ifPresent(ui -> ui.access(this::ejecutarRefresco));
	}

	private void ejecutarRefresco() {
		if (cajaActual != null && cajaActual.estaAbierta()) {
	        // Traer todos los movimientos (sin filtrar por fecha)
	        List<MovimientoCaja> movimientos = cajaService.obtenerMovimientosDeCaja(cajaActual.getId(), null);
	        gridMovimientos.setItems(movimientos);
	        actualizarTotalesEnFooter();
	        BigDecimal saldoActual = cajaService.calcularSaldoActual(cajaActual);
	        saldoActualLabel.setText("Saldo Actual: " + FormatoUtils.formatPesos(saldoActual));
	    }
	}

	private void actualizarTotalesEnFooter() {
		List<MovimientoCaja> movimientos = gridMovimientos.getListDataView().getItems().toList();
		BigDecimal totalDebe = movimientos.stream().filter(MovimientoCaja::esIngreso).map(MovimientoCaja::getDebe)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal totalHaber = movimientos.stream().filter(MovimientoCaja::esEgreso).map(MovimientoCaja::getHaber)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		FooterRow footerRow = gridMovimientos.getFooterRows().get(0);

		Grid.Column<MovimientoCaja> colDebe = gridMovimientos.getColumnByKey("debe");
		footerRow.getCell(colDebe).setText("Total Debe: " + FormatoUtils.formatPesos(totalDebe));

		Grid.Column<MovimientoCaja> colHaber = gridMovimientos.getColumnByKey("haber");
		footerRow.getCell(colHaber).setText("Total Haber: " + FormatoUtils.formatPesos(totalHaber));
	}

	private void actualizarEstado() {
		cajaActual = cajaService.obtenerCajaActual();
		if (cajaActual != null && cajaActual.estaAbierta()) {
			BigDecimal saldoActual = cajaService.calcularSaldoActual(cajaActual);
			saldoInicialLabel.setText("Saldo Inicial: " + FormatoUtils.formatPesos(cajaActual.getSaldoInicial()));
			saldoActualLabel.setText("Saldo Actual: " + FormatoUtils.formatPesos(saldoActual));
			btnAbrirCaja.setEnabled(false);
			btnCerrarCaja.setEnabled(true);
			btnNuevoMovimiento.setEnabled(true);
			cargarMovimientos();
		} else {
			saldoInicialLabel.setText("Caja cerrada");
			saldoActualLabel.setText("");
			btnAbrirCaja.setEnabled(true);
			btnCerrarCaja.setEnabled(false);
			btnNuevoMovimiento.setEnabled(false);
			gridMovimientos.setItems(List.of());
			actualizarTotalesEnFooter();
		}
	}

	private void cargarMovimientos() {
		if (cajaActual != null) {
	        List<MovimientoCaja> movimientos;
	        if (cajaActual.estaAbierta()) {
	            // Si está abierta, traer todos los movimientos (sin filtrar por fecha)
	            movimientos = cajaService.obtenerMovimientosDeCaja(cajaActual.getId(), null);
	        } else {
	            // Si está cerrada, solo los del día de cierre (o fecha actual)
	            movimientos = cajaService.obtenerMovimientosDeCaja(cajaActual.getId(), LocalDate.now());
	        }
	        gridMovimientos.setItems(movimientos);
	        actualizarTotalesEnFooter();
	    }
	}

	private void abrirCaja() {
		Dialog dialog = new Dialog();
		dialog.setHeaderTitle("Abrir Caja");

		// Obtener saldo sugerido
		BigDecimal saldoSugerido = cajaService.obtenerSaldoInicialSugerido();

		FormLayout form = new FormLayout();
		BigDecimalField saldoInicialField = new BigDecimalField("Saldo Inicial");
		saldoInicialField.setValue(saldoSugerido); // valor sugerido (0 o último cierre)
		saldoInicialField.setRequiredIndicatorVisible(true);
		TextArea observacionesField = new TextArea("Observaciones");

		Button guardar = new Button("Abrir", _ -> {
			try {
				Integer idUsuario = 1; // TODO: obtener usuario autenticado
				Caja caja = cajaService.abrirCaja(saldoInicialField.getValue(), idUsuario,
						observacionesField.getValue());
				Notification
						.show("Caja abierta con saldo: $ " + caja.getSaldoInicial(), 3000, Notification.Position.MIDDLE)
						.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				dialog.close();
				actualizarEstado();
			} catch (Exception ex) {
				Notification.show("Error: " + ex.getMessage(), 5000, Notification.Position.MIDDLE)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});
		guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		Button cancelar = new Button("Cancelar", _ -> dialog.close());
		cancelar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		dialog.getFooter().add(cancelar, guardar);
		dialog.add(form, saldoInicialField, observacionesField);
		dialog.open();
	}

	private void cerrarCaja() {
		if (cajaActual == null)
			return;

		Dialog dialog = new Dialog();
		dialog.setHeaderTitle("Cerrar Caja");

		// Calcular saldo actual sugerido
		BigDecimal saldoActual = cajaService.calcularSaldoActual(cajaActual);

		Checkbox generarReporteCheck = new Checkbox("Generar reporte de cierre");

		// Layout vertical para campos
		VerticalLayout formLayout = new VerticalLayout();
		formLayout.setSpacing(true);
		formLayout.setPadding(false);

		BigDecimalField saldoRealField = new BigDecimalField("Saldo real en caja");
		saldoRealField.setValue(saldoActual); // valor sugerido
		saldoRealField.setRequiredIndicatorVisible(true);
		saldoRealField.setWidthFull();

		TextArea observacionesField = new TextArea("Observaciones");
		observacionesField.setWidthFull();

		formLayout.add(saldoRealField, observacionesField, generarReporteCheck);

		Button cerrarBtn = new Button("Cerrar", _ -> {
			try {
				Integer idUsuario = 1; // TODO: usuario autenticado
				cajaService.cerrarCaja(cajaActual.getId(), saldoRealField.getValue(), idUsuario,
						observacionesField.getValue());
				Notification.show("Caja cerrada exitosamente").addThemeVariants(NotificationVariant.LUMO_SUCCESS);

				// Si el usuario marcó generar reporte, lo creamos y descargamos
				if (generarReporteCheck.getValue()) {
					ReporteCajaDiaria reporte = cajaReportRepo.obtenerReporteCajaDiaria(cajaActual.getId());
					byte[] pdf = reportGenerator.generarReporteCajaDiaria(reporte);
					descargarPdf(pdf, "CajaDiaria_" + cajaActual.getId() + ".pdf");
				}

				dialog.close();
				actualizarEstado();

			} catch (Exception ex) {
				Notification.show("Error: " + ex.getMessage(), 5000, Notification.Position.MIDDLE)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});
		cerrarBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
		Button cancelar = new Button("Cancelar", _ -> dialog.close());
		cancelar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		dialog.getFooter().add(cancelar, cerrarBtn);
		dialog.add(formLayout);
		dialog.open();
	}

	private void descargarPdf(byte[] pdf, String nombreArchivo) {
		if (pdf == null || pdf.length == 0) {
			Notification.show("El reporte está vacío", 3000, Notification.Position.MIDDLE)
					.addThemeVariants(NotificationVariant.LUMO_WARNING);
			return;
		}

		Anchor anchor = new Anchor(DownloadHandler.fromInputStream(_ -> new DownloadResponse(
				new ByteArrayInputStream(pdf),
				nombreArchivo,
				"application/pdf",
				pdf.length)), "");
		anchor.setTarget("_blank");
		anchor.setVisible(false);
		add(anchor);

		UI.getCurrent().getPage().open(anchor.getHref(), "_blank");
	}

	private void mostrarDialogoMovimiento() {
		if (cajaActual == null)
			return;

		Dialog dialog = new Dialog();
		dialog.setHeaderTitle("Registrar Movimiento Manual");

		FormLayout form = new FormLayout();
		BigDecimalField montoField = new BigDecimalField("Monto");
		montoField.setRequiredIndicatorVisible(true);

		Select<String> tipoSelect = new Select<>();
		tipoSelect.setLabel("Tipo");
		tipoSelect.setItems("Ingreso", "Egreso");
		tipoSelect.setValue("Ingreso");

		TextField descripcionField = new TextField("Descripción");
		descripcionField.setRequiredIndicatorVisible(true);

		// Campos opcionales (por ahora no usamos tipoPago ni comprobante)
		// Select<TipoPago> tipoPagoSelect = ...;
		Button guardar = new Button("Guardar", _ -> {
			try {
				MovimientoManualRec cmd = new MovimientoManualRec(cajaActual.getId(), montoField.getValue(),
						"Ingreso".equals(tipoSelect.getValue()), descripcionField.getValue(), null, // idTipoPago
						null // idComprobante
				);
				cajaService.registrarMovimientoManual(cmd);
				Notification.show("Movimiento registrado", 3000, Notification.Position.MIDDLE)
						.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				dialog.close();
				actualizarEstado(); // refresca saldo y grilla
			} catch (Exception ex) {
				Notification.show("Error: " + ex.getMessage(), 5000, Notification.Position.MIDDLE)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});

		guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		Button cancelar = new Button("Cancelar", _ -> dialog.close());
		cancelar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		dialog.getFooter().add(cancelar, guardar);
		dialog.add(form, montoField, tipoSelect, descripcionField);
		dialog.open();
	}

}
