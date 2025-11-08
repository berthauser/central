package com.visus.central.ui.component;

import java.util.ArrayList;
import java.util.List;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.visus.central.domain.model.GrupoFam;

public class GrupoFamSubForm extends VerticalLayout {

    private static final long serialVersionUID = 1L;
    
    private Grid<GrupoFam> grid = new Grid<>(GrupoFam.class, false);
    private List<GrupoFam> grupoFamList = new ArrayList<>();
    
    public interface DomListener {
        void onDomChange();
    }
    
    private List<DomListener> domListeners = new ArrayList<>();
    
    public GrupoFamSubForm() {
    	init();
    }
    
    private void init() {
    	
    	grid.addClassName("grid-documentacion-dark");

    	grid.addColumn(GrupoFam::getNombre)
    		.setHeader("Nombre")
    		.setResizable(true)
    		.setSortable(true);
    	grid.addColumn(GrupoFam::getParentesco)
    		.setHeader("Parentesco")
    		.setResizable(true)
    		.setSortable(true);
    	grid.addColumn(GrupoFam::getDocumento)
    		.setHeader("Documento")
    		.setResizable(true)
    		.setSortable(true);
    	grid.addColumn(GrupoFam::getNumero)
    		.setHeader("Número")
    		.setResizable(true)
    		.setSortable(true);
    	grid.addColumn(GrupoFam::getEstado)
    		.setHeader("Estado")
    		.setResizable(true)
    		.setSortable(true);
    	 
    	grid.setHeight("224px"); // ≈ 4 filas + header
        grid.setAllRowsVisible(false); // asegura scroll si hay más filas

    	Grid.Column<GrupoFam> _ = grid.addComponentColumn(grupoFamiliar -> {
            HorizontalLayout acciones = new HorizontalLayout();
            
            // Botón Editar - solo ícono
            Button editar = new Button(VaadinIcon.EDIT.create());
            editar.addClassName("btn-accion-icono");
            editar.addClickListener(_ -> abrirFormulario(grupoFamiliar));
            editar.setTooltipText("Editar"); // Tooltip para accesibilidad
            
            // Botón Eliminar - solo ícono
            Button eliminar = new Button(VaadinIcon.TRASH.create());
            eliminar.addClassName("btn-accion-icono");
            eliminar.addClickListener(_ -> confirmarEliminacion(grupoFamiliar));
            eliminar.setTooltipText("Eliminar"); // Tooltip para accesibilidad
            
            acciones.add(editar, eliminar);
            acciones.setSpacing(true);
            acciones.setPadding(false);
            return acciones;
        }).setHeader("Acciones").setWidth("100px"); // ancho más compacto


    	Button agregar = new Button("Agregar Familiar", VaadinIcon.PLUS.create(), _ -> abrirFormulario());
    	agregar.addClassName("btn-nuevo");

    	Details familiares = new Details("Familiares Ingresados", grid);
    	familiares.addClassName("domicilios-details");
    	familiares.setOpened(false); // cerrado por defecto
    	familiares.setWidthFull();

    	add(agregar, familiares);
    	
	}

	private void abrirFormulario() {
        abrirFormulario(null);
    }
    
    private void abrirFormulario(GrupoFam grupoFamExistente) {
    	GrupoFamForm form = new GrupoFamForm();
        Dialog dialog = new Dialog(form);
        
        dialog.setHeaderTitle(grupoFamExistente == null ? "Agregar Familiar" : "Editar Familiar");
        dialog.setDraggable(true);
        dialog.setModal(true);
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);
        
        if (grupoFamExistente != null) {
            form.setGrupoFamiliar(grupoFamExistente);
        }
        
        form.setOnSave(grupoFam -> {
            if (grupoFamExistente == null) {
                grupoFamList.add(grupoFam);
            } else {
                int index = grupoFamList.indexOf(grupoFamExistente);
                if (index >= 0) {
                    grupoFamList.set(index, grupoFam);
                }
            }
        
            grid.setItems(grupoFamList);
            dialog.close();
            // Notificar cambios
            domListeners.forEach(DomListener::onDomChange);
            
            System.out.println("✅ Familiar agregado/actualizado en lista - Total: " + grupoFamList.size());
        });
        
        form.setOnCancel(dialog::close);

        dialog.addOpenedChangeListener(event -> {
            if (event.isOpened()) {
                form.focusNombre();
            }
        });

        dialog.open();
    }
    
    private void confirmarEliminacion(GrupoFam grupoFam) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Confirmar eliminación");

        Span mensaje = new Span("¿Desea eliminar el familiar \"" + 
            grupoFam.getNombre() + "\"?");
        mensaje.getStyle().set("font-weight", "500");

        Button aceptar = new Button("Eliminar", _ -> {
            // Eliminar de la lista local
            grupoFamList.remove(grupoFam);
            grid.setItems(grupoFamList);
            dialog.close();
            // Notificar cambios
            domListeners.forEach(DomListener::onDomChange);
            System.out.println("✅ Familiar eliminado de lista - Total: " + grupoFamList.size());
        });
        
        Button cancelar = new Button("Cancelar", _ -> dialog.close());

        aceptar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        dialog.getFooter().add(aceptar, cancelar);
        dialog.add(mensaje);
        dialog.open();
    }
    
    public void addDomListener(DomListener listener) {
        domListeners.add(listener);
    }
    
    public List<GrupoFam> getGrupoFam() {
        return grupoFamList;
    }
    
    public void setGrupoFam(List<GrupoFam> grupoFam) {
        this.grupoFamList = grupoFam != null ? new ArrayList<>(grupoFam) : new ArrayList<>();
        grid.setItems(this.grupoFamList);
    }
    

}
