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
import com.visus.central.domain.model.Domicilio;
import com.visus.central.domain.model.Localidad;
import com.visus.central.domain.port.in.LocalidadUseCase;
import com.visus.central.infraestructure.util.SpringContextHelper;

public class DomicilioSubForm extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	
	private Grid<Domicilio> grid = new Grid<>(Domicilio.class, false);
    private List<Domicilio> domicilios = new ArrayList<>();
    
    public interface DomListener {
        void onDomChange();
    }
    
    private List<DomListener> domListeners = new ArrayList<>();

    public DomicilioSubForm() {
    	init();
    }
    
    private void init() {
		
    	grid.addClassName("grid-documentacion-dark");
        
    	grid.addColumn(Domicilio::getCalle)
        	.setHeader("Calle")
        	.setResizable(true)
        	.setSortable(true);
        grid.addColumn(Domicilio::getNumero)
        	.setHeader("Número")
        	.setResizable(true)
        	.setSortable(true);
        grid.addColumn(Domicilio::getBarrio)
        	.setHeader("Barrio")
        	.setResizable(true)
        	.setSortable(true);
        grid.addColumn(Domicilio::getManzana)
        	.setHeader("Manzana")
        	.setResizable(true)
        	.setSortable(true);
        grid.addColumn(Domicilio::getCasa)
        	.setHeader("Casa")
        	.setResizable(true)
        	.setSortable(true);
        grid.addColumn(Domicilio::getSector)
        	.setHeader("Sector")
        	.setResizable(true)
        	.setSortable(true);
        grid.addColumn(Domicilio::getOficina)
        	.setHeader("Oficina")
        	.setResizable(true)
        	.setSortable(true);
        grid.addColumn(Domicilio::getLote)
        	.setHeader("Lote")
        	.setResizable(true)
        	.setSortable(true);
        grid.addColumn(Domicilio::getTipoDomicilio)
        	.setHeader("Tipo de Domicilio")
        	.setResizable(true)
        	.setSortable(true);
        grid.setItems(domicilios);

        grid.setHeight("224px"); // ≈ 4 filas + header
        grid.setAllRowsVisible(false); // asegura scroll si hay más filas
        
        Grid.Column<Domicilio> _ = grid.addComponentColumn(domicilio -> {
            HorizontalLayout acciones = new HorizontalLayout();
            
            // Botón Editar - solo ícono
            Button editar = new Button(VaadinIcon.EDIT.create());
            editar.addClassName("btn-accion-icono");
            editar.addClickListener(_ -> abrirFormulario(domicilio));
            editar.setTooltipText("Editar"); // Tooltip para accesibilidad
            
            // Botón Eliminar - solo ícono
            Button eliminar = new Button(VaadinIcon.TRASH.create());
            eliminar.addClassName("btn-accion-icono");
            eliminar.addClickListener(_ -> confirmarEliminacion(domicilio));
            eliminar.setTooltipText("Eliminar"); // Tooltip para accesibilidad
            
            acciones.add(editar, eliminar);
            acciones.setSpacing(true);
            acciones.setPadding(false);
            return acciones;
        }).setHeader("Acciones").setWidth("100px"); // ancho más compacto
        
        Button agregar = new Button("Agregar Domicilio", VaadinIcon.PLUS.create(), _ -> abrirFormulario());
        agregar.addClassName("btn-nuevo");
        
        Details detallesDomicilios = new Details("Domicilios Ingresados", grid);
        detallesDomicilios.addClassName("domicilios-details");
        detallesDomicilios.setOpened(false); // cerrado por defecto
        detallesDomicilios.setWidthFull();

        add(agregar, detallesDomicilios);
		
	}

	// Método para crear nuevo domicilio
    private void abrirFormulario() {
        abrirFormulario(null);
    }
    
    // Método sobrecargado para editar domicilio existente
    private void abrirFormulario(Domicilio domicilioExistente) {
        DomicilioForm form = new DomicilioForm();
        Dialog dialog = new Dialog(form);
        
        dialog.setHeaderTitle(domicilioExistente == null ? "Agregar Domicilio" : "Editar Domicilio");
        dialog.setDraggable(true);
        dialog.setModal(true);
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);
        
     // Usa la clase utilitaria (mucho más simple y confiable)
        LocalidadUseCase localidadUseCase = SpringContextHelper.getBean(LocalidadUseCase.class);
        List<Localidad> localidades = localidadUseCase.listar();
        form.setLocalidades(localidades);
        
     // Luego cargar el domicilio (si existe)
        if (domicilioExistente != null) {
            form.setDomicilio(domicilioExistente);
        }
        
     // ESTO ES OBLIGATORIO
        form.setOnSave(domicilio -> {
            if (domicilioExistente == null) {
                domicilios.add(domicilio);
            } else {
                int index = domicilios.indexOf(domicilioExistente);
                if (index >= 0) {
                    domicilios.set(index, domicilio);
                }
            }
            grid.setItems(domicilios);
            dialog.close();
            // Notificar cambios
            domListeners.forEach(DomListener::onDomChange);
        });

        form.setOnValidate(domicilio -> {
            long count = domicilios.stream()
                .filter(d -> d.getTipoDomicilio() == domicilio.getTipoDomicilio())
                .count();
            
            if (domicilioExistente != null && 
                domicilioExistente.getTipoDomicilio() == domicilio.getTipoDomicilio()) {
                count--;
            }
            
            if (count > 0) {
                return "Ya existe un domicilio de tipo '" + 
                       domicilio.getTipoDomicilio() + "'. Solo se permite uno por tipo.";
            }
            return null;
        });
        
        form.setOnCancel(dialog::close);

        dialog.addOpenedChangeListener(event -> {
            if (event.isOpened()) {
                form.focusCalle();
            }
        });

        dialog.open();
    }

    public List<Domicilio> getDomicilios() {
        return domicilios;
    }
    
    private void confirmarEliminacion(Domicilio domicilio) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Confirmar eliminación");
        
        Span mensaje = new Span("¿Desea eliminar el domicilio " + 
            domicilio.getTipoDomicilio() + "?");
        mensaje.getStyle().set("font-weight", "500");
        
        Button aceptar = new Button("Eliminar", _ -> {
        	
            // Eliminar de la lista local
            domicilios.remove(domicilio);
            grid.setItems(domicilios);
            dialog.close();
            // Notificar cambios
            domListeners.forEach(DomListener::onDomChange);
            System.out.println("✅ Domicilio eliminado de lista - Total: " + domicilios.size());
        });
        
        Button cancelar = new Button("Cancelar", _ -> dialog.close());
        
        aceptar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        
        dialog.getFooter().add(aceptar, cancelar);
        dialog.add(mensaje);
        dialog.open();
    }
    
    public void setDomicilios(List<Domicilio> domicilios) {
        this.domicilios = domicilios != null ? new ArrayList<>(domicilios) : new ArrayList<>();
        grid.setItems(this.domicilios);
        System.out.println("📝 DomicilioSubForm.setDomicilios() - Cargados " + this.domicilios.size() + " domicilios");
    }
    
    public void addDomListener(DomListener listener) {
        domListeners.add(listener);
    }

}
