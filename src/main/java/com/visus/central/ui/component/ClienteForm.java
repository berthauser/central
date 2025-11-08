package com.visus.central.ui.component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.visus.central.domain.model.Cliente;
import com.visus.central.domain.model.Domicilio;
import com.visus.central.domain.model.GrupoFam;
import com.visus.central.domain.port.in.DomicilioUseCase;
import com.visus.central.domain.port.in.GrupoFamUseCase;
import com.visus.central.infraestructure.persistence.entity.JpaClienteEntity.Estado;
import com.visus.central.infraestructure.persistence.entity.JpaClienteEntity.Sexo;
import com.visus.central.infraestructure.persistence.entity.JpaClienteEntity.SituacionFiscal;
import com.visus.central.infraestructure.persistence.entity.JpaClienteEntity.TipoDocumento;
import com.visus.central.infraestructure.util.SpringContextHelper;


public class ClienteForm extends AbstractForm<Cliente> {

	private static final long serialVersionUID = 1L;

	// Campos principales
	private TextField nombreFantasia = new TextField("Nombre Fantasía");
	private TextField nombreCliente = new TextField("Nombre Cliente");
	private ComboBox<Sexo> sexo = new ComboBox<>("Sexo");
	private TextField telefonoFijo = new TextField("Teléfono Fijo");
	private TextField telefonoMovil = new TextField("Teléfono Móvil");

	private EmailField email = new EmailField("Email");

	// Detalle: Datos Secundarios
	private DatePicker fechaIngreso = new DatePicker("Fecha Ingreso");
	private DatePicker fechaActualizacion = new DatePicker("Fecha Actualización");
	private DatePicker fechaBaja = new DatePicker("Fecha Baja");
	private DatePicker fechaUltimaCompra = new DatePicker("Fecha Última Compra");

	// Detalle: Datos Financieros
	private IntegerField limiteFacturasVencidas = new IntegerField("Límite Facturas Vencidas");
	private BigDecimalField limiteCredito = new BigDecimalField("Límite Crédito");
	private BigDecimalField pagoMinimo = new BigDecimalField("Pago Mínimo");
	private ComboBox<SituacionFiscal> situacionFiscal = new ComboBox<>("Situación Fiscal");
	private BigDecimalField saldoCtaCte = new BigDecimalField("Saldo Cta Cte");

	// Campos finales
	private TextArea observaciones = new TextArea("Observaciones");
	private ComboBox<TipoDocumento> documento = new ComboBox<>("Documento");
	private TextField numero = new TextField("Número");
	private ComboBox<Estado> estado = new ComboBox<>("Estado");

	// Subformularios
	private DomicilioSubForm domicilioSubForm;
	private GrupoFamSubForm grupoFamSubForm;
	
	// Referencias a los Details
	private Details datosSecundarios;
	private Details datosFinancieros;
	
	private boolean domiciliosModificados = false;
	private boolean grupoFamiliarModificado = false;

	public ClienteForm() {
		super(Cliente.class);
		System.out.println("📝 ClienteForm.constructor() - modificar es null: " + (modificar == null));
		buildLayout();
		// Listener para cambios en domicilios
        domicilioSubForm.addDomListener(() -> {
            domiciliosModificados = true;
            actualizarEstadoBotones();
            System.out.println("📝 Cambios en domicilios detectados");
            
        });
        
        // Listener para cambios en Grupo Familiar
        grupoFamSubForm.addDomListener(() -> {
        	grupoFamiliarModificado = true;
        	actualizarEstadoBotones();
        	System.out.println("📝 Cambios en grupo familiar detectados");
        });
	}

