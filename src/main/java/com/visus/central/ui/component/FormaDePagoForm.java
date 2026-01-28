package com.visus.central.ui.component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.visus.central.domain.model.Coeficiente;
import com.visus.central.domain.model.FormaDePago;
import com.visus.central.domain.port.in.CoeficienteUseCase;
import com.visus.central.infraestructure.persistence.entity.JpaFormaDePagoEntity.ModalidadPago;
import com.visus.central.infraestructure.util.SpringContextHelper;

public class FormaDePagoForm extends AbstractForm<FormaDePago> {
	
private static final long serialVersionUID = 1L;
    
    private ComboBox<ModalidadPago> modalidad = new ComboBox<>("Modalidad");
    private Checkbox esDtoProntoPago = new Checkbox("Dto. Pronto Pago"); 
    private Checkbox esMesesCompletos = new Checkbox("Meses Completos");
    private BigDecimalField dtoProntoPago = new BigDecimalField("Dto. Pronto Pago");
    private ComboBox<Coeficiente> coeficiente = new ComboBox<>("Coeficiente");
    
    // Variables de estado internas
    // originalDtoProntoPago: valor traído desde la fila seleccionada en la grilla
    private BigDecimal originalDtoProntoPago = null;
    // Nueva bandera para evitar bucles
    private boolean updatingFromCode = false;
    
    public FormaDePagoForm() {
        super(FormaDePago.class);
        buildLayout();
        cargarCoeficientes();
    }
    
	private void buildLayout() {
        String campoStyle = "campo-estilo-imagen";
        
        modalidad.addClassName(campoStyle);
        modalidad.setRequired(true);
        modalidad.setItems((ModalidadPago.values()));
        modalidad.setItemLabelGenerator(ModalidadPago::getLabel);
        modalidad.setWidth("200px");
        
        coeficiente.addClassName(campoStyle);
        coeficiente.setRequired(true);
        coeficiente.setWidth("400px");
        
        // CheckBox
        esDtoProntoPago.addClassName(campoStyle);
        esDtoProntoPago.setWidth("150px");
        esDtoProntoPago.setTooltipText("Habilita la opción de descuento por Pronto Pago");
        
        dtoProntoPago.addClassName(campoStyle);
        dtoProntoPago.setWidth("150px");
        dtoProntoPago.setSuffixComponent(new Span("%"));
        dtoProntoPago.setTooltipText("Porcentaje de descuento que se aplicará si se paga antes de la fecha de vencimiento");
        
        esMesesCompletos.addClassName(campoStyle);
        esMesesCompletos.setWidth("150px");
        esMesesCompletos.setTooltipText("Cuando los días de plazo sean múltiplos de 30, los plazos se calcularán por meses en lugar de días. Es decir, si la fecha de factura es 21 de enero y la cantidad de cuotas son 3 (30, 60 y 90), las fechas de vencimiento serán 21 de febrero, 21 de marzo y 21 de abril.");
        
        // Configurar lógica de habilitación condicional
        // Listener del checkbox: al deschequear limpia la UI, al chequear restaura stagedDtoProntoPago
        esDtoProntoPago.addValueChangeListener(event -> {
        	// Evitar bucle: si la actualización viene del código, no hacer nada
            if (updatingFromCode) {
                return;
            }
        	
        	boolean tieneDto = Boolean.TRUE.equals(event.getValue());
            dtoProntoPago.setReadOnly(!tieneDto); // lo pone en FALSE
            
            if (!tieneDto) {
                // Guardar el valor actual antes de limpiar
                if (dtoProntoPago.getValue() != null) {
                	originalDtoProntoPago = dtoProntoPago.getValue();
                }
                dtoProntoPago.clear();
                if (current != null) {
                    current.setEsDtoProntoPago(false);
                    current.setDtoProntoPago(null);
                }
             // Usar binder.readBean en lugar de setBean para evitar bucles
                binder.readBean(current);
            } else {
            	if (originalDtoProntoPago != null) {
                    // Establecer la bandera antes de actualizar
                    updatingFromCode = true;
                    try {
                        dtoProntoPago.setValue(originalDtoProntoPago);
                        if (current != null) {
                            current.setDtoProntoPago(originalDtoProntoPago);
                            current.setEsDtoProntoPago(true);
                        }
                        binder.readBean(current);
                    } finally {
                        updatingFromCode = false; // Siempre resetear la bandera
                    }
                }
            }
        });
        
        // ALINEACIÓN HORIZONTAL: Usar HorizontalLayout para organizar en filas
        HorizontalLayout fila1 = new HorizontalLayout(modalidad, coeficiente, esDtoProntoPago, dtoProntoPago, esMesesCompletos);
        fila1.setSpacing(true);
        fila1.setAlignItems(Alignment.END); // Alinear al bottom para que se vea mejor
        
        add(fila1, buildBotonera());
        bindFields();
    }

