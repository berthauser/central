package com.visus.central.ui.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
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
import com.visus.central.domain.model.ReglaComision;
import com.visus.central.domain.model.TipoBaseComisionable;
import com.visus.central.domain.model.TipoCalculoComision;
import com.visus.central.domain.model.TipoEventoComision;
import com.visus.central.domain.model.TramoComision;
import com.visus.central.domain.model.Vendedor;
import com.visus.central.domain.port.in.ReglaComisionUseCase;
import com.visus.central.domain.port.out.VendedorRepository;
import com.visus.central.ui.component.CentralLayout;

import jakarta.annotation.security.PermitAll;

@Route(value = "reglas-comision", layout = CentralLayout.class)
@PageTitle("Reglas de Comisión")
@PermitAll
public class ReglaComisionView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final ReglaComisionUseCase reglaComisionUseCase;
	private final VendedorRepository vendedorRepository;

	private Grid<ReglaComision> grid;
	private Button btnNuevo;
	private Button btnInicio;

	@Autowired
	public ReglaComisionView(ReglaComisionUseCase reglaComisionUseCase, VendedorRepository vendedorRepository) {
		this.reglaComisionUseCase = reglaComisionUseCase;
		this.vendedorRepository = vendedorRepository;

		initUI();
		recargarDatos();
	}

	private void initUI() {
		setSizeFull();
		setPadding(true);
		setSpacing(true);

		H2 titulo = new H2("Reglas de Comisión");
		titulo.getStyle().set("margin", "0 0 20px 0");

		btnNuevo = new Button("Nueva Regla", _ -> abrirDialog(null));
		btnNuevo.addClassName("btn-nuevo");

		btnInicio = new Button("Inicio");
		btnInicio.setIcon(new Icon(VaadinIcon.HOME));
		btnInicio.addClassName("btn-volver-home");
		btnInicio.addClickListener(_ -> UI.getCurrent().navigate(""));

		grid = new Grid<>(ReglaComision.class, false);
		grid.addClassName("grid-documentacion-dark");
		grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		grid.setHeight("500px");
		configurarGrid();

		HorizontalLayout toolbar = new HorizontalLayout(btnNuevo, btnInicio);
		toolbar.setAlignItems(Alignment.BASELINE);

		add(titulo, toolbar, grid);
	}

	private void configurarGrid() {
		grid.addColumn(r -> r.getVendedor() != null ? r.getVendedor().getNombre() : "(Global)")
				.setHeader("Vendedor").setWidth("200px").setSortable(true);
		grid.addColumn(ReglaComision::getNombre).setHeader("Nombre").setWidth("250px");
		grid.addColumn(r -> r.getTipoBaseComisionable().getLabel()).setHeader("Base Comisionable")
				.setWidth("160px");
		grid.addColumn(r -> r.getTipoCalculo().getLabel()).setHeader("Tipo Cálculo").setWidth("120px");
		grid.addColumn(r -> {
			if (r.getTipoCalculo() == TipoCalculoComision.PORCENTAJE) {
				return String.format("%.2f%%", r.getValorCalculo());
			}
			return String.format("$%.2f", r.getValorCalculo());
		}).setHeader("Valor").setWidth("100px");
		grid.addColumn(r -> r.getTipoEvento().getLabel()).setHeader("Evento").setWidth("140px");
		grid.addColumn(r -> Boolean.TRUE.equals(r.getActivo()) ? "Sí" : "No").setHeader("Activo")
				.setWidth("80px");

		grid.addComponentColumn(r -> {
			HorizontalLayout acc = new HorizontalLayout();
			Button editar = new Button("Editar");
			editar.addClassName("btn-modificar");
			editar.addClickListener(_ -> abrirDialog(r));
			Button eliminar = new Button("Eliminar");
			eliminar.addClassName("btn-eliminar");
			eliminar.addClickListener(_ -> {
				reglaComisionUseCase.deleteById(r.getId());
				recargarDatos();
				Notification.show("Regla eliminada", 3000, Notification.Position.MIDDLE);
			});
			acc.add(editar, eliminar);
			return acc;
		}).setHeader("Acciones").setWidth("200px");
	}

	private void recargarDatos() {
		grid.setItems(reglaComisionUseCase.findAll());
	}

	private void abrirDialog(ReglaComision existente) {
		Dialog dialog = new Dialog();
		dialog.setHeaderTitle(existente == null ? "Nueva Regla de Comisión" : "Editar Regla de Comisión");
		dialog.setWidth("600px");

		ComboBox<Vendedor> cmbVendedor = new ComboBox<>("Vendedor");
		cmbVendedor.setItemLabelGenerator(v -> v.getNombre() + " (" + v.getNumero() + ")");
		cmbVendedor.setItems(vendedorRepository.findAll());
		cmbVendedor.setClearButtonVisible(true);

		TextField txtNombre = new TextField("Nombre");
		txtNombre.setWidthFull();

		ComboBox<TipoBaseComisionable> cmbBase = new ComboBox<>("Base Comisionable");
		cmbBase.setItems(TipoBaseComisionable.values());
		cmbBase.setItemLabelGenerator(TipoBaseComisionable::getLabel);

		ComboBox<TipoCalculoComision> cmbCalculo = new ComboBox<>("Tipo de Cálculo");
		cmbCalculo.setItems(TipoCalculoComision.values());
		cmbCalculo.setItemLabelGenerator(TipoCalculoComision::getLabel);

		NumberField txtValor = new NumberField("Valor");
		txtValor.setWidthFull();
		txtValor.setStep(0.01);

		ComboBox<TipoEventoComision> cmbEvento = new ComboBox<>("Evento");
		cmbEvento.setItems(TipoEventoComision.values());
		cmbEvento.setItemLabelGenerator(TipoEventoComision::getLabel);

		Checkbox chkDescuentos = new Checkbox("Incluir descuentos en la base");
		chkDescuentos.setValue(true);

		Checkbox chkDevoluciones = new Checkbox("Ajustar por devoluciones");
		chkDevoluciones.setValue(true);

		NumberField txtVentana = new NumberField("Ventana de ajuste (días)");
		txtVentana.setValue(30.0);
		txtVentana.setStep(1);

		Checkbox chkActivo = new Checkbox("Activo");
		chkActivo.setValue(true);

		List<TramoComision> tramosTemp = new ArrayList<>();
		if (existente != null && existente.getTramos() != null) {
			for (var t : existente.getTramos()) {
				var copia = new TramoComision();
				copia.setId(t.getId());
				copia.setDesde(t.getDesde());
				copia.setHasta(t.getHasta());
				copia.setPorcentaje(t.getPorcentaje());
				tramosTemp.add(copia);
			}
		}

		Grid<TramoComision> tramosGrid = new Grid<>(TramoComision.class, false);
		tramosGrid.addColumn(t -> String.format("$%.2f", t.getDesde())).setHeader("Desde").setWidth("120px");
		tramosGrid.addColumn(t -> t.getHasta() != null ? String.format("$%.2f", t.getHasta()) : "∞")
				.setHeader("Hasta").setWidth("120px");
		tramosGrid.addColumn(t -> String.format("%.2f%%", t.getPorcentaje())).setHeader("%").setWidth("80px");
		tramosGrid.addComponentColumn(t -> {
			Button btnEliminar = new Button(new Icon(VaadinIcon.TRASH));
			btnEliminar.addClassName("btn-eliminar");
			btnEliminar.addClickListener(_ -> {
				tramosTemp.remove(t);
				tramosGrid.setItems(tramosTemp);
			});
			return btnEliminar;
		}).setHeader("").setWidth("60px").setKey("acciones");
		tramosGrid.setItems(tramosTemp);
		tramosGrid.setHeight("200px");

		Span tramosLabel = new Span("Tramos (solo para cálculo escalonado)");
		tramosLabel.getStyle().set("font-weight", "bold");

		NumberField txtTramoDesde = new NumberField("Desde $");
		txtTramoDesde.setStep(0.01);
		txtTramoDesde.setWidth("150px");
		NumberField txtTramoHasta = new NumberField("Hasta $ (vacío = ∞)");
		txtTramoHasta.setStep(0.01);
		txtTramoHasta.setWidth("150px");
		NumberField txtTramoPct = new NumberField("%");
		txtTramoPct.setStep(0.01);
		txtTramoPct.setWidth("80px");

		Button btnAgregarTramo = new Button("Agregar Tramo", _ -> {
			if (txtTramoDesde.getValue() == null || txtTramoPct.getValue() == null
					|| txtTramoPct.getValue() <= 0) {
				Notification.show("Complete Desde y %", 2000, Notification.Position.MIDDLE);
				return;
			}
			var t = new TramoComision();
			t.setDesde(BigDecimal.valueOf(txtTramoDesde.getValue()));
			t.setHasta(txtTramoHasta.getValue() != null ? BigDecimal.valueOf(txtTramoHasta.getValue()) : null);
			t.setPorcentaje(BigDecimal.valueOf(txtTramoPct.getValue()));
			tramosTemp.add(t);
			tramosGrid.setItems(tramosTemp);
			txtTramoDesde.clear();
			txtTramoHasta.clear();
			txtTramoPct.clear();
		});
		btnAgregarTramo.addClassName("btn-nuevo");
		btnAgregarTramo.getStyle().set("padding", "4px 8px").set("font-size", "13px");

		HorizontalLayout tramoInputs = new HorizontalLayout(txtTramoDesde, txtTramoHasta, txtTramoPct, btnAgregarTramo);
		tramoInputs.setAlignItems(Alignment.BASELINE);
		tramoInputs.setSpacing(true);
		tramoInputs.setWidth("380px");

		VerticalLayout tramosSection = new VerticalLayout(tramosLabel, tramoInputs, tramosGrid);
		tramosSection.setSpacing(true);
		tramosSection.setPadding(false);

		cmbCalculo.addValueChangeListener(e -> {
			boolean escalonado = e.getValue() == TipoCalculoComision.ESCALONADO;
			txtValor.setVisible(!escalonado);
			tramosSection.setVisible(escalonado);
		});

		if (existente != null) {
			cmbVendedor.setValue(existente.getVendedor());
			txtNombre.setValue(existente.getNombre());
			cmbBase.setValue(existente.getTipoBaseComisionable());
			cmbCalculo.setValue(existente.getTipoCalculo());
			txtValor.setValue(existente.getValorCalculo().doubleValue());
			cmbEvento.setValue(existente.getTipoEvento());
			chkDescuentos.setValue(Boolean.TRUE.equals(existente.getIncluirDescuentos()));
			chkDevoluciones.setValue(Boolean.TRUE.equals(existente.getAjustarDevoluciones()));
			txtVentana.setValue(existente.getVentanaAjusteDias().doubleValue());
			chkActivo.setValue(Boolean.TRUE.equals(existente.getActivo()));

			boolean escalonado = existente.getTipoCalculo() == TipoCalculoComision.ESCALONADO;
			txtValor.setVisible(!escalonado);
			tramosSection.setVisible(escalonado);
		} else {
			tramosSection.setVisible(false);
		}

		FormLayout form = new FormLayout();
		form.add(cmbVendedor, txtNombre, cmbBase, cmbCalculo, txtValor, cmbEvento,
				chkDescuentos, chkDevoluciones, txtVentana, chkActivo);
		form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

		Button guardar = new Button("Guardar");
		guardar.addClassName("btn-grabar");
		guardar.addClickListener(_ -> {
			if (txtNombre.isEmpty()) {
				Notification.show("El nombre es obligatorio", 3000, Notification.Position.MIDDLE);
				return;
			}
			if (cmbBase.getValue() == null) {
				Notification.show("Seleccione una base comisionable", 3000, Notification.Position.MIDDLE);
				return;
			}
			if (cmbCalculo.getValue() == null) {
				Notification.show("Seleccione un tipo de cálculo", 3000, Notification.Position.MIDDLE);
				return;
			}

			boolean esEscalonado = cmbCalculo.getValue() == TipoCalculoComision.ESCALONADO;

			if (!esEscalonado && (txtValor.getValue() == null || txtValor.getValue() <= 0)) {
				Notification.show("Ingrese un valor válido", 3000, Notification.Position.MIDDLE);
				return;
			}

			if (esEscalonado && tramosTemp.isEmpty()) {
				Notification.show("Debe agregar al menos un tramo", 3000, Notification.Position.MIDDLE);
				return;
			}

			ReglaComision r = existente != null ? existente : new ReglaComision();
			r.setVendedor(cmbVendedor.getValue());
			r.setNombre(txtNombre.getValue());
			r.setTipoBaseComisionable(cmbBase.getValue());
			r.setTipoCalculo(cmbCalculo.getValue());
			r.setValorCalculo(esEscalonado && !tramosTemp.isEmpty()
					? tramosTemp.get(tramosTemp.size() - 1).getPorcentaje()
					: BigDecimal.valueOf(txtValor.getValue()));
			r.setTipoEvento(cmbEvento.getValue());
			r.setIncluirDescuentos(chkDescuentos.getValue());
			r.setAjustarDevoluciones(chkDevoluciones.getValue());
			r.setVentanaAjusteDias(txtVentana.getValue() != null ? txtVentana.getValue().intValue() : 30);
			r.setActivo(chkActivo.getValue());
			r.setTramos(new ArrayList<>(tramosTemp));

			reglaComisionUseCase.save(r);
			dialog.close();
			recargarDatos();
			Notification.show("Regla guardada", 3000, Notification.Position.MIDDLE);
		});

		Button cancelar = new Button("Cancelar", _ -> dialog.close());
		cancelar.addClassName("btn-volver");

		HorizontalLayout actions = new HorizontalLayout(guardar, cancelar);
		actions.setSpacing(true);

		VerticalLayout content = new VerticalLayout(form, tramosSection, actions);
		content.setPadding(false);
		dialog.add(content);
		dialog.open();
	}
}