	private void buildLayout() {
		
        this.domicilioSubForm = new DomicilioSubForm();
        this.grupoFamSubForm = new GrupoFamSubForm();
		
		String campoStyle = "campo-estilo-imagen";

		// Configurar campos principales
		nombreFantasia.addClassName(campoStyle);
		nombreFantasia.setRequired(true);
		nombreFantasia.setWidth("300px");

		nombreCliente.addClassName(campoStyle);
		nombreCliente.setRequired(true);
		nombreCliente.setWidth("300px");

		sexo.addClassName(campoStyle);
		sexo.setItems(Arrays.asList(Sexo.values()));
		sexo.setItemLabelGenerator(Sexo::getLabel);
		sexo.setWidth("150px");

		telefonoFijo.addClassName(campoStyle);
		telefonoFijo.setWidth("150px");

		telefonoMovil.addClassName(campoStyle);
		telefonoMovil.setWidth("150px");

		numero.addClassName(campoStyle);
		numero.setWidth("150px");

		email.addClassName(campoStyle);
		email.setWidth("300px");

		// Configurar detalle: Datos Secundarios
		fechaIngreso.addClassName(campoStyle);
		fechaIngreso.setWidth("150px");

		fechaActualizacion.addClassName(campoStyle);
		fechaActualizacion.setWidth("150px");

		fechaBaja.addClassName(campoStyle);
		fechaBaja.setWidth("150px");

		fechaUltimaCompra.addClassName(campoStyle);
		fechaUltimaCompra.setWidth("150px");

		// Configurar detalle: Datos Financieros
		limiteFacturasVencidas.addClassName(campoStyle);
		limiteFacturasVencidas.setWidth("100px");

		limiteCredito.addClassName(campoStyle);
		limiteCredito.setWidth("150px");
		limiteCredito.setHelperText("$");

		pagoMinimo.addClassName(campoStyle);
		pagoMinimo.setWidth("150px");
		pagoMinimo.setHelperText("$");

		situacionFiscal.addClassName(campoStyle);
		situacionFiscal.setItems(Arrays.asList(SituacionFiscal.values()));
		situacionFiscal.setItemLabelGenerator(SituacionFiscal::getLabel);
		situacionFiscal.setWidth("300px");
		situacionFiscal.setRequired(true);

		saldoCtaCte.addClassName(campoStyle);
		saldoCtaCte.setWidth("150px");
		saldoCtaCte.setHelperText("$");

		// Configurar campos finales
		observaciones.addClassName(campoStyle);
		observaciones.setWidth("600px");
		observaciones.setHeight("100px");

		documento.addClassName(campoStyle);
		documento.setItems(Arrays.asList(TipoDocumento.values()));
		documento.setItemLabelGenerator(TipoDocumento::getLabel);
		documento.setWidth("200px");
		documento.setRequired(true);

		estado.addClassName(campoStyle);
		estado.setItems(Arrays.asList((Estado.values())));
		estado.setItemLabelGenerator(Estado::getLabel);
		estado.setWidth("150px");
		estado.setRequired(true);

		// Layout principal
		HorizontalLayout fila1 = new HorizontalLayout(nombreCliente, nombreFantasia ,sexo, telefonoFijo, telefonoMovil);
		fila1.setSpacing(true);
		fila1.setAlignItems(Alignment.END);

		HorizontalLayout fila2 = new HorizontalLayout(documento, numero, email, estado);
		fila2.setSpacing(true);
		fila2.setAlignItems(Alignment.END);
		
		// Detalle: Datos Secundarios
		FormLayout datosSecundariosLayout = new FormLayout(
				fechaIngreso, fechaActualizacion, fechaBaja, fechaUltimaCompra
				);
		datosSecundariosLayout.setResponsiveSteps(
				new FormLayout.ResponsiveStep("0", 1),
				new FormLayout.ResponsiveStep("400px", 2),
				new FormLayout.ResponsiveStep("600px", 4)
				);
 		
		datosSecundarios = new Details("Datos Secundarios", datosSecundariosLayout);
		datosSecundarios.setOpened(false);
		datosSecundarios.addThemeVariants(DetailsVariant.SMALL);
		datosSecundarios.addThemeVariants(DetailsVariant.FILLED);
		datosSecundarios.setWidthFull();
		
		// Detalle: Datos Financieros
		FormLayout datosFinancierosLayout = new FormLayout(
				limiteFacturasVencidas, limiteCredito, pagoMinimo, situacionFiscal, saldoCtaCte
				);
		datosFinancierosLayout.setResponsiveSteps(
				new FormLayout.ResponsiveStep("0", 1),
//				new FormLayout.ResponsiveStep("400px", 2),
				new FormLayout.ResponsiveStep("20em", 2),
				new FormLayout.ResponsiveStep("600px", 3)
				);

		datosFinancieros = new Details("Datos Financieros", datosFinancierosLayout);
//		
		datosFinancieros.setOpened(false);
		datosFinancieros.addThemeVariants(DetailsVariant.SMALL);
		datosFinancieros.addThemeVariants(DetailsVariant.FILLED);
		datosFinancieros.setWidthFull();

		HorizontalLayout filaDatos = new HorizontalLayout(datosSecundarios, datosFinancieros);

		// Botonera
		HorizontalLayout botonera = (HorizontalLayout) buildBotonera();
	
		botonera.setSpacing(true);
		botonera.setPadding(false);
		
		// Reducir el margen superior de la botonera
        botonera.getStyle().set("margin-top", "var(--lumo-space-xs)");
        
		// AGREGAR LISTENER PARA CERRAR DETAILS AL MODIFICAR
	    modificar.addClickListener(_ -> cerrarDetails());
	    
	    // Añadir listeners de foco para cerrar los Details
	    List<Component> focusComponents = List.of(
	    		nombreCliente, nombreFantasia ,sexo, telefonoFijo, telefonoMovil, documento, numero, email, estado,
	    		crear, modificar, eliminar, volver
	    		);

        for (Component comp : focusComponents) {
            comp.getElement().addEventListener("focus", _ -> {
            	if (datosSecundarios != null) {
        	        datosSecundarios.setOpened(false);
        	    }
        	    if (datosFinancieros != null) {
        	        datosFinancieros.setOpened(false);
        	    }
            });
        }

        add(new H3("Domicilios"));
		// Agregar todo al formulario
		add(fila1, fila2, filaDatos, observaciones);
		add(botonera);
		add(domicilioSubForm, grupoFamSubForm);

		bindFields();
	}

