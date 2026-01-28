package com.visus.central.ui.component;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.shared.Registration;

public abstract class AbstractProcessForm extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	
	// Botones personalizados para procesos
    protected final Button aceptar = new Button("Aceptar");
    protected final Button cancelar = new Button("Cancelar");
    protected final Button volver = new Button("Volver", new Icon(VaadinIcon.BACKWARDS));
    
    // Callbacks funcionales
    private Runnable onAccept;
    private Runnable onCancel;
    private Runnable onBack;
    
    // Label para el título del proceso
    protected final H1 tituloLabel = new H1();
    
    // Contenedor para componentes específicos del proceso
    protected final VerticalLayout contenidoLayout = new VerticalLayout();
    
    public AbstractProcessForm() {
        // Configurar botones
        aceptar.addClassName("btn-crear");
        cancelar.addClassName("btn-eliminar");
        volver.addClassName("btn-volver");
        
        // Estilos para el título
        tituloLabel.getStyle()
            .set("color", "var(--lumo-primary-text-color)")
            .set("margin-bottom", "1.5em");
        
        // Configurar layout de contenido
        contenidoLayout.setPadding(false);
        contenidoLayout.setSpacing(true);
        contenidoLayout.setWidthFull();
        
        // Eventos de botones
        aceptar.addClickListener(_ -> {
            if (validarFormulario()) {
                ejecutarProceso();
                if (onAccept != null) onAccept.run();
                fireEvent(new AcceptEvent(this));
            }
        });
        
        cancelar.addClickListener(_ -> {
            limpiarFormulario();
            if (onCancel != null) onCancel.run();
            fireEvent(new CancelEvent(this));
        });
        
        volver.addClickListener(_ -> {
            if (onBack != null) onBack.run();
            fireEvent(new BackEvent(this));
        });
        
        // Configurar layout principal
        setPadding(true);
        setSpacing(true);
        setWidthFull();
        
        // Agregar componentes en orden
        add(tituloLabel);
        add(contenidoLayout);
        add(crearBotonera());
        
        // Configurar contenido específico del proceso
        configurarContenido();
    }
    
    /**
     * Método para establecer el título del proceso
     */
    public void setTitulo(String titulo) {
        tituloLabel.setText(titulo);
    }
    
    /**
     * Método para obtener el layout de contenido donde se agregarán
     * los componentes específicos del proceso
     */
    public VerticalLayout getContenidoLayout() {
        return contenidoLayout;
    }
    
    /**
     * Método abstracto que las subclases deben implementar
     * para configurar sus componentes específicos
     */
    protected abstract void configurarContenido();
    
    /**
     * Método para validar el formulario antes de ejecutar el proceso
     * Retorna true si la validación es exitosa
     */
    protected abstract boolean validarFormulario();
    
    /**
     * Método que ejecuta la lógica principal del proceso
     */
    protected abstract void ejecutarProceso();
    
    /**
     * Método para limpiar el formulario
     */
    public abstract void limpiarFormulario();
    
    /**
     * Crea la botonera con los botones de acción
     */
    protected HorizontalLayout crearBotonera() {
    	
    	// Tooltips para claridad
    	Tooltip.forComponent(aceptar)
    	.withText("Validar datos y proceder con la operación")
    	.withPosition(Tooltip.TooltipPosition.TOP);
    	Tooltip.forComponent(cancelar)
    	.withText("Limpiar formulario y cancelar operación")
    	.withPosition(Tooltip.TooltipPosition.TOP);
    	Tooltip.forComponent(volver)
    	.withText("Volver a la vista anterior")
    	.withPosition(Tooltip.TooltipPosition.TOP);

        // Iconos para mayor claridad
        aceptar.setIcon(VaadinIcon.CHECK.create());
        cancelar.setIcon(VaadinIcon.CLOSE.create());
        volver.setIcon(VaadinIcon.ARROW_LEFT.create());
    	
        HorizontalLayout botonera = new HorizontalLayout(aceptar, cancelar, volver);
        botonera.setSpacing(true);
        botonera.setPadding(false);
        botonera.setWidthFull();
//        botonera.setJustifyContentMode(JustifyContentMode.END);
        botonera.setJustifyContentMode(JustifyContentMode.START);
     // Agregar margen superior para separar del contenido
        botonera.getStyle()
            .set("margin-top", "1.5em")
            .set("padding-top", "1em")
            .set("border-top", "1px solid var(--lumo-contrast-10pct)");
        return botonera;
    }
    
 // Setters para callbacks
    public void setOnAccept(Runnable onAccept) {
        this.onAccept = onAccept;
    }
    
    public void setOnCancel(Runnable onCancel) {
        this.onCancel = onCancel;
    }
    
    public void setOnBack(Runnable onBack) {
        this.onBack = onBack;
    }
    
    // Eventos personalizados
    public static abstract class ProcessFormEvent extends ComponentEvent<AbstractProcessForm> {
		
    	private static final long serialVersionUID = 1L;

		protected ProcessFormEvent(AbstractProcessForm source) {
            super(source, false);
        }
    }
    
    public static class AcceptEvent extends ProcessFormEvent {
		
    	private static final long serialVersionUID = 1L;

		public AcceptEvent(AbstractProcessForm source) {
            super(source);
        }
    }
    
    public static class CancelEvent extends ProcessFormEvent {
		
    	private static final long serialVersionUID = 1L;

		public CancelEvent(AbstractProcessForm source) {
            super(source);
        }
    }
    
    public static class BackEvent extends ProcessFormEvent {
		
    	private static final long serialVersionUID = 1L;

		public BackEvent(AbstractProcessForm source) {
            super(source);
        }
    }
    
    // Métodos para agregar listeners
    public Registration addAcceptListener(ComponentEventListener<AcceptEvent> listener) {
        return addListener(AcceptEvent.class, listener);
    }
    
    public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
        return addListener(CancelEvent.class, listener);
    }
    
    public Registration addBackListener(ComponentEventListener<BackEvent> listener) {
        return addListener(BackEvent.class, listener);
    }

}
