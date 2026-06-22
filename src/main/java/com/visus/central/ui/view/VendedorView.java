package com.visus.central.ui.view;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.visus.central.domain.model.Vendedor;
import com.visus.central.domain.port.in.VendedorUseCase;
import com.visus.central.ui.component.AbstractForm;
import com.visus.central.ui.component.CentralLayout;
import com.visus.central.ui.component.VendedorForm;

@Route(value = "vendedores", layout = CentralLayout.class)
@PageTitle("Vendedores")
public class VendedorView extends AbstractView<Vendedor> {
	
	private static final long serialVersionUID = 1L;
	
	public VendedorView(VendedorUseCase vendedorUseCase) {
		super("Nuevo Vendedor", vendedorUseCase); // usa optimista por defecto
//        super("Nuevo Cliente", service, false); // desactiva optimista para data muy grande
		grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		grid.addClassName("grid-documentacion-dark");
    }
	
	@Override
    protected List<Grid.Column<Vendedor>> buildColumns(Grid<Vendedor> grid) {
        return List.of(
            grid.addColumn(Vendedor::getNombre).setHeader("NOMBRE"),
            grid.addColumn(Vendedor::getTelefono).setHeader("MÓVIL"),
            grid.addColumn(Vendedor::getTipoDocumento).setHeader("TIPO"),
            grid.addColumn(Vendedor::getNumero).setHeader("NÚMERO"),
            grid.addColumn(Vendedor::getSituacionFiscal).setHeader("SITUACIÓN FISCAL")
        );
    }

	@Override
    protected AbstractForm<Vendedor> buildForm() {
		return new VendedorForm(); // Usa la referencia guardada
    }

	@Override
	protected void aplicarFiltro(String criterio) {
	    if (criterio == null || criterio.isBlank()) {
	        grid.setItems(service.findAll());
	    } else {
	        grid.setItems(((VendedorUseCase) service).findByNombreContainingIgnoreCase(criterio));
	    }
	}
	
	@Override
	protected void eliminar(Vendedor model) {
	    Dialog dialogo = new Dialog();
	    dialogo.setHeaderTitle("Confirmar eliminación de vendedor");
	    Span mensaje = new Span("El vendedor \"" + model.getNombre() + "\" y sus domicilios se eliminarán. ¿Desea continuar?");
	    mensaje.getStyle().set("font-weight", "500");
	    
	    Button aceptar = new Button("Eliminar", _ -> {
	        // Implementar lógica de eliminación DIRECTAMENTE
	        showLoading("Eliminando...");
	        try {
	            service.deleteById(getId(model));
	            if (useOptimisticUpdate) {
	                eliminarOptimista(model);
	            } else {
	                actualizarGrilla();
	            }
	            form.setVisible(false);
	            showSuccess("Vendedor eliminado");
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