	@Override
	protected void bindFields() {
		binder.forField(nombreFantasia)
		.asRequired("El Nombre Fantasía es Obligatorio")
		.bind(Cliente::getNombreFantasia, Cliente::setNombreFantasia);

		binder.forField(nombreCliente)
		.asRequired("El Nombre Cliente es Obligatorio")
		.bind(Cliente::getNombreCliente, Cliente::setNombreCliente);

		binder.forField(sexo)
		.asRequired("El Sexo es Obligatorio")
		.bind(Cliente::getSexo, Cliente::setSexo);

		binder.bind(telefonoFijo, Cliente::getTelefonoFijo, Cliente::setTelefonoFijo);
		binder.bind(telefonoMovil, Cliente::getTelefonoMovil, Cliente::setTelefonoMovil);

		binder.forField(numero)
		.withNullRepresentation("")
		.withConverter(new StringToLongConverter("Debe ser un número"))
		.bind(Cliente::getNumero, Cliente::setNumero);

		binder.bind(email, Cliente::getEmail, Cliente::setEmail);

		// Binding de fechas
		binder.bind(fechaIngreso, Cliente::getFechaIngreso, Cliente::setFechaIngreso);
		binder.bind(fechaActualizacion, Cliente::getFechaActualizacion, Cliente::setFechaActualizacion);
		binder.bind(fechaBaja, Cliente::getFechaBaja, Cliente::setFechaBaja);
		binder.bind(fechaUltimaCompra, Cliente::getFechaUltimaCompra, Cliente::setFechaUltimaCompra);

		// Binding de datos financieros
		binder.forField(limiteFacturasVencidas)
		.withConverter(
				// Integer -> Short (de UI a modelo)
				value -> value != null ? value.shortValue() : null,
						// Short -> Integer (de modelo a UI)
						value -> value != null ? value.intValue() : null
				)
		.bind(Cliente::getLimiteFacturasVencidas, Cliente::setLimiteFacturasVencidas);

		// Límite Crédito - no negativo
		binder.forField(limiteCredito) // BigDecimalField -> trabaja con BigDecimal
		.withValidator(value -> value == null || value.compareTo(BigDecimal.ZERO) >= 0, 
				"El límite de crédito no puede ser negativo")
		.withValidator(value -> value == null || value.scale() <= 2,
				"El límite de crédito no puede tener más de 2 decimales")
		.bind(Cliente::getLimiteCredito, Cliente::setLimiteCredito);

		// Pago Mínimo - no negativo, permitir null
		binder.forField(pagoMinimo)
		    .withValidator(value -> value == null || value.compareTo(BigDecimal.ZERO) >= 0, 
		        "El pago mínimo no puede ser negativo")
		    .withValidator(value -> value == null || value.scale() <= 2,
		        "El pago mínimo no puede tener más de 2 decimales")
		    .withValidator(value -> value == null || value.precision() <= 18,  // ✅ Cambio aquí
		        "El pago mínimo no puede tener más de 18 dígitos")
		    .bind(Cliente::getPagoMinimo, Cliente::setPagoMinimo);
		
		binder.bind(situacionFiscal, Cliente::getSituacionFiscal, Cliente::setSituacionFiscal);

		// Saldo Cta Cte - puede ser negativo
		binder.forField(saldoCtaCte) // BigDecimalField -> trabaja con BigDecimal
		.withValidator(value -> value == null || value.scale() <= 2,
				"El saldo no puede tener más de 2 decimales")
		.bind(Cliente::getSaldoCtaCte, Cliente::setSaldoCtaCte);

		binder.bind(observaciones, Cliente::getObservaciones, Cliente::setObservaciones);
		binder.bind(documento, Cliente::getDocumento, Cliente::setDocumento);
		binder.bind(estado, Cliente::getEstado, Cliente::setEstado);
	}

