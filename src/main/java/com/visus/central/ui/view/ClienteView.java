package com.visus.central.ui.view;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.visus.central.domain.model.Cliente;
import com.visus.central.domain.port.in.ClienteUseCase;
import com.visus.central.ui.component.AbstractForm;
import com.visus.central.ui.component.CentralLayout;
import com.visus.central.ui.component.ClienteForm;

@Route(value = "clientes", layout = CentralLayout.class)
@PageTitle("Gestión de Clientes")
public class ClienteView extends AbstractView<Cliente> {
	
	private static final long serialVersionUID = 1L;

	public ClienteView(ClienteUseCase service) {
        super("Nuevo Cliente", service);
	}

    @Override
    protected List<Grid.Column<Cliente>> buildColumns(Grid<Cliente> grid) {
        return List.of(
        	grid.addColumn(Cliente::getNombreCliente)
            	.setHeader("NOMBRE REAL")
            	.setAutoWidth(true)
            	.setResizable(true),
        	grid.addColumn(Cliente::getNombreFantasia)
            	.setHeader("NOMBRE FANTASÍA")
            	.setAutoWidth(true)
            	.setResizable(true),
            grid.addColumn(Cliente::getTelefonoMovil)	
            	.setHeader("MÓVIL")
            	.setAutoWidth(true)
            	.setResizable(true),
            grid.addColumn(Cliente::getDocumento)
            	.setHeader("TIPO")
            	.setAutoWidth(true)
            	.setResizable(true),
            grid.addColumn(Cliente::getNumero)
            	.setHeader("NÚMERO")
            	.setAutoWidth(true)
            	.setResizable(true),
            grid.addColumn(Cliente::getSaldoCtaCte)
            	.setHeader("SALDO en CUENTA")
            	.setAutoWidth(true)
            	.setResizable(true),
            grid.addColumn(Cliente::getEstado)
                .setHeader("ESTADO")
                .setAutoWidth(true)
            	.setResizable(true)
        );
    }
    
    @Override
    protected AbstractForm<Cliente> buildForm() {
        ClienteForm form = new ClienteForm();
    	return form;
    }

    @Override
    protected void aplicarFiltro(String criterio) {
        if (criterio == null || criterio.isBlank()) {
            grid.setItems(service.findAll());
        } else {
            grid.setItems(((ClienteUseCase) service).findByNombreContainingIgnoreCase(criterio));
        }
    }
    
    @Override
    protected void eliminar(Cliente model) {
        Dialog dialogo = new Dialog();
        dialogo.setHeaderTitle("Confirmar eliminación de cliente");
        Span mensaje = new Span("El cliente \"" + model.getNombreCliente() + "\" y sus datos relacionados se eliminarán. ¿Desea continuar?");
        mensaje.getStyle().set("font-weight", "500");
        
        Button aceptar = new Button("Eliminar", _ -> {
            showLoading("Eliminando...");
            try {
                service.deleteById(getId(model));
                if (useOptimisticUpdate) {
                    eliminarOptimista(model);
                } else {
                    actualizarGrilla();
                }
                form.setVisible(false);
                showSuccess("Cliente eliminado");
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
        
}
