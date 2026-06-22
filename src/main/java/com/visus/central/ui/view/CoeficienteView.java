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
import com.visus.central.domain.model.Coeficiente;
import com.visus.central.domain.model.TipoPago;
import com.visus.central.domain.port.in.CoeficienteUseCase;
import com.visus.central.domain.port.out.TipoPagoRepository;
import com.visus.central.ui.component.AbstractForm;
import com.visus.central.ui.component.CentralLayout;
import com.visus.central.ui.component.CoeficienteForm;

@Route(value = "coeficientes", layout = CentralLayout.class)
@PageTitle("Gestión de Coeficientes")
public class CoeficienteView extends AbstractView<Coeficiente> {

	private static final long serialVersionUID = 1L;

	private final TipoPagoRepository tipoPagoRepository; // NUEVO

	public CoeficienteView(CoeficienteUseCase service, TipoPagoRepository tipoPagoRepository) {
		this.tipoPagoRepository = tipoPagoRepository;
		super("Nuevo Coeficiente", service);
	}

	@Override
	protected List<Grid.Column<Coeficiente>> buildColumns(Grid<Coeficiente> grid) {
		return List.of(grid.addColumn(Coeficiente::getDescripcion).setHeader("DESCRIPCIÓN"),
				grid.addColumn(c -> c.getTipo_pago() != null ? c.getTipo_pago().getDescripcion() : "")
						.setHeader("TIPO DE PAGO"), // NUEVA COLUMNA
				grid.addColumn(c -> c.getCoeficiente() != null ? c.getCoeficiente().toString() : "")
						.setHeader("COEFICIENTE"),
				grid.addColumn(c -> c.getCuotas() != null ? c.getCuotas().toString() : "").setHeader("CUOTAS"));
	}

	@Override
	protected AbstractForm<Coeficiente> buildForm() {
		CoeficienteForm form = new CoeficienteForm();

		// Cargar los tipos de pago que requieren coeficiente
		List<TipoPago> tiposPago = tipoPagoRepository.findByRequiereCoeficienteTrue();
		form.setTiposPago(tiposPago);

		return form;
	}

	@Override
	protected void aplicarFiltro(String criterio) {
		if (criterio == null || criterio.isBlank()) {
			grid.setItems(service.findAll());
		} else {
			grid.setItems(service.findAll().stream()
					.filter(c -> c.getDescripcion() != null
							&& c.getDescripcion().toLowerCase().contains(criterio.toLowerCase()))
					.collect(Collectors.toList()));
		}
	}

	private void mostrarConfirmacionEliminacion(Coeficiente coeficiente) {
		Dialog dialogo = new Dialog();
		dialogo.setHeaderTitle("Confirmar eliminación");

		Span mensaje = new Span(
				"¿Está seguro de que desea eliminar el Coeficiente \"" + coeficiente.getDescripcion() + "\"?");
		mensaje.getStyle().set("font-weight", "500");

		Button aceptar = new Button("Eliminar", _ -> {
			showLoading("Eliminando...");
			try {
				service.deleteById(coeficiente.getId());
				filtro.clear(); // Limpiar filtro después de eliminar
				actualizarLista();
				form.setVisible(false);
				Notification.show("Coeficiente eliminado", 3000, Notification.Position.BOTTOM_CENTER)
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

		// Si hay texto, filtrar
		List<Coeficiente> lista = service.findAll().stream()
				.filter(con -> con.getDescripcion() != null
						&& con.getDescripcion().toLowerCase().contains(texto.toLowerCase().trim()))
				.collect(Collectors.toList());

		grid.setItems(lista);
	}

	@Override
	protected void eliminar(Coeficiente model) {
		mostrarConfirmacionEliminacion(model);
	}

}
