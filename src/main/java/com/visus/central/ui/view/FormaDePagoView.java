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
import com.visus.central.domain.model.FormaDePago;
import com.visus.central.domain.port.in.FormaDePagoUseCase;
import com.visus.central.ui.component.AbstractForm;
import com.visus.central.ui.component.FormaDePagoForm;
import com.visus.central.ui.component.CentralLayout;

@Route(value = "formasdepago", layout = CentralLayout.class)
@PageTitle("Gestión de Formas de Pago")
public class FormaDePagoView extends AbstractView<FormaDePago> {

	private static final long serialVersionUID = 1L;
	
	private final FormaDePagoUseCase service;

	public FormaDePagoView(FormaDePagoUseCase service) {
		super("Nueva Forma de Pago", service);
		this.service = service;
	}

    @Override
    protected List<Grid.Column<FormaDePago>> buildColumns(Grid<FormaDePago> grid) {
    	List<Grid.Column<FormaDePago>> columns = List.of(
                grid.addColumn(f -> f.getModalidad() != null ? f.getModalidad().toString() : "")
                    .setHeader("MODALIDAD")
                    .setResizable(true),
                grid.addColumn(f -> f.getCoeficiente() != null ? f.getCoeficiente().getDescripcion() : "")
                    .setHeader("DESCRIPCIÓN COEFICIENTE")
                    .setResizable(true),
                grid.addColumn(f -> f.getCoeficiente() != null ? f.getCoeficiente().getCoeficiente().toString() : "")
                    .setHeader("COEFICIENTE")
                    .setResizable(true),
                grid.addColumn(f -> f.getCoeficiente() != null ? f.getCoeficiente().getCuotas().toString() : "")
                    .setHeader("CUOTAS")
                    .setResizable(true),
                grid.addColumn(f -> f.getEsDtoProntoPago() != null ? f.getEsDtoProntoPago().toString() : "")
                    .setHeader("Dto. PRONTO PAGO")
                    .setResizable(true),
                grid.addColumn(f -> f.getDtoProntoPago() != null ? f.getDtoProntoPago().toString() : "")
                    .setHeader("Dto. %")
                    .setResizable(true),
                grid.addColumn(f -> f.getEsMesesCompletos() != null ? f.getEsMesesCompletos().toString() : "")
                    .setHeader("MESES COMPLETOS")
                    .setResizable(true)
            );
    	
    	// Establecer anchos iniciales más razonables
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        
        return columns;
    }
    
    @Override
    protected AbstractForm<FormaDePago> buildForm() {
    	FormaDePagoForm form = new FormaDePagoForm();
        return form; // Simple y sin dependencias
    }

    @Override
    protected void aplicarFiltro(String criterio) {
        if (criterio == null || criterio.isBlank()) {
            grid.setItems(service.findAll());
        } else {
            grid.setItems(service.findAll().stream()
                .filter(f -> f.getModalidad() != null && 
                       f.getModalidad().toString().toLowerCase().contains(criterio.toLowerCase()))
                .collect(Collectors.toList()));
        }
    }
    
    private void mostrarConfirmacionEliminacion(FormaDePago formaDePago) {
        Dialog dialogo = new Dialog();
        dialogo.setHeaderTitle("Confirmar eliminación");

        Span mensaje = new Span("¿Está seguro de que desea eliminar la Forma de Pago \"" + 
            formaDePago.getModalidad() + "\"?");
        mensaje.getStyle().set("font-weight", "500");

        Button aceptar = new Button("Eliminar", _ -> {
            showLoading("Eliminando...");
            try {
                service.deleteById(formaDePago.getId());
                filtro.clear();
                actualizarLista();
                form.setVisible(false);
                Notification.show("Forma de Pago eliminada", 3000, Notification.Position.BOTTOM_CENTER)
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
    protected void eliminar(FormaDePago model) {
        mostrarConfirmacionEliminacion(model);
    }
	
}