    @Override
    protected void bindFields() {
    	binder.forField(modalidad)
    	.asRequired("La modalidad es obligatoria")
    	.bind(FormaDePago::getModalidad, FormaDePago::setModalidad);

    	binder.forField(coeficiente)
    	.asRequired("El coeficiente es obligatorio")
    	.bind(FormaDePago::getCoeficiente, FormaDePago::setCoeficiente);

    	binder.forField(esDtoProntoPago)
    	.bind(FormaDePago::getEsDtoProntoPago, FormaDePago::setEsDtoProntoPago);

    	// BINDING con validadores que solo aplican cuando el checkbox está activo.
        // Observación: consideramos inválido NULL o ZERO cuando el checkbox es true.
        binder.forField(dtoProntoPago)
            .withValidator(value -> {
                // No validar cuando el descuento no aplica
                if (!Boolean.TRUE.equals(esDtoProntoPago.getValue())) {
                    return true;
                }
                // Cuando aplica, value no puede ser null ni cero
                if (value == null) return false;
                return value.compareTo(BigDecimal.ZERO) > 0; // >0 (cero inválido)
            }, "El descuento por pronto pago es requerido y no puede ser cero")
            .withValidator(value -> {
                if (!Boolean.TRUE.equals(esDtoProntoPago.getValue())) {
                    return true;
                }
                if (value == null) return false;
                return value.compareTo(new BigDecimal("100")) <= 0;
            }, "El descuento por pronto pago no puede ser mayor a 100")
            .withValidator(value -> {
                if (!Boolean.TRUE.equals(esDtoProntoPago.getValue())) {
                    return true;
                }
                if (value == null) return false;
                return value.scale() <= 2;
            }, "No puede tener más de 2 decimales")
            .bind(FormaDePago::getDtoProntoPago, FormaDePago::setDtoProntoPago);
    	
    	
    	binder.forField(esMesesCompletos)
    	.bind(FormaDePago::getEsMesesCompletos, FormaDePago::setEsMesesCompletos);
    }

    private void cargarCoeficientes() {
    	try {
            CoeficienteUseCase coeficienteService = 
                SpringContextHelper.getBean(CoeficienteUseCase.class);
            setCoeficientes(coeficienteService.findAll());
        } catch (Exception e) {
            System.err.println("Error al cargar coeficientes: " + e.getMessage());
            setCoeficientes(new ArrayList<>());
        }
	}
    
    // Nuevo método para configurar los coeficientes disponibles
    public void setCoeficientes(List<Coeficiente> coeficientes) {
        coeficiente.setItems(coeficientes);
        coeficiente.setItemLabelGenerator(c -> 
            c != null ? c.getDescripcion() + " (" + c.getCoeficiente() + "x, " + c.getCuotas() + " cuotas)" : "");
    }
    
    @Override
    protected void setFormValues(FormaDePago entity) {
        if (entity == null) {
            modalidad.clear();
            coeficiente.clear();
            esDtoProntoPago.clear();
            dtoProntoPago.clear();
            esMesesCompletos.clear();
            originalDtoProntoPago = null;
            return;
        }
        modalidad.setValue(entity.getModalidad());
        coeficiente.setValue(entity.getCoeficiente());
        esDtoProntoPago.setValue(entity.getEsDtoProntoPago());
        
        // Guardar el valor original
        originalDtoProntoPago = entity.getDtoProntoPago();
        dtoProntoPago.setValue(entity.getDtoProntoPago());
        
        esMesesCompletos.setValue(entity.getEsMesesCompletos());
    }

    @Override
    protected void updateCurrentFromBinder() {
        // Todos los campos están bindados menos este que lo hago manual
    	// Cuando se guarda, actualizar el valor temporal
        if (current != null && current.getDtoProntoPago() != null) {
        	originalDtoProntoPago = current.getDtoProntoPago();
        }
    }

}