	@Override
	protected void setFormValues(Cliente entity) {
		
		System.out.println("📝 setFormValues() - Cargando cliente: " + 
	            (entity != null ? entity.getNombreFantasia() : "NULL"));
		
		if (entity == null) {
			clearForm();
			// Limpiar domicilios
            domicilioSubForm.setDomicilios(new ArrayList<>());
			return;
		}

		nombreFantasia.setValue(entity.getNombreFantasia() != null ? entity.getNombreFantasia() : "");
		nombreCliente.setValue(entity.getNombreCliente() != null ? entity.getNombreCliente() : "");
		sexo.setValue(entity.getSexo());
		telefonoFijo.setValue(entity.getTelefonoFijo() != null ? entity.getTelefonoFijo() : "");
		telefonoMovil.setValue(entity.getTelefonoMovil() != null ? entity.getTelefonoMovil() : "");
		numero.setValue(entity.getNumero() != null ? entity.getNumero().toString() : "");
		email.setValue(entity.getEmail() != null ? entity.getEmail() : "");

		fechaIngreso.setValue(entity.getFechaIngreso());
		fechaActualizacion.setValue(entity.getFechaActualizacion());
		fechaBaja.setValue(entity.getFechaBaja());
		fechaUltimaCompra.setValue(entity.getFechaUltimaCompra());
		// IntegerField - CONVERTIR Short a Integer
		limiteFacturasVencidas.setValue(
				entity.getLimiteFacturasVencidas() != null ? 
						entity.getLimiteFacturasVencidas().intValue() : null
				);
		limiteCredito.setValue(entity.getLimiteCredito());
		pagoMinimo.setValue(entity.getPagoMinimo());
		situacionFiscal.setValue(entity.getSituacionFiscal());
		saldoCtaCte.setValue(entity.getSaldoCtaCte());
		observaciones.setValue(entity.getObservaciones() != null ? entity.getObservaciones() : "");
		documento.setValue(entity.getDocumento());
		estado.setValue(entity.getEstado());
		
		// CARGAR DOMICILIOS Y GRUPO FAMILIAR SOLO CUANDO SE EDITA UN CLIENTE EXISTENTE
		if (entity.getId() != null) {
		    System.out.println("📝 Cliente existente (ID: " + entity.getId() + "), cargando domicilios y grupo familiar bajo demanda");
		    cargarDomiciliosDelCliente(entity.getId());
		    cargarGrupoFamDelCliente(entity.getId());
		} else {
		    System.out.println("📝 Cliente Nuevo - Sin domicilios ni grupo familiar");
		    domicilioSubForm.setDomicilios(new ArrayList<>());
		    grupoFamSubForm.setGrupoFam(new ArrayList<>());
		}
		
	}
	
