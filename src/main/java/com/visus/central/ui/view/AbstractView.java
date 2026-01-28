package com.visus.central.ui.view;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.visus.central.domain.port.in.CrudUseCase;
import com.visus.central.ui.component.AbstractForm;

public abstract class AbstractView<T> extends VerticalLayout {
	
	private static final long serialVersionUID = 1L;
	
	private List<T> itemsList = new ArrayList<>();
	protected final boolean useOptimisticUpdate;
	protected final CrudUseCase<T> service;
    protected final Grid<T> grid = new Grid<>();
    protected final AbstractForm<T> form;
    protected TextField filtro;
    protected Button nuevo;
    
    // NUEVO: Flag para diferir carga de datos
    protected boolean deferInitialDataLoad = false;
    
    // Constructor para datos pequeños (optimista)
    protected AbstractView(String titulo, CrudUseCase<T> service) {
        this(titulo, service, true); // por defecto: optimista
    }
    
    // Constructor flexible
    protected AbstractView(String titulo, CrudUseCase<T> service, boolean useOptimisticUpdate) {
        this.service = service;
        this.useOptimisticUpdate = useOptimisticUpdate;
        this.form = buildForm();

        setPadding(true);
        setSpacing(false);

        add(buildHeader(titulo));
        configurarGrilla();
        
        VerticalLayout contenido = new VerticalLayout(grid, form);
        contenido.setWidthFull();
        contenido.setSpacing(true);
        contenido.setPadding(true);
        contenido.setFlexGrow(1, grid); // el grid crece cuando el form está oculto
        contenido.setFlexGrow(0, form); // el form no empuja al grid

        form.setVisible(false); // oculto por defecto
        grid.setHeight("500px"); // altura inicial razonable

        add(contenido);
        
        // MODIFICADO: Solo actualizar si no se difirió la carga
        if (!deferInitialDataLoad) {
            actualizarGrilla(); // inicializa según la estrategia
        }
        
        form.setOnCancel(() -> {
            form.setVisible(false);
            grid.setHeight("500px"); // restaura altura original
        });

        form.setOnSave(model -> {
            guardar(model);
            grid.setHeight("500px");
        });

        form.setOnDelete(model -> {
            eliminar(model);
            grid.setHeight("500px");
        });
        
        form.setVisible(false);
    }
    
    // NUEVO: Método para diferir carga de datos
    protected void deferDataLoading() {
        this.deferInitialDataLoad = true;
        // Establecer un grid vacío inicialmente
        grid.setItems(new ArrayList<>());
    }

    protected HorizontalLayout buildHeader(String textoNuevo) {
        nuevo = new Button(textoNuevo, new Icon(VaadinIcon.PLUS));
        nuevo.addClassName("btn-nuevo");
        nuevo.addClickListener(_ -> {
            form.clearForm();
            form.setVisible(true);
        });

        filtro = new TextField();
        filtro.setPlaceholder("Buscar por nombre...");
        filtro.setClearButtonVisible(true);
        filtro.addClassName("campo-estilo-imagen");
        filtro.addValueChangeListener(e -> aplicarFiltro(e.getValue()));

        Button inicio = new Button("Inicio", new Icon(VaadinIcon.HOME));
        inicio.addClassName("btn-volver-home");
        inicio.addClickListener(_ -> UI.getCurrent().navigate(HomeView.class));

        HorizontalLayout header = new HorizontalLayout(nuevo, filtro, inicio);
        header.addClassName("header-base");
        header.add(nuevo, filtro, inicio);
        header.setAlignItems(Alignment.CENTER);
        return header;
    }

    // Grilla institucional  
    private void configurarGrilla() {
        grid.setSizeFull();
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    	grid.setColumnReorderingAllowed(true);
    	grid.setHeight("400px");
    	grid.addClassName("grid-documentacion-dark");
        buildColumns(grid);
        grid.asSingleSelect().addValueChangeListener(event -> {
            T seleccionado = event.getValue();
            if (seleccionado != null) {
            	form.addClassName("form-panel");
            	form.setEntity(seleccionado);
                form.setVisible(true);
                grid.setHeight("350px"); // reduce altura al mostrar el form
            }
        });
    }
    
