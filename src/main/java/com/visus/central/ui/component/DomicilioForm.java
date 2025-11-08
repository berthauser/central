package com.visus.central.ui.component;

import java.util.Collections;
import java.util.List;
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
import com.visus.central.domain.model.Domicilio;
import com.visus.central.domain.model.Localidad;
import com.visus.central.infraestructure.persistence.entity.JpaDomicilioEntity.TipoDomicilio;

public class DomicilioForm extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private TextField calle = new TextField("Calle");
	private TextField numero = new TextField("Número");
	private TextField barrio = new TextField("Barrio");
	private TextField manzana = new TextField("Manzana");
	private TextField casa = new TextField("Casa");
	private TextField sector = new TextField("Sector");
	private TextField oficina = new TextField("Oficina");
	private TextField lote = new TextField("Lote");
	private final ComboBox<Localidad> localidad = new ComboBox<>("Localidad");
	private final ComboBox<TipoDomicilio> tipoDomicilio = new ComboBox<>("Tipo de Domicilio");
	
	private Domicilio currentDomicilio; // Nuevo campo para seguimiento

	private Button guardar = new Button("Guardar");
	private Button cancelar = new Button("Cancelar");

	// Callbacks
    private Consumer<Domicilio> onSave;
    private Runnable onCancel;
    private Function<Domicilio, String> onValidate; // NUEVO: validación personalizada
    
 // Servicio de localidades (inyectado desde fuera)
    private List<Localidad> localidadesDisponibles = Collections.emptyList();
	
	public DomicilioForm() {
	    setSpacing(true);
	    setPadding(true);
	    
	    // sacar esto si jode
	    setWidth("900px");
        setMaxWidth("100%");

	    String campoStyle = "campo-estilo-imagen";

	    // Calle y Número
        calle.addClassName(campoStyle);
        calle.setRequired(true);
        calle.setWidth("300px");

        numero.addClassName(campoStyle);
        numero.setRequired(true);
        numero.setWidth("80px");

        // Barrio
        barrio.addClassName(campoStyle);
        barrio.setWidth("300px");
	    
        // Localidad - NUEVO
        localidad.addClassName(campoStyle);
        localidad.setWidth("300px");
        localidad.setRequired(true); // obligatorio por la BD

        // Campos pequeños
        manzana.addClassName(campoStyle);
        manzana.setWidth("80px");

        casa.addClassName(campoStyle);
        casa.setWidth("80px");

        sector.addClassName(campoStyle);
        sector.setWidth("70px"); // más compacto

        oficina.addClassName(campoStyle);
        oficina.setWidth("60px"); // más compacto

        lote.addClassName(campoStyle);
        lote.setWidth("70px"); // más compacto

        tipoDomicilio.addClassName(campoStyle);
        tipoDomicilio.setWidth("250px");
        tipoDomicilio.setItems(TipoDomicilio.values());
        tipoDomicilio.setItemLabelGenerator(TipoDomicilio::toString);

        // Agrupaciones
        HorizontalLayout calleNumero = new HorizontalLayout(calle, numero);
        calleNumero.setSpacing(true);

        HorizontalLayout manzanaCasa = new HorizontalLayout(manzana, casa);
        manzanaCasa.setSpacing(true);

        // NUEVO: Grupo compacto para sector, oficina, lote
        HorizontalLayout sectorOficinaLote = new HorizontalLayout(sector, oficina, lote);
        sectorOficinaLote.setSpacing(true);
        sectorOficinaLote.setFlexGrow(1, sector);
        sectorOficinaLote.setFlexGrow(0, oficina);
        sectorOficinaLote.setFlexGrow(0, lote);

        // FormLayout - NUEVA ESTRUCTURA
        FormLayout layout = new FormLayout(
            calleNumero,           // Fila 1: Calle + Número
            barrio,                // Fila 2: Barrio
            localidad,             // Fila 3: Localidad (debajo de Barrio)
            manzanaCasa,           // Fila 4: Manzana + Casa
            sectorOficinaLote,     // Fila 5: Sector + Oficina + Lote (más compacto)
            tipoDomicilio          // Fila 6: Tipo de Domicilio
        );
	    
	    // NUEVO: Agrupamos manzanaCasa en una sola fila horizontal
	    HorizontalLayout filaInferior = new HorizontalLayout();
	    filaInferior.setSpacing(true);
	    filaInferior.add(manzanaCasa);

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
	
	// Método para configurar las localidades disponibles
    public void setLocalidades(java.util.List<Localidad> localidades) {
        this.localidadesDisponibles = localidades != null ? localidades : java.util.Collections.emptyList();
        localidad.setItems(this.localidadesDisponibles);
        localidad.setItemLabelGenerator(Localidad::getNombre); // asume que Localidad tiene getNombre()
    }

	private void guardar() {
		if (calle.isEmpty() || numero.isEmpty() || localidad.getValue() == null) {
            Notification.show("Calle, Número y Localidad son obligatorios", 3000, Notification.Position.MIDDLE)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        try {
            Domicilio d = new Domicilio();
            
         // MANTENER EL ID SI ES EDICIÓN
            if (currentDomicilio != null) {
                d.setId(currentDomicilio.getId());
            }
            
            d.setCalle(calle.getValue());
            d.setNumero(Short.valueOf(numero.getValue()));
            d.setBarrio(barrio.getValue());
            d.setManzana(manzana.getValue());
            d.setCasa(casa.getValue());
            d.setSector(sector.getValue());
            d.setOficina(oficina.getValue());
            d.setLote(lote.getValue());
            d.setTipoDomicilio(tipoDomicilio.getValue());
            d.setIdLocalidad(localidad.getValue().getIdlocalidad()); // 👈 ASIGNACIÓN CLAVE

            // Validación de regla de negocio
            if (onValidate != null) {
                String error = onValidate.apply(d);
                if (error != null) {
                    Notification.show(error, 4000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                    return;
                }
            }
			
			if (onSave != null) {
				onSave.accept(d);
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

	public void focusCalle() {
		calle.focus();
	}
	
	// NUEVOS métodos para callbacks
    public void setOnSave(Consumer<Domicilio> onSave) {
        this.onSave = onSave;
    }

    public void setOnCancel(Runnable onCancel) {
        this.onCancel = onCancel;
    }

    public void setOnValidate(Function<Domicilio, String> onValidate) {
        this.onValidate = onValidate;
    }
    
    // Método para cargar un domicilio existente (edición)
    public void setDomicilio(Domicilio domicilio) {
        if (domicilio == null) return;
        
        this.currentDomicilio = domicilio; // 👈 Guardar referencia al original
        
        calle.setValue(domicilio.getCalle());
        numero.setValue(domicilio.getNumero() != null ? domicilio.getNumero().toString() : "");
        barrio.setValue(domicilio.getBarrio());
        manzana.setValue(domicilio.getManzana());
        casa.setValue(domicilio.getCasa());
        sector.setValue(domicilio.getSector());
        oficina.setValue(domicilio.getOficina());
        lote.setValue(domicilio.getLote());
        tipoDomicilio.setValue(domicilio.getTipoDomicilio());
        
     // Cargar localidad si existe
        if (domicilio.getIdLocalidad() != null) {
            // Buscar la localidad por ID en la lista disponible
            Localidad localidadSeleccionada = localidadesDisponibles.stream()
                .filter(l -> l.getIdlocalidad().equals(domicilio.getIdLocalidad()))
                .findFirst()
                .orElse(null);
            localidad.setValue(localidadSeleccionada);
        } else {
            localidad.clear();
        }
    }
	
}
