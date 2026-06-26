package com.visus.central.ui.component;

import java.util.function.Consumer;
import java.util.function.Function;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.visus.central.domain.model.GrupoFam;
import com.visus.central.domain.model.Estado;
import com.visus.central.domain.model.Parentesco;
import com.visus.central.domain.model.TipoDocumento;

public class GrupoFamForm extends VerticalLayout {

    private static final long serialVersionUID = 1L;
    
    private TextField nombre = new TextField("Nombre");
    private ComboBox<Parentesco> parentesco = new ComboBox<>("Parentesco");
    private ComboBox<TipoDocumento> tipoDocumento = new ComboBox<>("Tipo de Documento");
    private TextField numero = new TextField("Número");
    private ComboBox<Estado> estado = new ComboBox<>("Estado");
    
    private GrupoFam currentGrupoFam;
    
    private Button guardar = new Button("Guardar");
	private Button cancelar = new Button("Cancelar");
    
    // Callbacks
    private Consumer<GrupoFam> onSave;
    private Runnable onCancel;
    private Function<GrupoFam, String> onValidate; // NUEVO: validación personalizada

    public GrupoFamForm() {
        buildLayout();
    }
    
    private void buildLayout() {
        String campoStyle = "campo-estilo-imagen";
        
        nombre.addClassName(campoStyle);
        nombre.setRequired(true);
        nombre.setWidth("300px");
        nombre.setMaxLength(100);
        
        // Configurar combos
        parentesco.addClassName(campoStyle);
        parentesco.setWidth("150px");
        parentesco.setRequired(true);
        parentesco.setItems(Parentesco.values());
        parentesco.setItemLabelGenerator(Parentesco::toString);
        
        tipoDocumento.addClassName(campoStyle);
        tipoDocumento.setWidth("150px");
        tipoDocumento.setRequired(true);
        tipoDocumento.setItems(TipoDocumento.values());
        tipoDocumento.setItemLabelGenerator(TipoDocumento::toString);
        
        estado.addClassName(campoStyle);
        estado.setWidth("150px");
        estado.setRequired(true);
        estado.setItems(Estado.values());
        estado.setItemLabelGenerator(Estado::toString);
        // Fin combos
        
        numero.addClassName(campoStyle);
        numero.setRequired(true);
        numero.setWidth("150px");
        numero.setPlaceholder("Número de documento");
        
        // FormLayout - NUEVA ESTRUCTURA
        FormLayout layout = new FormLayout(
            nombre,           // Fila 1: nombre
            parentesco,       // Fila 2: parentesco
            tipoDocumento,    // Fila 3: tipo de documento
            numero,           // Fila 4: número de documento
            estado     		  // Fila 5: estado
        );
        
        layout.setResponsiveSteps(
	    		new FormLayout.ResponsiveStep("0", 1),
	    		new FormLayout.ResponsiveStep("600px", 2),
	    		new FormLayout.ResponsiveStep("900px", 3)
	    		);
        
        // Botonera
        HorizontalLayout acciones = new HorizontalLayout(guardar, cancelar);
        acciones.setSpacing(true);
        acciones.setJustifyContentMode(JustifyContentMode.START);
        
        guardar.addClassName("btn-crear");
        cancelar.addClassName("btn-volver");
        
        add(layout, acciones);
        
        // Eventos
	    guardar.addClickListener(_ -> guardar());
	    cancelar.addClickListener(_ -> cancelar());
    }
    
    private void guardar() {
		if (nombre.isEmpty() || parentesco.isEmpty() || tipoDocumento.isEmpty() || numero.isEmpty() || estado.isEmpty()) {
            Notification.show("Todos los campos son obligatorios", 3000, Notification.Position.MIDDLE)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        try {
            GrupoFam grupoFamiliar = new GrupoFam();
            
         // MANTENER EL ID SI ES EDICIÓN
            if (currentGrupoFam != null) {
                grupoFamiliar.setId(currentGrupoFam.getId());
            }
            
            grupoFamiliar.setNombre(nombre.getValue());
            grupoFamiliar.setParentesco(parentesco.getValue());
            grupoFamiliar.setDocumento(tipoDocumento.getValue());
            grupoFamiliar.setNumero(Long.valueOf(numero.getValue()));
            grupoFamiliar.setEstado(estado.getValue());

            // Validación de regla de negocio
            if (onValidate != null) {
                String error = onValidate.apply(grupoFamiliar);
                if (error != null) {
                    Notification.show(error, 4000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                    return;
                }
            }
			
			if (onSave != null) {
				onSave.accept(grupoFamiliar);
			}
		} catch (NumberFormatException ex) {
            Notification.show("Número debe ser un valor numérico válido", 3000, Notification.Position.MIDDLE)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
	}

    private void cancelar() {
		if (onCancel != null) {
			onCancel.run();
		}
	}
    
    public void focusNombre() {
		nombre.focus();
	}
    
 // NUEVOS métodos para callbacks
    public void setOnSave(Consumer<GrupoFam> onSave) {
        this.onSave = onSave;
    }

    public void setOnCancel(Runnable onCancel) {
        this.onCancel = onCancel;
    }

    public void setOnValidate(Function<GrupoFam, String> onValidate) {
        this.onValidate = onValidate;
    }
    
 // Método para cargar un grupo familiar existente (edición)
    public void setGrupoFamiliar(GrupoFam grupoFam) {
        if (grupoFam == null) return;
        
        this.currentGrupoFam = grupoFam; // Guardar referencia al original
        
        nombre.setValue(grupoFam.getNombre());
        parentesco.setValue(grupoFam.getParentesco());
        tipoDocumento.setValue(grupoFam.getDocumento());
        numero.setValue(grupoFam.getNumero() != null ? grupoFam.getNumero().toString() : "");
        estado.setValue(grupoFam.getEstado());
    }

}