    protected void aplicarFiltro(String criterio) {
        grid.setItems(service.findAll()); // por defecto: sin filtro
    }
    
    protected abstract List<Grid.Column<T>> buildColumns(Grid<T> grid);
    
    protected abstract AbstractForm<T> buildForm();
    
    protected void guardar(T model) {
        showLoading("Guardando...");
        try {
            T modelGuardado = service.save(model);
            
            if (useOptimisticUpdate) {
                actualizarGrillaOptimista(modelGuardado);
            } else {
                actualizarGrilla(); // recarga completa
            }
            
            form.setVisible(false);
            showSuccess("Registro guardado");
        } finally {
            hideLoading();
        }
    }
    
    protected void eliminar(T model) {
   		confirmarEliminacion(() -> {
            showLoading("Eliminando...");
            try {
                service.deleteById(getId(model));
                
                if (useOptimisticUpdate) {
                    eliminarOptimista(model);
                } else {
                    actualizarGrilla();
                }
                
                form.setVisible(false);
                showSuccess("Registro eliminado");
            } finally {
                hideLoading();
            }
        });
    }
    
    protected void eliminarOptimista(T model) {
        Integer idAEliminar = getId(model);
        itemsList.removeIf(item -> getId(item).equals(idAEliminar));
        grid.setItems(itemsList);
    }

    protected Integer getId(T model) {
        try {
            return (Integer) model.getClass().getMethod("getId").invoke(model);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo obtener el ID del modelo", e);
        }
    }
    
    protected void confirmarEliminacion(Runnable onConfirm) {
        Dialog dialogo = new Dialog();
        dialogo.setHeaderTitle("Confirmar eliminación");
        
        Span mensaje = new Span("El registro y sus datos relacionados se eliminarán. ¿Desea continuar?");
        mensaje.getStyle().set("font-weight", "500");
        
        Button aceptar = new Button("Eliminar", _ -> {
            onConfirm.run();
            dialogo.close();
        });
        Button cancelar = new Button("Cancelar", _ -> dialogo.close());
        
        aceptar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        
        dialogo.getFooter().add(aceptar, cancelar);
        dialogo.add(mensaje);
        dialogo.open();
    }
    
    protected void actualizarGrilla() {
        if (useOptimisticUpdate) {
            itemsList = service.findAll();
            grid.setItems(itemsList);
        } else {
            // Para datos grandes: usar DataProvider con paginación
            grid.setItems(service.findAll()); // o mejor: DataProvider paginado
        }
    }
    
    private void actualizarGrillaOptimista(T modelGuardado) {
        Integer idGuardado = getId(modelGuardado);
        
        // Buscar si el modelo ya existe en la lista
        boolean existe = itemsList.stream()
            .anyMatch(item -> getId(item).equals(idGuardado));

        if (existe) {
            // Modificación: reemplazar el item existente
            itemsList.replaceAll(item -> 
                getId(item).equals(idGuardado) ? modelGuardado : item
            );
        } else {
            // Alta: agregar al final
            itemsList.add(modelGuardado);
        }

        // Actualizar la grilla con la lista local
        grid.setItems(itemsList);
    }
    
    // Métodos auxiliares
    private Notification loadingNotification;

    protected void showLoading(String message) {
        loadingNotification = Notification.show(message, -1, Notification.Position.MIDDLE);
        loadingNotification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
    }

    protected void hideLoading() {
        if (loadingNotification != null) {
            loadingNotification.close();
            loadingNotification = null;
        }
    }

    protected void showSuccess(String message) {
        Notification.show(message, 3000, Notification.Position.BOTTOM_CENTER)
            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

}
