package com.visus.central.ui.component;

import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public abstract class AbstractForm<T> extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	// logger
    private static final Logger log = LoggerFactory.getLogger(AbstractForm.class);
	
	protected final Binder<T> binder;
    protected final Button crear = new Button("Crear");
    protected final Button modificar = new Button("Modificar");
    protected final Button eliminar = new Button("Eliminar");
    protected final Button volver = new Button("Volver", new Icon(VaadinIcon.BACKWARDS));

    private final Class<T> entityClass;
    protected T current;
    
 // Callbacks funcionales para interoperabilidad con AbstractView
    private Consumer<T> onSave;
    private Consumer<T> onDelete;
    private Runnable onCancel;

    public void setOnSave(Consumer<T> onSave) {
        this.onSave = onSave;
    }

    public void setOnDelete(Consumer<T> onDelete) {
        this.onDelete = onDelete;
    }

    public void setOnCancel(Runnable onCancel) {
        this.onCancel = onCancel;
    }

	public AbstractForm(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.binder = new Binder<>(entityClass);
        
        crear.addClassName("btn-crear");
        modificar.addClassName("btn-modificar");
        eliminar.addClassName("btn-eliminar");
        volver.addClassName("btn-volver");

        crear.addClickListener(_ -> {
            if (validateAndProceed("Crear")) {
                if (onSave != null) onSave.accept(current);
                fireEvent(new CreateEvent(this, current));
            }
        });
        
        modificar.addClickListener(_ -> {
        	if (validateAndProceed("Modificar")) {
                if (onSave != null) onSave.accept(current);
                fireEvent(new UpdateEvent(this, current));
            }
        });

        eliminar.addClickListener(_ -> {
            if (onDelete != null) onDelete.accept(current);
            fireEvent(new DeleteEvent(this, current));
        });

        volver.addClickListener(_ -> {
            fireEvent(new BackEvent(this));
            if (onCancel != null) onCancel.run();
        });
        
     // Inicializar el binder con una instancia vacía
        this.current = createEmptyInstance();
        binder.setBean(current);
        
    }
    
	protected abstract void bindFields();
	
	public void setEntity(T entity) {
	   
		log.info("AbstractForm.setEntity() - Entidad: {} ID: {}", 
	            entity != null ? entity.getClass().getSimpleName() : "null",
	            entity != null ? getId(entity) : "null");
	        
	        this.current = entity;  // CORRECCIÓN 1: Usar current, no entity
	        
	        if (entity != null) {
	            try {
	                log.info("setFormValues() - Llamando a método personalizado");
	                setFormValues(entity);  // Establecer valores primero
	                
	                log.info("binder.setBean() - Asignando bean");
	                binder.setBean(entity); // Luego vincular con el binder
	                
	                log.info("Formulario cargado exitosamente para ID: {}", getId(entity));
	            } catch (Exception e) {
	                log.error("Error al cargar datos en el formulario para ID: {}", 
	                    entity != null ? getId(entity) : "null", e);
	                
	                // Limpiar el formulario si hay error
	                binder.removeBean();
	                clearForm();
	             // Mostrar error específico
	                String errorMsg = "Error al cargar los datos: ";
	                if (e instanceof NullPointerException) {
	                    errorMsg += "Hay campos con valores nulos que no pueden ser procesados.";
	                } else {
	                    errorMsg += e.getMessage();
	                }
	                
	                Notification.show(errorMsg, 5000, Notification.Position.MIDDLE);
	                throw new RuntimeException("Error en setFormValues", e);
	            }
	        } else {
	            // CORRECCIÓN 2: Esto NO es código muerto - es necesario
	            clearForm();
	            log.info("Formulario limpiado - entidad es null");
	        }
	}
	
	// MÉTODO AUXILIAR PARA OBTENER ID DE CUALQUIER ENTIDAD
	private Object getId(T entity) {
	    if (entity == null) return null;
	    try {
	        return entity.getClass().getMethod("getId").invoke(entity);
	    } catch (Exception e) {
	        return "N/A";
	    }
	}
	
	// Método de tipo helper
	private boolean validateAndProceed(String operation) {
		try {
            // Esto valida TODOS los campos y si son válidos, escribe los valores en 'current'
            binder.writeBean(current);
            System.out.println("✅ Validación y actualización OK - " + operation);
            
            // Solo después de una validación exitosa, actualizamos campos no bindeados
            updateCurrentFromBinder();
            
            return true;
            
        } catch (ValidationException e) {
            System.out.println("❌ Validación falló - " + operation);
            
            // Mostrar errores de validación
            e.getValidationErrors().forEach(error -> {
                System.out.println("   - " + error.getErrorMessage());
                
                // Mostrar notificación para el usuario
                Notification.show(error.getErrorMessage(), 
                    3000, Notification.Position.MIDDLE);
            });
            
            return false;
        }
	}
	
	protected Component buildBotonera() {
	    HorizontalLayout acciones = new HorizontalLayout(crear, modificar, eliminar, volver);
	    acciones.setSpacing(true);
	    acciones.setPadding(false);
	    return acciones;
	}

	public void clearForm() {
        this.current = createEmptyInstance();
        binder.setBean(current);
        setFormValues(current);
    }

	public T getEntity() {
		// NOTA: Este método NO valida, solo retorna la entidad actual
        // Si se necesita validar, usar validateAndProceed() antes
        return current;
    }

	protected void updateCurrentFromBinder() {
		// Las subclases pueden sobrescribir este método para actualizar
        // campos NO bindeados después de una validación exitosa
        // Por ejemplo: current.setDomicilios(domicilioSubForm.getDomicilios());
    }

	protected abstract void setFormValues(T entity);

	private T createEmptyInstance() {
        try {
            return entityClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo instanciar " + entityClass.getSimpleName(), e);
        }
    }

	public Class<T> getEntityClass() {
        return entityClass;
    }

	// Eventos personalizados
    @SuppressWarnings("serial")
    public static class CreateEvent extends FormEvent {
        public CreateEvent(AbstractForm<?> source, Object entity) {
            super(source, entity);
        }
    }

    @SuppressWarnings("serial")
    public static class UpdateEvent extends FormEvent {
        public UpdateEvent(AbstractForm<?> source, Object entity) {
            super(source, entity);
        }
    }

    @SuppressWarnings("serial")
    public static class DeleteEvent extends FormEvent {
        public DeleteEvent(AbstractForm<?> source, Object entity) {
            super(source, entity);
        }
    }

    @SuppressWarnings("serial")
    public static class BackEvent extends ComponentEvent<AbstractForm<?>> {
        public BackEvent(AbstractForm<?> source) {
            super(source, false);
        }
    }

    public Registration addCreateListener(ComponentEventListener<CreateEvent> listener) {
        return addListener(CreateEvent.class, listener);
    }

    public Registration addUpdateListener(ComponentEventListener<UpdateEvent> listener) {
        return addListener(UpdateEvent.class, listener);
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addBackListener(ComponentEventListener<BackEvent> listener) {
        return addListener(BackEvent.class, listener);
    }
    

}