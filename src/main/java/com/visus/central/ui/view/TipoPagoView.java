package com.visus.central.ui.view;

import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.visus.central.domain.model.TipoPago;
import com.visus.central.domain.port.in.TipoPagoUseCase;
import com.visus.central.ui.component.AbstractForm;
import com.visus.central.ui.component.CentralLayout;
import com.visus.central.ui.component.TipoPagoForm;

@Route(value = "tiposdepago", layout = CentralLayout.class)
@PageTitle("Gestión de Tipos de Pago")
public class TipoPagoView extends AbstractView<TipoPago> {

	private static final long serialVersionUID = 1L;

	private final TipoPagoUseCase service;

	protected TipoPagoView(TipoPagoUseCase service) {
		super("Nuevo Tipo de Pago", service);
		this.service = service;
	}

	@Override
	protected AbstractForm<TipoPago> buildForm() {
		return new TipoPagoForm(); // Simple y sin dependencias
	}

	@Override
	protected List<Grid.Column<TipoPago>> buildColumns(Grid<TipoPago> grid) {

		// Establecer anchos iniciales más razonables
		grid.getColumns().forEach(col -> col.setAutoWidth(true));

		List<Grid.Column<TipoPago>> columns = List.of(

				grid.addColumn(f -> f.getDescripcion() != null ? f.getDescripcion().toString() : "")
						.setHeader("DESCRIPCIÓN").setResizable(true),

				grid.addColumn(f -> f.getRequiere_coeficiente() != null ? f.getRequiere_coeficiente().toString() : "")
						.setHeader("REQUIERE COEFICIENTE").setResizable(true),

				grid.addColumn(f -> f.getEs_pronto_pago() != null ? f.getEs_pronto_pago().toString() : "")
						.setHeader("Dto. PRONTO PAGO").setResizable(true),

				grid.addColumn(f -> f.getDto_pronto_pago() != null ? f.getDto_pronto_pago().toString() : "")
						.setHeader("Dto. %").setResizable(true),

				grid.addColumn(f -> f.getGenera_deuda() != null ? f.getGenera_deuda().toString() : "")
						.setHeader("GENERA DEUDA").setResizable(true),

				grid.addColumn(f -> f.getAfecta_caja() != null ? f.getAfecta_caja().toString() : "")
						.setHeader("AFECTA A CAJA").setResizable(true));

		return columns;
	}

	@Override
	protected void aplicarFiltro(String criterio) {
		if (criterio == null || criterio.isBlank()) {
			grid.setItems(service.findAll());
		} else {
			grid.setItems(service.findAll().stream()
					.filter(f -> f.getDescripcion() != null
							&& f.getDescripcion().toString().toLowerCase().contains(criterio.toLowerCase()))
					.collect(Collectors.toList()));
		}
	}

	private void mostrarConfirmacionEliminacion(TipoPago tipoPago) {
		Dialog dialogo = new Dialog();
		dialogo.setHeaderTitle("Confirmar eliminación");

		Span mensaje = new Span(
				"¿Está seguro de que desea eliminar el Tipo de Pago \"" + tipoPago.getDescripcion() + "\"?");
		mensaje.getStyle().set("font-weight", "500");

		Button aceptar = new Button("Eliminar", _ -> {
			showLoading("Eliminando...");
			try {
				service.deleteById(tipoPago.getId());
				filtro.clear();
				actualizarLista();
				form.setVisible(false);
				Notification.show("Tipo de Pago eliminado", 3000, Notification.Position.BOTTOM_CENTER)
						.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			} finally {
				hideLoading();
			}
			dialogo.close();
		});

		Button cancelar = new Button("Cancelar", _ -> dialogo.close());

		aceptar.addThemeVariants(ButtonVariant.LUMO_ERROR);
		cancelar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		dialogo.getFooter().add(aceptar, cancelar);
		dialogo.add(mensaje);
		dialogo.open();
	}

	// Actualiza la grilla
	private void actualizarLista() {
		String texto = filtro.getValue();

		// Si el texto está vacío o es solo espacios, mostrar todos
		if (texto == null || texto.trim().isEmpty()) {
			grid.setItems(service.findAll());
			return;
		}
	}

	@Override
	protected void eliminar(TipoPago model) {
		mostrarConfirmacionEliminacion(model);
	}

}
