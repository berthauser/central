package com.visus.central.ui.view;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.visus.central.domain.model.Proveedor;
import com.visus.central.domain.port.in.ProveedorUseCase;
import com.visus.central.ui.component.AbstractForm;
import com.visus.central.ui.component.CentralLayout;
import com.visus.central.ui.component.ProveedorForm;

@Route(value = "proveedores", layout = CentralLayout.class)
@PageTitle("Gestión de Proveedores")
public class ProveedorView extends AbstractView<Proveedor> {
	
	private static final long serialVersionUID = 1L;
    private final ProveedorUseCase service;

	public ProveedorView(ProveedorUseCase service) {
        super("Nuevo Proveedor", service);
        this.service = service; // Asignar referencia al tipo específico
	}
	
	@Override
    protected List<Grid.Column<Proveedor>> buildColumns(Grid<Proveedor> grid) {
		
		List<Grid.Column<Proveedor>> columns = List.of(
            grid.addColumn(Proveedor::getNombreFantasia)
            .setHeader("NOMBRE FANTASÍA")
            .setResizable(true)
            .setSortable(true),
            grid.addColumn(Proveedor::getNombreReal)
            .setHeader("NOMBRE REAL")
            .setResizable(true)
            .setSortable(true),
            grid.addColumn(p -> p.getDocumento() != null ? p.getDocumento().getLabel() : "")
            .setHeader("DOCUMENTO")
            .setResizable(true),
            grid.addColumn(Proveedor::getNumero)
            .setHeader("NÚMERO")
            .setResizable(true),
            grid.addColumn(Proveedor::getTelefonoUno)
            .setHeader("TELÉFONO 1")
            .setResizable(true),
            grid.addColumn(Proveedor::getTelefonoDos)
            .setHeader("TELÉFONO 2")
            .setResizable(true),
            grid.addColumn(Proveedor::getTelefonoTres)
            .setHeader("TELÉFONO 3")
            .setResizable(true),
            grid.addColumn(p -> p.getSituacionFiscal() != null ? p.getSituacionFiscal().getLabel() : "")
                .setHeader("SITUACIÓN FISCAL")
                .setResizable(true),
            grid.addColumn(p -> p.getBanco() != null ? p.getBanco().getNombre() : "") // MOSTRAR NOMBRE DEL BANCO
                .setHeader("BANCO")
                .setResizable(true),
            grid.addColumn(Proveedor::getEmail).setHeader("EMAIL"),
            grid.addColumn(p -> p.getEstado() != null ? p.getEstado().getLabel() : "")
                .setHeader("ESTADO")
                .setResizable(true)
        );
		
		grid.setColumnReorderingAllowed(true);
		// Establecer anchos iniciales más razonables
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        
        return columns;
    }
	
	@Override
    protected AbstractForm<Proveedor> buildForm() {
        ProveedorForm form = new ProveedorForm();
        return form;
    }

    @Override
    protected void aplicarFiltro(String criterio) {
    	if (criterio == null || criterio.isBlank()) {
            grid.setItems(service.findAll()); // 👈 Usar service específico
        } else {
            grid.setItems(service.findByNombreContainingIgnoreCase(criterio)); // 👈 Usar service específico
        }
    }
    
    private void mostrarConfirmacionEliminacion(Proveedor proveedor) {
        Dialog dialogo = new Dialog();
        dialogo.setHeaderTitle("Confirmar eliminación de proveedor");
        Span mensaje = new Span("El proveedor \"" + proveedor.getNombreFantasia() + "\" se eliminará. ¿Desea continuar?");
        mensaje.getStyle().set("font-weight", "500");
        
        Button aceptar = new Button("Eliminar", _ -> {
            showLoading("Eliminando...");
            try {
                service.deleteById(proveedor.getId());
                if (useOptimisticUpdate) {
                    eliminarOptimista(proveedor);
                } else {
                    actualizarGrilla();
                }
                form.setVisible(false);
                showSuccess("Proveedor eliminado");
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
    
    @Override
    protected void eliminar(Proveedor model) {
        mostrarConfirmacionEliminacion(model);
    }

}
