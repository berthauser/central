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
import com.visus.central.domain.model.Concepto;
import com.visus.central.domain.port.in.ConceptoUseCase;
import com.visus.central.ui.component.AbstractForm;
import com.visus.central.ui.component.CentralLayout;
import com.visus.central.ui.component.ConceptoForm;

@Route(value = "conceptos", layout = CentralLayout.class)
@PageTitle("Gestión de Conceptos")
public class ConceptoView extends AbstractView<Concepto> {
	
	private static final long serialVersionUID = 1L;

	public ConceptoView(ConceptoUseCase service) {
        super("Nuevo Concepto", service);
	}
    
    private void mostrarConfirmacionEliminacion(Concepto concepto) {
        Dialog dialogo = new Dialog();
        dialogo.setHeaderTitle("Confirmar eliminación");

        Span mensaje = new Span("¿Está seguro de que desea eliminar el Concepto \"" + 
            concepto.getDescripcion() + "\"?");
        mensaje.getStyle().set("font-weight", "500");

        Button aceptar = new Button("Eliminar", _ -> {
            // Implementar lógica de eliminación DIRECTAMENTE (sin super.eliminar)
            showLoading("Eliminando...");
            try {
                service.deleteById(concepto.getId());
             // 👇 LIMPIAR EL FILTRO ANTES DE ACTUALIZAR
                filtro.clear();
                actualizarLista();
                form.setVisible(false);
                // TU NOTIFICACIÓN PERSONALIZADA
                Notification.show("Concepto eliminado", 3000, Notification.Position.BOTTOM_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } finally {
                hideLoading();
            }
            dialogo.close();
        });
        
        Button cancelar = new Button("Cancelar", _ -> dialogo.close());
//
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
        List<Concepto> lista = service.findAll().stream()
            .filter(con -> con.getDescripcion() != null && 
                   con.getDescripcion().toLowerCase().contains(texto.toLowerCase().trim()))
            .collect(Collectors.toList());
        
        grid.setItems(lista);
    }
    
    @Override
    protected void aplicarFiltro(String criterio) {
        if (criterio == null || criterio.isBlank()) {
            grid.setItems(service.findAll());
        } else {
            grid.setItems(service.findAll().stream()
                .filter(con -> con.getDescripcion().toLowerCase().contains(criterio.toLowerCase()))
                .collect(Collectors.toList()));
        }
    }
    
    // SOBREESCRIBE EL MÉTODO DE GUARDADO PARA PERSONALIZAR
    @Override
    protected void guardar(Concepto model) {
        super.guardar(model); // Llama al comportamiento base
        // La notificación ya está en AbstractView, no necesitas otra
    }
    
    // IMPLEMENTACIÓN CORRECTA - usa Concepto, no T
    @Override
    protected List<Grid.Column<Concepto>> buildColumns(Grid<Concepto> grid) {
        return List.of(
            grid.addColumn(Concepto::getDescripcion).setHeader("DESCRIPCIÓN")
        );
    }

    // SOBREESCRIBE EL MÉTODO DE ELIMINACIÓN PARA USAR TU DIÁLOGO
    @Override
    protected void eliminar(Concepto model) {
        mostrarConfirmacionEliminacion(model);
    }
    
    // IMPLEMENTACIÓN CORRECTA - devuelve AbstractForm<Concepto>
    @Override
    protected AbstractForm<Concepto> buildForm() {
        return new ConceptoForm();
    }
    
}
