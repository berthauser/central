package com.visus.central.ui.view;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import com.visus.central.domain.model.Articulo;
import com.visus.central.domain.model.Linea;
import com.visus.central.domain.model.Rubro;
import com.vaadin.flow.component.UI;
import com.visus.central.domain.port.in.ArticuloUseCase;
import com.visus.central.domain.port.in.LineaUseCase;
import com.visus.central.domain.port.in.RubroUseCase;
import com.visus.central.infraestructure.util.ZplGenerator;
import com.visus.central.ui.component.CentralLayout;

import jakarta.annotation.PostConstruct;

@Route(value = "impresion-etiquetas", layout = CentralLayout.class)
@PageTitle("Impresión de Etiquetas")
public class ImpresionEtiquetasView extends VerticalLayout {

	private final RubroUseCase rubroUseCase;
	private final LineaUseCase lineaUseCase;
	private final ArticuloUseCase articuloUseCase;

	private final ComboBox<Rubro> rubroCombo = new ComboBox<>("Rubro");
	private final ComboBox<Linea> lineaCombo = new ComboBox<>("Línea");
	private final Button buscarBtn = new Button("Buscar");
	private final Grid<Articulo> grid = new Grid<>(Articulo.class, false);
	private final Button imprimirBtn = new Button("Imprimir Etiquetas", new Icon(VaadinIcon.PRINT));
	private final Anchor downloadLink = new Anchor();
	private final Checkbox todosCheck = new Checkbox("Seleccionar todos");
	private final IntegerField cantidadField = new IntegerField("Cantidad");

	private List<Articulo> articulos = new ArrayList<>();

	public ImpresionEtiquetasView(RubroUseCase rubroUseCase, LineaUseCase lineaUseCase,
			ArticuloUseCase articuloUseCase) {
		this.rubroUseCase = rubroUseCase;
		this.lineaUseCase = lineaUseCase;
		this.articuloUseCase = articuloUseCase;
	}

	@PostConstruct
	void init() {
		setSizeFull();
		setPadding(true);
		setSpacing(true);

		buildFilterBar();
		buildGrid();
		buildActionBar();

		rubroCombo.setItems(rubroUseCase.listar());
		rubroCombo.setItemLabelGenerator(Rubro::getDescripcion);
		rubroCombo.addValueChangeListener(e -> {
			lineaCombo.clear();
			if (e.getValue() != null) {
				lineaCombo.setItems(lineaUseCase.findByRubroId(e.getValue().getId()));
				lineaCombo.setEnabled(true);
			} else {
				lineaCombo.setEnabled(false);
			}
		});

		lineaCombo.setItemLabelGenerator(Linea::getDescripcion);
		lineaCombo.setEnabled(false);

		buscarBtn.addClickListener(_ -> buscar());
		todosCheck.addValueChangeListener(e -> {
			grid.asMultiSelect().deselectAll();
			if (e.getValue()) {
				grid.asMultiSelect().select(articulos);
			}
		});

		downloadLink.getElement().setAttribute("style", "display: none");
		add(downloadLink);
	}

	private void buildFilterBar() {
		HorizontalLayout filterBar = new HorizontalLayout();
		filterBar.setWidthFull();
		filterBar.setAlignItems(Alignment.END);

		rubroCombo.setWidth("300px");
		lineaCombo.setWidth("300px");
		buscarBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		filterBar.add(rubroCombo, lineaCombo, buscarBtn);
		add(filterBar);
	}

	private void buildGrid() {
		grid.setSizeFull();
		grid.addColumn(Articulo::getCodigo_barra).setHeader("Código Barra").setWidth("130px").setFlexGrow(0);
		grid.addColumn(Articulo::getCodigo_interno).setHeader("Código Interno").setWidth("130px").setFlexGrow(0);
		grid.addColumn(Articulo::getDescripcion).setHeader("Descripción").setWidth("250px");
		grid.addColumn(a -> a.getPresentacion() != null ? a.getPresentacion().getDescripcion() : "")
			.setHeader("Presentación").setWidth("120px").setFlexGrow(0);
		grid.addColumn(a -> a.getMedida() != null ? a.getMedida().getDescripcion() : "")
			.setHeader("Medida").setWidth("100px").setFlexGrow(0);
		grid.addColumn(Articulo::getStock).setHeader("Stock").setWidth("80px").setFlexGrow(0);

		grid.addColumn(new ComponentRenderer<>(articulo -> {
			IntegerField qtyField = new IntegerField();
			qtyField.setValue(articulo.getStock() != null ? articulo.getStock() : 1);
			qtyField.setMin(1);
			qtyField.setMax(9999);
			qtyField.setWidth("80px");
			qtyField.addValueChangeListener(e -> articuloQuantities.put(articulo.getId(), e.getValue()));
			return qtyField;
		})).setHeader("Cant.").setWidth("90px").setFlexGrow(0);

		grid.setSelectionMode(Grid.SelectionMode.MULTI);

		add(grid);
	}

