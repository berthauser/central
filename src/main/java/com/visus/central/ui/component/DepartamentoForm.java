package com.visus.central.ui.component;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.visus.central.domain.model.Departamento;
import com.visus.central.domain.model.Provincia;

public class DepartamentoForm extends AbstractForm<Departamento> {

	private static final long serialVersionUID = 1L;

	private TextField nombre = new TextField("Nombre");
	private final ComboBox<Provincia> provincia = new ComboBox<>("Provincia");

	public DepartamentoForm() {
		super(Departamento.class);
		
		nombre.addClassName("campo-estilo-imagen");
		provincia.addClassName("campo-estilo-imagen");

		provincia.setItems(Provincia.values());
		provincia.setItemLabelGenerator(Provincia::toString); // usa el label si redefinís toString()
		
		HorizontalLayout acciones = new HorizontalLayout(crear, modificar, eliminar, volver);
        acciones.setSpacing(true);
        acciones.setAlignItems(Alignment.START);

        add(nombre, provincia, acciones);
        
        bindFields();

	}

	@Override
    protected void bindFields() {
        binder.forField(nombre)
            .asRequired("El nombre es obligatorio")
            .bind(Departamento::getNombre, Departamento::setNombre);

        binder.forField(provincia)
            .asRequired("La provincia es obligatoria")
            .bind(Departamento::getProvincia, Departamento::setProvincia);
    }
	
	@Override
    protected void setFormValues(Departamento entity) {
		if (entity == null) {
	        nombre.clear();
	        provincia.clear();
	        return;
	    }

	    if (nombre == null || provincia == null) {
	        System.out.println("❌ Campo visual no inicializado");
	        return;
	    }

	    nombre.setValue(entity.getNombre() != null ? entity.getNombre() : "");
	    provincia.setValue(entity.getProvincia());
        
    }

    @Override
    protected void updateCurrentFromBinder() {
        // Si tenés campos no bindables, actualizalos aquí
        // En este caso, todos están bindados, así que no hace falta nada
    }

}