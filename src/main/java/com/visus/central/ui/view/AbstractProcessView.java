package com.visus.central.ui.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.visus.central.ui.component.AbstractProcessForm;

public abstract class AbstractProcessView extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	
	// Label para el nombre del proceso
    protected final Span nombreProcesoLabel = new Span();
    
    // Contenedor para el formulario del proceso
    protected final VerticalLayout formularioContainer = new VerticalLayout();
    
    // Botón de inicio
    protected Button inicio;
    
    // Referencia al formulario específico del proceso
    protected AbstractProcessForm procesoForm;
    
    public AbstractProcessView() {
        // Configuración básica del layout
        setPadding(true);
        setSpacing(false);
        setSizeFull();
        
        // Configurar el label del proceso
        nombreProcesoLabel.getStyle()
            .set("font-size", "1.2em")
            .set("font-weight", "bold")
            .set("color", "var(--lumo-primary-text-color)")
            .set("margin-bottom", "1em");
        
        // Configurar contenedor del formulario
        formularioContainer.setPadding(false);
        formularioContainer.setSpacing(true);
        formularioContainer.setWidthFull();
        
        // Crear header
        HorizontalLayout header = crearHeader();
        
        // Agregar componentes al layout principal
        add(header);
        add(formularioContainer);
        
        // Inicializar el formulario del proceso
        inicializarProceso();
        
     // Configurar eventos del formulario
        if (procesoForm != null) {
            configurarEventosFormulario();
        }
    }
    
    /**
     * Crea el header con el nombre del proceso y el botón de inicio
     */
    protected HorizontalLayout crearHeader() {
        inicio = new Button("Inicio", new Icon(VaadinIcon.HOME));
        inicio.addClassName("btn-volver-home");
        inicio.addClickListener(_ -> UI.getCurrent().navigate(HomeView.class));
        
        HorizontalLayout header = new HorizontalLayout(nombreProcesoLabel, inicio);
        header.addClassName("header-base");
        header.setWidthFull();
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        header.setAlignItems(Alignment.CENTER);
        
        return header;
    }
    
    /**
     * Establece el nombre del proceso que se mostrará en el header
     */
    protected void setNombreProceso(String nombre) {
        nombreProcesoLabel.setText(nombre);
    }
    
    /**
     * Método abstracto para inicializar el proceso específico
     * Debe crear el formulario y agregarlo al contenedor
     */
    protected abstract void inicializarProceso();
    
    /**
     * Configura los eventos del formulario del proceso
     */
    protected void configurarEventosFormulario() {
        // Configurar callback para el botón Aceptar
        procesoForm.setOnAccept(() -> {
            // Lógica después de aceptar el proceso
            onProcesoAceptado();
        });
        
        // Configurar callback para el botón Cancelar
        procesoForm.setOnCancel(() -> {
            // Lógica después de cancelar el proceso
            onProcesoCancelado();
        });
        
        // Configurar callback para el botón Volver
        procesoForm.setOnBack(() -> {
            // Navegar a la vista anterior o inicio
            onVolver();
        });
    }
    
    /**
     * Método llamado cuando se acepta el proceso
     * Puede ser sobrescrito por las subclases
     */
    protected void onProcesoAceptado() {
    	// Lógica por defecto: mostrar mensaje de éxito
    	// CAMBIO: Mostrar mensaje informativo, no de éxito
    	Notification notification = Notification.show(
    			"Validación exitosa. Por favor, revise los datos y confirme la operación.",
    			4000,
    			Position.BOTTOM_START
    			);
    	notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
    }
    
    /**
     * Método llamado cuando se cancela el proceso
     * Puede ser sobrescrito por las subclases
     */
    protected void onProcesoCancelado() {
        // Lógica por defecto: limpiar formulario
        procesoForm.limpiarFormulario();
    }
    
    /**
     * Método llamado cuando se presiona Volver
     * Puede ser sobrescrito por las subclases
     */
    protected void onVolver() {
        // Navegar al inicio por defecto
        UI.getCurrent().navigate(HomeView.class);
    }
    
    /**
     * Obtiene el formulario del proceso
     */
    protected AbstractProcessForm getProcesoForm() {
        return procesoForm;
    }
	
	
}
