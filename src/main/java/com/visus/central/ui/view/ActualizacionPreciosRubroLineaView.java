package com.visus.central.ui.view;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.visus.central.domain.port.in.ActualizarPreciosUseCase;
import com.visus.central.domain.port.in.LineaUseCase;
import com.visus.central.domain.port.in.RubroUseCase;
import com.visus.central.domain.port.out.ArticuloRepository;
import com.visus.central.ui.component.ActualizacionPreciosRubroLineaForm;
import com.visus.central.ui.component.CentralLayout;

import jakarta.annotation.security.RolesAllowed;

@PageTitle("Actualización de Precios por Rubro y Línea")
@Route(value = "actualizacion-precios", layout = CentralLayout.class)
@RolesAllowed("ADMIN") // Ajusta según tus roles
public class ActualizacionPreciosRubroLineaView extends AbstractProcessView {

	private static final long serialVersionUID = 1L;
	
	private RubroUseCase obtenerRubrosUseCase;

	private LineaUseCase obtenerLineasUseCase;

	private ActualizarPreciosUseCase actualizarPreciosUseCase;

	private ArticuloRepository articuloRepository;

	 public ActualizacionPreciosRubroLineaView(
	            RubroUseCase obtenerRubrosUseCase,
	            LineaUseCase obtenerLineasUseCase,
	            ActualizarPreciosUseCase actualizarPreciosUseCase,
	            ArticuloRepository articuloRepository) {
		 
	        this.obtenerRubrosUseCase = obtenerRubrosUseCase;
	        this.obtenerLineasUseCase = obtenerLineasUseCase;
	        this.actualizarPreciosUseCase = actualizarPreciosUseCase;
	        this.articuloRepository = articuloRepository;
	        
	        super(); // Llama al constructor de AbstractProcessView
	    }

	@Override 
	protected void inicializarProceso() {

		// Establecer el nombre del proceso
		setNombreProceso("Actualización de Precios por Rubro y Línea");

		// Crear el formulario específico para este proceso
		procesoForm = new ActualizacionPreciosRubroLineaForm(
				obtenerRubrosUseCase, 
				obtenerLineasUseCase,
				actualizarPreciosUseCase,
				articuloRepository);

		// Configurar el formulario con la lógica de negocio adicional
		configurarFormularioEspecifico((ActualizacionPreciosRubroLineaForm) procesoForm);

		// Agregar el formulario al contenedor
		formularioContainer.add(procesoForm);
	}

	private void configurarFormularioEspecifico(ActualizacionPreciosRubroLineaForm form) {
		// Aquí podemos configurar callbacks adicionales si es necesario
		// Por ejemplo, para manejar la lógica de confirmación y ejecución

		form.setOnAccept(() -> {
			// Este callback se llama cuando se presiona Aceptar en el formulario
			// Pero la lógica de validación y ejecución está dentro del formulario
			// Sin embargo, podríamos querer hacer algo adicional en la vista
			onProcesoAceptadoConFormulario(form);
		});
	}

	@Override
	protected void onProcesoAceptado() {
		Notification notification = Notification.show(
				"✓ Datos validados correctamente. Revise el resumen y confirme para aplicar los cambios.",
				4000,
				Notification.Position.BOTTOM_START
				);
		notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
	}

	private void onProcesoAceptadoConFormulario(ActualizacionPreciosRubroLineaForm form) {
		// Aquí podríamos, por ejemplo, mostrar un diálogo de confirmación
		// antes de ejecutar el proceso, si es necesario.

		// Pero en este caso, el formulario ya tiene su propia validación y lógica.
		// Podemos optar por manejar la ejecución del proceso aquí o dejar que el formulario lo haga.

		// En este ejemplo, vamos a dejar que el formulario maneje la lógica,
		// pero podríamos extraer los datos del formulario y ejecutar el proceso desde aquí.

		// Por simplicidad, asumimos que el formulario ya maneja todo.
		// Solo notificamos a la vista que el proceso fue aceptado.
		showSuccess("Proceso de actualización de precios por Rubro y Líneas iniciado.");
	}

	@Override
	protected void onProcesoCancelado() {
		// Podemos personalizar el comportamiento al cancelar
		super.onProcesoCancelado();
		showInfo("Proceso cancelado.");
	}

	@Override
	protected void onVolver() {
		// Personalizar la acción de volver si es necesario
		super.onVolver();
	}

	// Métodos auxiliares para notificaciones (podrían moverse a una clase base)
	private void showSuccess(String message) {
		Notification.show(message, 3000, Notification.Position.MIDDLE
				);
	}

	private void showInfo(String message) {
		Notification.show(message, 3000, Notification.Position.MIDDLE
				);
	}

}
