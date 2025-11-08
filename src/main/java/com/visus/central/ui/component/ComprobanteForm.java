package com.visus.central.ui.component;

import java.util.Arrays;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.visus.central.domain.model.Comprobante;
import com.visus.central.infraestructure.persistence.entity.JpaComprobanteEntity.Columna;
import com.visus.central.infraestructure.persistence.entity.JpaComprobanteEntity.NombreCorto;

public class ComprobanteForm extends AbstractForm<Comprobante> {

    private static final long serialVersionUID = 1L;
    
    private TextField nombreLargo = new TextField("Nombre Largo");
    private ComboBox<NombreCorto> nombreCorto = new ComboBox<>("Nombre Corto");
    private IntegerField numeroInicial = new IntegerField("Número Inicial");
    private IntegerField sucursal = new IntegerField("Sucursal");
    private ComboBox<Columna> columna = new ComboBox<>("Columna");

    public ComprobanteForm() {
        super(Comprobante.class);
        buildLayout();
    }
    
    private void buildLayout() {
        String campoStyle = "campo-estilo-imagen";
        
        nombreLargo.addClassName(campoStyle);
        nombreLargo.setRequired(true);
        nombreLargo.setWidth("400px");
        nombreLargo.setMaxLength(60);
        
        nombreCorto.addClassName(campoStyle);
        nombreCorto.setRequired(true);
        nombreCorto.setItems(Arrays.asList(NombreCorto.values()));
        nombreCorto.setItemLabelGenerator(NombreCorto::getLabel);
        nombreCorto.setWidth("150px");
        
        numeroInicial.addClassName(campoStyle);
        numeroInicial.setRequired(true);
        numeroInicial.setWidth("150px");
        numeroInicial.setMin(1);
        numeroInicial.setMax(999999);
        
        sucursal.addClassName(campoStyle);
        sucursal.setRequired(true);
        sucursal.setWidth("150px");
        sucursal.setMin(1);
        sucursal.setMax(9999);
        
        columna.addClassName(campoStyle);
        columna.setRequired(true);
        columna.setItems(Arrays.asList(Columna.values()));
        columna.setItemLabelGenerator(Columna::getLabel);
        columna.setWidth("150px");
        
        // ALINEACIÓN HORIZONTAL: Usar HorizontalLayout para organizar en filas
        HorizontalLayout fila1 = new HorizontalLayout(nombreLargo, nombreCorto, numeroInicial, sucursal, columna);
        fila1.setSpacing(true);
        fila1.setAlignItems(Alignment.END); // Alinear al bottom para que se vea mejor
        
        add(fila1, buildBotonera());
        bindFields();
        
    }
    
    @Override
    protected void bindFields() {
        binder.forField(nombreLargo)
            .asRequired("El nombre largo es obligatorio")
            .bind(Comprobante::getNombreLargo, Comprobante::setNombreLargo);
            
        binder.forField(nombreCorto)
            .asRequired("El nombre corto es obligatorio")
            .bind(Comprobante::getNombreCorto, Comprobante::setNombreCorto);
            
        binder.forField(numeroInicial)
            .asRequired("El número inicial es obligatorio")
            .withValidator(value -> value != null && value >= 1,
                "El número inicial debe ser mayor o igual a 1")
            .bind(Comprobante::getNumeroInicial, Comprobante::setNumeroInicial);
            
        binder.forField(sucursal)
            .asRequired("La sucursal es obligatoria")
            .withValidator(value -> value != null && value >= 1,
                "La sucursal debe ser mayor o igual a 1")
            .bind(Comprobante::getSucursal, Comprobante::setSucursal);
            
        binder.forField(columna)
            .asRequired("La columna es obligatoria")
            .bind(Comprobante::getColumna, Comprobante::setColumna);
    }
    
    @Override
    protected void setFormValues(Comprobante entity) {
        if (entity == null) {
            nombreLargo.clear();
            nombreCorto.clear();
            numeroInicial.clear();
            sucursal.clear();
            columna.clear();
            return;
        }
        nombreLargo.setValue(entity.getNombreLargo() != null ? entity.getNombreLargo() : "");
        nombreCorto.setValue(entity.getNombreCorto());
        numeroInicial.setValue(entity.getNumeroInicial());
        sucursal.setValue(entity.getSucursal());
        columna.setValue(entity.getColumna());
    }

    @Override
    protected void updateCurrentFromBinder() {
        // Todos los campos están bindados
    }

}
