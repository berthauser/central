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
import com.visus.central.domain.model.Alicuota;
import com.visus.central.domain.port.in.AlicuotaUseCase;
import com.visus.central.ui.component.AbstractForm;
import com.visus.central.ui.component.CentralLayout;
import com.visus.central.ui.component.AlicuotaForm;

@Route(value = "alicuotas", layout = CentralLayout.class)
@PageTitle("Gestión de Alícuotas")
public class AlicuotaView extends AbstractView<Alicuota> {

	private static final long serialVersionUID = 1L;

	public AlicuotaView(AlicuotaUseCase service) {
		super("Nueva Alícuota", service);
	}
	
	@Override
    protected List<Grid.Column<Alicuota>> buildColumns(Grid<Alicuota> grid) {
        return List.of(
            grid.addColumn(Alicuota::getDescripcion).setHeader("DESCRIPCIÓN"),
            grid.addColumn(c -> c.getGravamen() != null ? c.getGravamen().toString() : "")
                .setHeader("GRAVAMEN")
        );
    }
    
    // IMPLEMENTACIÓN CORRECTA - devuelve AbstractForm<Alicuota>
    @Override
    protected AbstractForm<Alicuota> buildForm() {
        return new AlicuotaForm();
    }
    
    @Override
    protected void aplicarFiltro(String criterio) {
        if (criterio == null || criterio.isBlank()) {
            grid.setItems(service.findAll());
        } else {
            grid.setItems(service.findAll().stream()
                .filter(c -> c.getDescripcion() != null && 
                       c.getDescripcion().toLowerCase().contains(criterio.toLowerCase()))
                .collect(Collectors.toList()));
        }
    }
    
    private void mostrarConfirmacionEliminacion(Alicuota alicuota) {
        Dialog dialogo = new Dialog();
        dialogo.setHeaderTitle("Confirmar eliminación");

        Span mensaje = new Span("¿Está seguro de que desea eliminar la Alícuota\"" + 
            alicuota.getDescripcion() + "\"?");
        mensaje.getStyle().set("font-weight", "500");

        Button aceptar = new Button("Eliminar", _ -> {
            showLoading("Eliminando...");
            try {
                service.deleteById(alicuota.getId());
                filtro.clear(); // Limpiar filtro después de eliminar
                actualizarLista();
                form.setVisible(false);
                Notification.show("Alícuota eliminada", 3000, Notification.Position.BOTTOM_CENTER)
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
        List<Alicuota> lista = service.findAll().stream()
            .filter(con -> con.getDescripcion() != null && 
                   con.getDescripcion().toLowerCase().contains(texto.toLowerCase().trim()))
            .collect(Collectors.toList());
        
        grid.setItems(lista);
    }
    
    @Override
    protected void eliminar(Alicuota model) {
        mostrarConfirmacionEliminacion(model);
    }
	
	
}
