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
import com.visus.central.domain.model.Comprobante;
import com.visus.central.domain.port.in.ComprobanteUseCase;
import com.visus.central.ui.component.AbstractForm;
import com.visus.central.ui.component.ComprobanteForm;
import com.visus.central.ui.component.CentralLayout;

@Route(value = "comprobantes", layout = CentralLayout.class)
@PageTitle("Gestión de Comprobantes")
public class ComprobanteView extends AbstractView<Comprobante> {
	
	private static final long serialVersionUID = 1L;

	private final ComprobanteUseCase service;

	public ComprobanteView(ComprobanteUseCase service) {
        super("Nuevo Comprobante", service);
        this.service = service;
	}
	
	@Override
    protected List<Grid.Column<Comprobante>> buildColumns(Grid<Comprobante> grid) {
        return List.of(
            grid.addColumn(Comprobante::getNombreLargo).setHeader("NOMBRE LARGO"),
            grid.addColumn(Comprobante::getNombreCorto).setHeader("NOMBRE CORTO"),
            grid.addColumn(Comprobante::getNumeroInicial).setHeader("NÚMERO INICIAL"),
            grid.addColumn(Comprobante::getSucursal).setHeader("SUCURSAL"),
            grid.addColumn(c -> c.getColumna() != null ? c.getColumna().toString() : "")
                .setHeader("COLUMNA")
        );
    }

    @Override
    protected AbstractForm<Comprobante> buildForm() {
        return new ComprobanteForm();
    }

    @Override
    protected void aplicarFiltro(String criterio) {
        if (criterio == null || criterio.isBlank()) {
            grid.setItems(service.findAll());
        } else {
            grid.setItems(service.findAll().stream()
                .filter(c -> c.getNombreLargo() != null && 
                       c.getNombreLargo().toLowerCase().contains(criterio.toLowerCase()))
                .collect(Collectors.toList()));
        }
    }
    
    private void mostrarConfirmacionEliminacion(Comprobante comprobante) {
        Dialog dialogo = new Dialog();
        dialogo.setHeaderTitle("Confirmar eliminación");

        Span mensaje = new Span("¿Está seguro de que desea eliminar el Comprobante \"" + 
            comprobante.getNombreLargo() + "\"?");
        mensaje.getStyle().set("font-weight", "500");

        Button aceptar = new Button("Eliminar", _ -> {
            showLoading("Eliminando...");
            try {
                service.deleteById(comprobante.getId());
                filtro.clear();
                actualizarLista();
                form.setVisible(false);
                Notification.show("Comprobante eliminado", 3000, Notification.Position.BOTTOM_CENTER)
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
        List<Comprobante> lista = service.findAll().stream()
            .filter(con -> con.getNombreLargo() != null && 
                   con.getNombreLargo().toLowerCase().contains(texto.toLowerCase().trim()))
            .collect(Collectors.toList());
        
        grid.setItems(lista);
    }
    
    @Override
    protected void eliminar(Comprobante model) {
        mostrarConfirmacionEliminacion(model);
    }

}
