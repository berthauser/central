package com.visus.central.ui.component;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.visus.central.domain.model.Domicilio;
import com.visus.central.domain.model.Vendedor;
import com.visus.central.domain.port.in.DomicilioUseCase;
import com.visus.central.domain.model.SituacionFiscal;
import com.visus.central.domain.model.TipoDocumento;
import com.visus.central.infraestructure.util.SpringContextHelper;

public class VendedorForm extends AbstractForm<Vendedor> {
	
	private static final long serialVersionUID = 1L;
	
	private TextField nombre = new TextField("Nombre");
    private TextField telefono = new TextField("Móvil");
    private final ComboBox<TipoDocumento> tipoDocumento = new ComboBox<>("Tipo de Documento");
    private TextField numero = new TextField("Número");
    private final ComboBox<SituacionFiscal> situacionFiscal = new ComboBox<>("Situación Fiscal");
    private DomicilioSubForm domicilioSubForm;
    private boolean domiciliosModificados = false;

    public VendedorForm() {
        super(Vendedor.class);
        buildLayout();
        
        // Listener para cambios en domicilios
        domicilioSubForm.addDomListener(() -> {
            domiciliosModificados = true;
            actualizarEstadoBotones();
        });
    }

    private void buildLayout() {
    	
    	this.domicilioSubForm = new DomicilioSubForm();

    	// Estilos comunes
    	String campoStyle = "campo-estilo-imagen";
    	nombre.addClassName(campoStyle);
    	telefono.addClassName(campoStyle);
    	tipoDocumento.addClassName(campoStyle);
    	numero.addClassName(campoStyle);
    	situacionFiscal.addClassName(campoStyle);

    	// Anchos
    	nombre.setWidth("300px");
    	telefono.setWidth("200px");
    	situacionFiscal.setWidth("250px");
    	tipoDocumento.setWidth("150px");
    	numero.setWidth("150px");

    	// Configurar combos
    	tipoDocumento.setItems(TipoDocumento.values());
    	tipoDocumento.setItemLabelGenerator(TipoDocumento::toString);
    	situacionFiscal.setItems(SituacionFiscal.values());
    	situacionFiscal.setItemLabelGenerator(SituacionFiscal::toString);

    	// Panel de documento
    	VerticalLayout documentoLayout = new VerticalLayout(tipoDocumento, numero);
    	documentoLayout.setSpacing(true);
    	documentoLayout.setPadding(false);
    	documentoLayout.setWidthFull();

    	// Details para documento
    	Details documentoDetails = new Details("Documento", documentoLayout);
    	documentoDetails.setOpened(false);
    	documentoDetails.setWidth("250px");
    	documentoDetails.setHeight("auto"); // 👈 clave: no fuerza altura fija

    	// Fila 1: Nombre + Móvil + Situación Fiscal + Documento
    	HorizontalLayout filaCampos = new HorizontalLayout(nombre, telefono, situacionFiscal, documentoDetails);
    	filaCampos.setSpacing(true);
    	filaCampos.setPadding(false);
    	filaCampos.setFlexGrow(1, nombre); // nombre ocupa el resto
    	filaCampos.setFlexGrow(0, telefono);
    	filaCampos.setFlexGrow(0, situacionFiscal);
    	filaCampos.setFlexGrow(0, documentoDetails); // 👈 no crece verticalmente

    	// Botonera
    	HorizontalLayout botonera = (HorizontalLayout) buildBotonera();
    	botonera.setSpacing(true);
    	botonera.setPadding(false);

    	// Reducir el margen superior de la botonera
    	botonera.getStyle().set("margin-top", "var(--lumo-space-xs)");

    	// Añadir listeners de foco para cerrar el Details
    	List<Component> focusComponents = List.of(
    			nombre, telefono, situacionFiscal,
    			crear, modificar, eliminar, volver
    			);

    	for (Component comp : focusComponents) {
    		comp.getElement().addEventListener("focus", _ -> {
    			if (documentoDetails.isOpened()) {
    				documentoDetails.setOpened(false);
    			}
    		});
    	}

    	// Agregar al formulario
    	add(filaCampos);
    	add(botonera);
    	add(new H3("Domicilios"));
    	add(domicilioSubForm);

    	bindFields();

    }

    @Override
    protected void bindFields() {
    	binder.forField(nombre)
    		.asRequired("El Nombre es Obligatorio")
    		.bind(Vendedor::getNombre, Vendedor::setNombre);
    	
    	binder.bind(telefono, Vendedor::getTelefono, Vendedor::setTelefono);
    	
    	binder.forField(tipoDocumento)
    		.asRequired("El Tipo de Documento es Obligatorio")
    		.bind(Vendedor::getTipoDocumento, Vendedor::setTipoDocumento);
    	
    	binder.forField(numero)
    		.withNullRepresentation("") // ¡esto es clave!
    		.withConverter(new StringToLongConverter("Debe ser un número"))
    		.bind(Vendedor::getNumero, Vendedor::setNumero);
    	
    	binder.forField(situacionFiscal)
    		.asRequired("La Situación Fiscal es Obligatoria")
    		.bind(Vendedor::getSituacionFiscal, Vendedor::setSituacionFiscal);
    }

