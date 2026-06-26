package com.visus.central.ui.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import com.visus.central.domain.model.Articulo;
import com.visus.central.domain.model.Lista;
import com.visus.central.domain.model.ListaPorcentual;
import com.visus.central.domain.model.TipoPorcentual;
import com.visus.central.domain.port.in.ArticuloUseCase;
import com.visus.central.domain.port.in.ListaUseCase;
import com.visus.central.ui.component.CentralLayout;

import org.openpdf.text.Document;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.openpdf.text.pdf.RGBColor;

@Route(value = "consulta-de-listas", layout = CentralLayout.class)
@PageTitle("Consulta de Listas de Precios")
public class ConsultaDeListasView extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    private final ListaUseCase listaService;
    private final ArticuloUseCase articuloService;

    private final ComboBox<Lista> listaCombo = new ComboBox<>("Seleccionar Lista");
    private final ProgressBar progressBar = new ProgressBar();
    private final Grid<PrecioListaRow> grid = new Grid<>(PrecioListaRow.class, false);
    private final Button inicio = new Button("Inicio", new Icon(VaadinIcon.HOME));
    private final Anchor downloadLink = new Anchor();
    private byte[] currentPdfBytes;
    private String currentPdfFileName;
    private List<PrecioListaRow> currentRows = new ArrayList<>();

    public ConsultaDeListasView(ListaUseCase listaService, ArticuloUseCase articuloService) {
        this.listaService = listaService;
        this.articuloService = articuloService;

        configurarVista();
        configurarGrid();
        cargarListas();
    }

    private void configurarVista() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        listaCombo.setWidth("400px");
        listaCombo.addClassName("campo-estilo-imagen");
        listaCombo.setItemLabelGenerator(Lista::getDescripcion);
        listaCombo.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                cargarArticulos(e.getValue());
            }
        });

        downloadLink.setText("Imprimir");
        downloadLink.addClassName("btn-nuevo");
        downloadLink.getStyle().set("display", "inline-flex");
        downloadLink.getStyle().set("align-items", "center");
        downloadLink.getStyle().set("cursor", "pointer");
        downloadLink.getStyle().set("text-decoration", "none");
        downloadLink.setVisible(false);
        downloadLink.getElement().setAttribute("download", true);

        inicio.addClassName("btn-volver-home");
        inicio.addClickListener(_ -> getUI().ifPresent(ui -> ui.navigate("")));

        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        progressBar.setWidth("100%");

        HorizontalLayout toolbar = new HorizontalLayout(listaCombo, downloadLink, inicio);
        toolbar.setAlignItems(Alignment.END);
        add(toolbar, progressBar, grid);
    }

    private void configurarGrid() {
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setColumnReorderingAllowed(true);
        grid.setHeight("500px");
        grid.addClassName("grid-documentacion-dark");
        grid.setSizeFull();

        grid.addColumn(PrecioListaRow::getCodigo).setHeader("CÓDIGO");
        grid.addColumn(PrecioListaRow::getDescripcion).setHeader("DESCRIPCIÓN").setWidth("250px");
        grid.addColumn(row -> "$ " + String.format("%.2f", row.getPrecioBase())).setHeader("PRECIO BASE").setTextAlign(ColumnTextAlign.END);
        grid.addColumn(row -> String.format("%.2f", row.getPorcentajeAjuste()) + "%").setHeader("% AJUSTE").setTextAlign(ColumnTextAlign.END);
        grid.addColumn(row -> "$ " + String.format("%.2f", row.getPrecioFinal())).setHeader("PRECIO FINAL").setTextAlign(ColumnTextAlign.END);
    }

    private void cargarListas() {
        List<Lista> listas = listaService.findAll();
        listaCombo.setItems(listas);
    }

    private void cargarArticulos(Lista lista) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Cálculo de Precios");
        dialog.setText("El cálculo de precios puede demorar varios minutos. ¿Desea continuar?");
        dialog.setCancelable(true);
        dialog.setConfirmText("Calcular");
        dialog.addConfirmListener(_ -> {
            progressBar.setVisible(true);
            UI ui = getUI().orElse(null);
            Lista listaSnapshot = lista;
            new Thread(() -> {
                List<Articulo> articulos = articuloService.findAll();
                BigDecimal totalAjuste = calcularAjusteTotal(listaSnapshot);

                List<PrecioListaRow> rows = articulos.stream()
                    .filter(a -> a.getPrecioCosto() != null)
                    .map(a -> calcularRow(a, totalAjuste))
                    .collect(Collectors.toList());

                currentRows = rows;
                currentPdfBytes = generarPdf(listaSnapshot);
                currentPdfFileName = "precios_" + listaSnapshot.getDescripcion().replaceAll("\\s+", "_") + ".pdf";

                if (ui != null) {
                    ui.access(() -> {
                        grid.setItems(currentRows);
                        progressBar.setVisible(false);

                        if (currentPdfBytes != null) {
                            downloadLink.setHref(DownloadHandler.fromInputStream(_ -> {
                                try {
                                    return new DownloadResponse(
                                        new ByteArrayInputStream(currentPdfBytes),
                                        currentPdfFileName,
                                        "application/pdf",
                                        currentPdfBytes.length);
                                } catch (Exception e) {
                                    return DownloadResponse.error(500);
                                }
                            }));
                            downloadLink.setVisible(true);
                        }
                    });
                }
            }).start();
        });
        dialog.open();
    }

    private BigDecimal calcularAjusteTotal(Lista lista) {
        if (lista.getItems() == null || lista.getItems().isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;
        for (ListaPorcentual item : lista.getItems()) {
            if (item.getPorcentual() != null && item.getPorcentual().getPorcentual() != null) {
                BigDecimal pct = item.getPorcentual().getPorcentual();
                if (item.getPorcentual().getClasificacion() == TipoPorcentual.Bonificación) {
                    total = total.subtract(pct);
                } else {
                    total = total.add(pct);
                }
            }
        }
        return total;
    }

    private PrecioListaRow calcularRow(Articulo a, BigDecimal totalAjuste) {
        BigDecimal precioCosto = a.getPrecioCosto();
        BigDecimal margen = a.getMargenUtilidad() != null ? a.getMargenUtilidad() : BigDecimal.ZERO;

        BigDecimal precioBase = precioCosto.add(
            precioCosto.multiply(margen).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));

        BigDecimal precioFinal = precioBase.add(
            precioBase.multiply(totalAjuste).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));

        return new PrecioListaRow(
            a.getCodigo_interno() != null ? a.getCodigo_interno() : "",
            a.getDescripcion() != null ? a.getDescripcion() : "",
            precioBase,
            totalAjuste,
            precioFinal);
    }

    private byte[] generarPdf(Lista lista) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            Font headerFontLeft = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Paragraph header = new Paragraph("Buldra Deportes", headerFontLeft);
            header.setAlignment(Element.ALIGN_LEFT);
            document.add(header);

            document.add(new Paragraph(" "));

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph title = new Paragraph("Lista de Precios", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph subtitle = new Paragraph(lista.getDescripcion(), subtitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitle);

            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.5f, 4f, 1.5f, 1f, 1.5f});

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            String[] headers = {"CÓDIGO", "DESCRIPCIÓN", "PRECIO BASE", "% AJUSTE", "PRECIO FINAL"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(new RGBColor(200, 200, 200));
                table.addCell(cell);
            }

            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
            for (PrecioListaRow row : currentRows) {
                table.addCell(new PdfPCell(new Phrase(row.getCodigo(), dataFont)));
                table.addCell(new PdfPCell(new Phrase(row.getDescripcion(), dataFont)));

                PdfPCell baseCell = new PdfPCell(new Phrase(
                    "$ " + row.getPrecioBase().setScale(2, RoundingMode.HALF_UP), dataFont));
                baseCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(baseCell);

                PdfPCell ajusteCell = new PdfPCell(new Phrase(
                    row.getPorcentajeAjuste().setScale(2, RoundingMode.HALF_UP) + "%", dataFont));
                ajusteCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(ajusteCell);

                PdfPCell finalCell = new PdfPCell(new Phrase(
                    "$ " + row.getPrecioFinal().setScale(2, RoundingMode.HALF_UP), dataFont));
                finalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(finalCell);
            }

            document.add(table);
            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            Notification.show("Error al generar PDF: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static class PrecioListaRow {
        private final String codigo;
        private final String descripcion;
        private final BigDecimal precioBase;
        private final BigDecimal porcentajeAjuste;
        private final BigDecimal precioFinal;

        public PrecioListaRow(String codigo, String descripcion, BigDecimal precioBase,
                BigDecimal porcentajeAjuste, BigDecimal precioFinal) {
            this.codigo = codigo;
            this.descripcion = descripcion;
            this.precioBase = precioBase;
            this.porcentajeAjuste = porcentajeAjuste;
            this.precioFinal = precioFinal;
        }

        public String getCodigo() { return codigo; }
        public String getDescripcion() { return descripcion; }
        public BigDecimal getPrecioBase() { return precioBase; }
        public BigDecimal getPorcentajeAjuste() { return porcentajeAjuste; }
        public BigDecimal getPrecioFinal() { return precioFinal; }
    }
}
