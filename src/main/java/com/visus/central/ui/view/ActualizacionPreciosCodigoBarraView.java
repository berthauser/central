package com.visus.central.ui.view;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.visus.central.domain.port.in.ActualizarPreciosUseCase;
import com.visus.central.domain.port.out.ArticuloRepository;
import com.visus.central.ui.component.ActualizacionPreciosCodigoBarraForm;
import com.visus.central.ui.component.CentralLayout;

import jakarta.annotation.security.RolesAllowed;

@PageTitle("Actualización de Precios por Código de Barras")
@Route(value = "actualizacion-precios-codigo-barra", layout = CentralLayout.class)
@RolesAllowed("ADMIN")
public class ActualizacionPreciosCodigoBarraView extends AbstractProcessView {
	
	private static final long serialVersionUID = 1L;

	private final ActualizarPreciosUseCase actualizarPreciosUseCase;
	private final ArticuloRepository articuloRepository;

	public ActualizacionPreciosCodigoBarraView(
			ActualizarPreciosUseCase actualizarPreciosUseCase,
			ArticuloRepository articuloRepository) {

		this.actualizarPreciosUseCase = actualizarPreciosUseCase;
		this.articuloRepository = articuloRepository;

		super(); // Llama al constructor de AbstractProcessView
	}
	
	@Override
    protected void inicializarProceso() {
        // Establecer el nombre del proceso
        setNombreProceso("Actualización de Precios por Código de Barras");
        
        // Crear el formulario específico para este proceso
        procesoForm = new ActualizacionPreciosCodigoBarraForm(
            actualizarPreciosUseCase,
            articuloRepository
        );
        
        // Agregar el formulario al contenedor
        formularioContainer.add(procesoForm);
        
        // Configurar eventos del formulario
        configurarEventosFormulario();
    }
    
    @Override
    protected void configurarEventosFormulario() {
        if (procesoForm == null) return;
        
        procesoForm.setOnAccept(() -> onProcesoAceptado());
        procesoForm.setOnCancel(() -> onProcesoCancelado());
        procesoForm.setOnBack(() -> onVolver());
    }
    
    @Override
    protected void onProcesoAceptado() {
        Notification notification = Notification.show(
            "Datos validados correctamente. Revise el resumen y confirme para aplicar los cambios.",
            4000,
            Notification.Position.BOTTOM_START
        );
        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
    }
    
    @Override
    protected void onProcesoCancelado() {
        super.onProcesoCancelado();
    	Notification.show("Proceso cancelado", 3000, Notification.Position.MIDDLE);
    }
    
    @Override
    protected void onVolver() {
        Notification.show("Volviendo...", 2000, Notification.Position.MIDDLE);
        super.onVolver();
    }
    
	
}