	private void buildActionBar() {
		HorizontalLayout actionBar = new HorizontalLayout();
		actionBar.setWidthFull();
		actionBar.setAlignItems(Alignment.CENTER);

		todosCheck.getStyle().set("margin-left", "10px");
		cantidadField.setLabel("Cant. por defecto");
		cantidadField.setValue(1);
		cantidadField.setMin(1);
		cantidadField.setMax(9999);
		cantidadField.setWidth("150px");

		imprimirBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		imprimirBtn.addClickListener(_ -> generarZpl());

		actionBar.add(todosCheck, cantidadField, imprimirBtn);
		add(actionBar);
	}

	private void buscar() {
		Rubro rubro = rubroCombo.getValue();
		Linea linea = lineaCombo.getValue();

		if (rubro == null && linea == null) {
			Notification.show("Seleccione al menos un Rubro o una Línea");
			return;
		}

		if (linea != null) {
			articulos = articuloUseCase.buscarPorLinea(linea.getIdLinea());
		} else {
			articulos = articuloUseCase.buscarPorRubroId(rubro.getId());
		}

		if (rubro != null && linea == null) {
			articulos = articulos.stream()
					.filter(a -> a.getLinea() != null && a.getLinea().getRubro() != null
							&& a.getLinea().getRubro().getId().equals(rubro.getId()))
					.collect(Collectors.toList());
		}

		grid.setItems(articulos);
		todosCheck.setValue(false);
		Notification.show("Artículos encontrados: " + articulos.size());
	}

	private void generarZpl() {
		Set<Articulo> seleccionados = grid.asMultiSelect().getSelectedItems();
		if (seleccionados.isEmpty()) {
			Notification.show("Seleccione al menos un artículo");
			return;
		}

		int defaultQty = cantidadField.getValue() != null ? cantidadField.getValue() : 1;

		StringBuilder zplCompleto = new StringBuilder();
		zplCompleto.append("^XA\n");
		zplCompleto.append("^LL300\n");
		zplCompleto.append("^PW800\n");
		zplCompleto.append("^LS0\n");

		for (Articulo a : seleccionados) {
			int qty = articuloQuantities.getOrDefault(a.getId(), defaultQty);
			for (int i = 0; i < qty; i++) {
				String etiqueta = ZplGenerator.generarEtiqueta(
						a.getCodigo_barra(),
						a.getLinea() != null ? a.getLinea().getDescripcion() : null,
						a.getCodigo_interno(),
						a.getDescripcion(),
						a.getMedida() != null ? a.getMedida().getDescripcion() : null,
						a.getPresentacion() != null ? a.getPresentacion().getDescripcion() : null,
						a.getStock(),
						800, 300);
				zplCompleto.append(etiqueta);
			}
		}

		String zplContent = zplCompleto.toString();

		byte[] zplBytes = zplContent.getBytes(java.nio.charset.StandardCharsets.UTF_8);

		StreamResource resource = new StreamResource("etiquetas.zpl", () -> new ByteArrayInputStream(zplBytes));
		resource.setContentType("application/octet-stream");

		downloadLink.setHref(resource);
		downloadLink.getElement().setAttribute("download", "etiquetas.zpl");

		UI.getCurrent().getPage().executeJs(
			"const a = document.createElement('a');" +
			"a.href = this.href;" +
			"a.download = 'etiquetas.zpl';" +
			"document.body.appendChild(a);" +
			"a.click();" +
			"document.body.removeChild(a);",
			downloadLink.getElement()
		);
	}

	private final Map<Integer, Integer> articuloQuantities = new HashMap<>();
}
