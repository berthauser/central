package com.visus.central.ui.component;

import java.util.function.Consumer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

public abstract class AbstractForm<T> extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	
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
        
    }
    
	protected abstract void bindFields();

	public void setEntity(T entity) {
		
		System.out.println("📝 AbstractForm.setEntity() - Entidad: " + 
		        (entity != null ? entity.getClass().getSimpleName() + " ID:" + getId(entity) : "NULL"));
		
		this.current = entity;
		if (entity == null) {
			throw new IllegalArgumentException("La entidad no puede ser null");
		} else {
			System.out.println("📝 binder.setBean() - Asignando bean");
	        binder.setBean(entity); // 🔗 vincula campos
	        System.out.println("📝 setFormValues() - Llamando a método personalizado");
	        setFormValues(entity);  // 👈 ESTE MÉTODO DEBE EJECUTARSE
	        System.out.println("📝 setFormValues() - Método personalizado ejecutado");
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
	    var validationResult = binder.validate();
	    if (validationResult.isOk()) {
	        System.out.println("✅ Todos los campos del BINDER OK - " + operation);
	        updateCurrentFromBinder();
	        return true;
	    } else {
	        System.out.println("❌ Los campos del BINDER NOT OK - " + operation);
	        validationResult.getValidationErrors().forEach(error -> {
	            System.out.println("   - " + error.getErrorMessage());
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
        updateCurrentFromBinder();
        return current;
    }

	protected void updateCurrentFromBinder() {
        // Si usás campos no bindables, actualizalos aquí manualmente
        // Ejemplo: current.setProvincia(comboProvincia.getValue());
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