    public List<Domicilio> getDomicilios() {
        return domicilioSubForm.getDomicilios();
    }

    @Override
    protected void setFormValues(Vendedor entity) {
        
        if (entity == null) {
        	clearForm();
            // Limpiar domicilios
            domicilioSubForm.setDomicilios(new ArrayList<>());
            return;
        }

        nombre.setValue(entity.getNombre() != null ? entity.getNombre() : "");
        telefono.setValue(entity.getTelefono() != null ? entity.getTelefono() : "");
        tipoDocumento.setValue(entity.getTipoDocumento());
        numero.setValue(entity.getNumero() != null ? entity.getNumero().toString() : "");
        situacionFiscal.setValue(entity.getSituacionFiscal());
        
        // Cargar domicilios del vendedor
        if (entity.getDomicilios() != null) {
            domicilioSubForm.setDomicilios(new ArrayList<>(entity.getDomicilios()));
        } else {
            domicilioSubForm.setDomicilios(new ArrayList<>());
        }
        
        // CÓDIGO REFACTORIZADO: Cargar domicilios bajo demanda
        if (entity.getId() != null) {
            // Vendedor existente - cargar domicilios bajo demanda
            System.out.println("Vendedor existente (ID: " + entity.getId() + "), cargando domicilios bajo demanda");
            cargarDomiciliosDelVendedor(entity.getId());
        } else {
            // Vendedor nuevo - sin domicilios
            System.out.println("Vendedor nuevo, sin domicilios");
            domicilioSubForm.setDomicilios(new ArrayList<>());
        }
        
    }
    
    @Override
    protected void updateCurrentFromBinder() {
    	System.out.println("=== updateCurrentFromBinder() ===");
        System.out.println("Domicilios en DomicilioSubForm: " + 
            (domicilioSubForm.getDomicilios() != null ? domicilioSubForm.getDomicilios().size() : "null"));
        
        if (current != null) {
            current.setDomicilios(domicilioSubForm.getDomicilios());
            System.out.println("Domicilios asignados al vendedor current: " + 
                (current.getDomicilios() != null ? current.getDomicilios().size() : "null"));
        } else {
            System.out.println("current es null en updateCurrentFromBinder()");
        }
        
        domiciliosModificados = false;
        actualizarEstadoBotones();
    }
    
    private void actualizarEstadoBotones() {
        System.out.println("📝 actualizarEstadoBotones() - domiciliosModificados: " + domiciliosModificados);

        if (modificar == null) {
        	System.out.println("⚠️ modificar es null - no se puede actualizar botones");
        	return;
        }

        boolean hayCambiosPendientes = domiciliosModificados; //|| otroBooleano;
        actualizarBotonModificar(hayCambiosPendientes);
    }
    
    private void cargarDomiciliosDelVendedor(Integer idVendedor) {
        if (idVendedor == null) {
            System.out.println("📝 Vendedor nuevo - Sin domicilios");
            domicilioSubForm.setDomicilios(new ArrayList<>());
            return;
        }
        
        try {
        	// CARGAR DOMICILIOS BAJO DEMANDA
            System.out.println("Buscando domicilios para vendedor ID: " + idVendedor);
            
            // Usar el UseCase para cargar domicilios
            DomicilioUseCase domicilioUseCase = SpringContextHelper.getBean(DomicilioUseCase.class);
            List<Domicilio> domicilios = domicilioUseCase.findByVendedorId(idVendedor);
            
            System.out.println("Encontrados " + domicilios.size() + " domicilios en BD");
            domicilioSubForm.setDomicilios(domicilios);
            
        } catch (Exception e) {
            System.err.println("❌ Error al cargar domicilios del vendedor: " + e.getMessage());
            e.printStackTrace();
            domicilioSubForm.setDomicilios(new ArrayList<>());
        }
    }
    
    private void actualizarBotonModificar(boolean hayCambiosPendientes) {
	    if (hayCambiosPendientes) {
	        modificar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
	        modificar.setText("Guardar Cambios*");
	        System.out.println("🟢 Botón Modificar actualizado - Hay cambios pendientes");
	    } else {
	        modificar.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
	        modificar.setText("Modificar");
	        System.out.println("🟡 Botón Modificar restaurado - Sin cambios pendientes");
	    }
	}
    
}
