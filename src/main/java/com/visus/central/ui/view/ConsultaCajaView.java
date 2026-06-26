package com.visus.central.ui.view;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import com.visus.central.domain.model.Caja;
import com.visus.central.domain.model.MovimientoCaja;
import com.visus.central.domain.model.OrigenMovimiento;
import com.visus.central.domain.port.in.CajaUseCase;
import com.visus.central.domain.port.out.CajaReadRepository;
import com.visus.central.domain.port.out.ReportGenerator;
import com.visus.central.domain.port.out.dto.ReporteCajaDiaria;
import com.visus.central.ui.component.CentralLayout;
import com.visus.central.infraestructure.util.FormatoUtils;

@Route(value = "consulta-caja", layout = CentralLayout.class)
@PageTitle("Consulta de Caja")
public class ConsultaCajaView extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    private CajaReadRepository cajaReportRepo;

    @Autowired
    private ReportGenerator reportGenerator;

    private final CajaUseCase cajaService;
    private final Grid<Caja> gridCajas = new Grid<>(Caja.class, false);
    private final DatePicker desdePicker = new DatePicker("Desde");
    private final DatePicker hastaPicker = new DatePicker("Hasta");
    private final Button buscarBtn = new Button("Buscar", new Icon(VaadinIcon.SEARCH));
    private final Button inicio = new Button("Inicio", new Icon(VaadinIcon.HOME));

    @Autowired
    public ConsultaCajaView(CajaUseCase cajaService) {
        this.cajaService = cajaService;
        configurarVista();
        configurarGrid();
    }

    private void configurarVista() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        desdePicker.setValue(LocalDate.now().withDayOfMonth(1));
        hastaPicker.setValue(LocalDate.now());
        desdePicker.setWidth("180px");
        hastaPicker.setWidth("180px");

        buscarBtn.addClassName("btn-nuevo");
        buscarBtn.addClickListener(_ -> buscar());

        inicio.addClassName("btn-volver-home");
        inicio.addClickListener(_ -> getUI().ifPresent(ui -> ui.navigate("")));

        HorizontalLayout toolbar = new HorizontalLayout(desdePicker, hastaPicker, buscarBtn, inicio);
        toolbar.setAlignItems(Alignment.END);
        add(toolbar, gridCajas);
    }

    private void configurarGrid() {
        gridCajas.addColumn(Caja::getId).setHeader("ID").setTextAlign(ColumnTextAlign.END).setWidth("70px");

        gridCajas.addColumn(c -> c.getFechaApertura() != null ? c.getFechaApertura().format(DATE_FMT) : "")
                .setHeader("FECHA APERTURA").setWidth("130px");

        gridCajas.addColumn(c -> c.getHoraApertura() != null ? c.getHoraApertura().format(TIME_FMT) : "")
                .setHeader("HORA APERTURA").setWidth("120px");

        gridCajas.addColumn(c -> c.getFechaCierre() != null ? c.getFechaCierre().format(DATE_FMT) : "")
                .setHeader("FECHA CIERRE").setWidth("130px");

        gridCajas.addColumn(c -> c.getHoraCierre() != null ? c.getHoraCierre().format(TIME_FMT) : "")
                .setHeader("HORA CIERRE").setWidth("120px");

        gridCajas.addColumn(c -> FormatoUtils.formatPesos(c.getSaldoInicial()))
                .setHeader("SALDO INICIAL").setTextAlign(ColumnTextAlign.END).setWidth("130px");

        gridCajas.addColumn(c -> FormatoUtils.formatPesos(c.getSaldoRealCierre()))
                .setHeader("SALDO CIERRE").setTextAlign(ColumnTextAlign.END).setWidth("130px");

        gridCajas.addColumn(new ComponentRenderer<>(caja -> {
            Button detalleBtn = new Button("Ver Detalle", new Icon(VaadinIcon.EYE));
            detalleBtn.addClassName("btn-nuevo");
            detalleBtn.addClickListener(_ -> mostrarDetalleCaja(caja));
            return detalleBtn;
        })).setHeader("ACCIÓN").setWidth("160px");

        gridCajas.setSizeFull();
        gridCajas.setHeight("500px");
        gridCajas.addClassName("grid-documentacion-dark");
    }

    private void buscar() {
        LocalDate desde = desdePicker.getValue();
        LocalDate hasta = hastaPicker.getValue();
        if (desde == null || hasta == null) {
            Notification.show("Seleccione rango de fechas");
            return;
        }
        if (hasta.isBefore(desde)) {
            Notification.show("La fecha 'Hasta' debe ser posterior a 'Desde'");
            return;
        }
        List<Caja> cajas = cajaService.obtenerCajasCerradasPorRango(desde, hasta);
        gridCajas.setItems(cajas);
        if (cajas.isEmpty()) {
            Notification.show("No se encontraron cajas cerradas en el rango seleccionado");
        }
    }

    private void mostrarDetalleCaja(Caja caja) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Caja #" + caja.getId() + " - Detalle de Movimientos");
        dialog.setWidth("900px");
        dialog.setHeight("600px");

        Grid<MovimientoCaja> gridMov = new Grid<>(MovimientoCaja.class, false);
        gridMov.setSizeFull();

        gridMov.addColumn(m -> m.getFecha() != null ? m.getFecha().format(DATE_FMT) : "")
                .setHeader("FECHA").setWidth("110px");
        gridMov.addColumn(m -> m.getHora() != null ? m.getHora().format(TIME_FMT) : "")
                .setHeader("HORA").setWidth("100px");
        gridMov.addColumn(MovimientoCaja::getDescripcion).setHeader("DESCRIPCIÓN").setWidth("200px");

        gridMov.addColumn(new ComponentRenderer<>(mov -> {
            String label = mov.getOrigen() != null ? mov.getOrigen().toString() : "";
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
        })).setHeader("ORIGEN").setWidth("120px");

        gridMov.addColumn(m -> m.esIngreso() ? FormatoUtils.formatPesos(m.getDebe()) : "")
                .setHeader("DEBE").setTextAlign(ColumnTextAlign.END).setWidth("120px");
        gridMov.addColumn(m -> m.esEgreso() ? FormatoUtils.formatPesos(m.getHaber()) : "")
                .setHeader("HABER").setTextAlign(ColumnTextAlign.END).setWidth("120px");

        List<MovimientoCaja> movimientos = cajaService.obtenerMovimientosDeCaja(caja.getId(), null);
        gridMov.setItems(movimientos);

        FooterRow footer = gridMov.appendFooterRow();
        BigDecimal totalDebe = movimientos.stream().filter(MovimientoCaja::esIngreso)
                .map(MovimientoCaja::getDebe).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalHaber = movimientos.stream().filter(MovimientoCaja::esEgreso)
                .map(MovimientoCaja::getHaber).reduce(BigDecimal.ZERO, BigDecimal::add);
        Grid.Column<MovimientoCaja> colDebe = gridMov.getColumns().get(4);
        Grid.Column<MovimientoCaja> colHaber = gridMov.getColumns().get(5);
        footer.getCell(gridMov.getColumns().get(3)).setText("Total:");
        footer.getCell(colDebe).setText(FormatoUtils.formatPesos(totalDebe));
        colDebe.getElement().getStyle().set("font-weight", "bold").set("color", "#2e7d32");
        footer.getCell(colHaber).setText(FormatoUtils.formatPesos(totalHaber));
        colHaber.getElement().getStyle().set("font-weight", "bold").set("color", "#c62828");

        Button imprimirBtn = new Button("Imprimir", new Icon(VaadinIcon.PRINT));
        imprimirBtn.addClassName("btn-nuevo");
        imprimirBtn.addClickListener(_ -> imprimirReporteCaja(caja));

        Button cerrarBtn = new Button("Cerrar", _ -> dialog.close());
        cerrarBtn.addThemeVariants(com.vaadin.flow.component.button.ButtonVariant.LUMO_TERTIARY);

        dialog.getFooter().add(cerrarBtn, imprimirBtn);
        dialog.add(gridMov);
        dialog.open();
    }

    private void imprimirReporteCaja(Caja caja) {
        try {
            ReporteCajaDiaria reporte = cajaReportRepo.obtenerReporteCajaDiaria(caja.getId());
            byte[] pdf = reportGenerator.generarReporteCajaDiaria(reporte);
            descargarPdf(pdf, "CajaDiaria_" + caja.getId() + ".pdf");
        } catch (Exception e) {
            Notification.show("Error al generar reporte: " + e.getMessage(), 5000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
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
}