	@Override
	protected void updateCurrentFromBinder() {
		System.out.println("=== updateCurrentFromBinder() ===");
	    
	    if (current != null) {
	        // Para domicilios
	        List<Domicilio> domicilios = domicilioSubForm.getDomicilios();
	        if (domicilios != null) {
	            for (Domicilio domicilio : domicilios) {
	                domicilio.setIdCliente(current.getId());
	                System.out.println("📝 Asignando idCliente: " + current.getId() + " al domicilio");
	            }
	        }
	        current.setDomicilios(domicilios);
	        
	        // Para grupo familiar
	        List<GrupoFam> grupoFam = grupoFamSubForm.getGrupoFam();
	        if (grupoFam != null) {
	            for (GrupoFam miembro : grupoFam) {
	                miembro.setIdCliente(current.getId()); // O el método que uses para establecer la relación
	                System.out.println("📝 Asignando idCliente: " + current.getId() + " al familiar: " + miembro.getNombre());
	            }
	        }
	        current.setGrupoFam(grupoFam);
	        System.out.println("📝 Domicilios asignados: " + (domicilios != null ? domicilios.size() : 0));
	        System.out.println("📝 Familiares asignados: " + (grupoFam != null ? grupoFam.size() : 0));
	    }
	    
	    domiciliosModificados = false;
	    grupoFamiliarModificado = false;
	    cerrarDetails();
	    actualizarEstadoBotones();
	}
	
	private void actualizarEstadoBotones() {
		System.out.println("📝 actualizarEstadoBotones() - domiciliosModificados: " + domiciliosModificados + ", grupoFamiliarModificado: " + grupoFamiliarModificado);
	    
	    if (modificar == null) {
	        System.out.println("⚠️ modificar es null - no se puede actualizar botones");
	        return;
	    }

	    boolean hayCambiosPendientes = domiciliosModificados || grupoFamiliarModificado;
	    actualizarBotonModificar(hayCambiosPendientes);
	}
	
	private void cargarDomiciliosDelCliente(Integer idCliente) {
		if (idCliente == null) {
	        System.out.println("📝 Cliente nuevo - Sin domicilios");
	        domicilioSubForm.setDomicilios(new ArrayList<>());
	        return;
	    }
	    
	    try {
	        System.out.println("🔍 Buscando domicilios para cliente ID: " + idCliente);
	        
	        // ✅ Usar el UseCase para cargar domicilios (solo lectura)
	        DomicilioUseCase domicilioUseCase = SpringContextHelper.getBean(DomicilioUseCase.class);
	        List<Domicilio> domicilios = domicilioUseCase.findByClienteId(idCliente);
	        
	        System.out.println("📝 Encontrados " + domicilios.size() + " domicilios en BD");
	        domicilioSubForm.setDomicilios(domicilios);
	        
	    } catch (Exception e) {
	        System.err.println("❌ Error al cargar domicilios: " + e.getMessage());
	        domicilioSubForm.setDomicilios(new ArrayList<>());
	    }
	}
	
	private void cargarGrupoFamDelCliente(Integer idCliente) {
		if (idCliente == null) {
	        System.out.println("📝 Cliente nuevo - Sin grupo familiar");
	        grupoFamSubForm.setGrupoFam(new ArrayList<>());
	        return;
	    }
	    
	    try {
	        System.out.println("🔍 Buscando grupo familiar para cliente ID: " + idCliente);
	        
	        // ✅ Usar el UseCase para cargar grupo familiar (solo lectura)
	        GrupoFamUseCase grupoFamUseCase = SpringContextHelper.getBean(GrupoFamUseCase.class);
	        List<GrupoFam> grupoFam = grupoFamUseCase.findByClienteId(idCliente);
	        
	        System.out.println("📝 Encontrados " + grupoFam.size() + " miembros en el grupo familiar");
	        grupoFamSubForm.setGrupoFam(grupoFam);
	        
	    } catch (Exception e) {
	        System.err.println("❌ Error al cargar grupo familiar: " + e.getMessage());
	        grupoFamSubForm.setGrupoFam(new ArrayList<>());
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
	
	// MÉTODO AUXILIAR PARA CERRAR LOS DETAILS
	private void cerrarDetails() {
		if (datosSecundarios != null) {
			datosSecundarios.setOpened(false);
		}
		if (datosFinancieros != null) {
			datosFinancieros.setOpened(false);
		}
	}

	public List<Domicilio> getDomicilios() {
		return domicilioSubForm.getDomicilios();
	}

	public List<GrupoFam> getGrupoFam() {
		return grupoFamSubForm.getGrupoFam();
	}
}
