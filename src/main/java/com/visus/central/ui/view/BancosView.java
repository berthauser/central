package com.visus.central.ui.view;

import java.util.stream.Collectors;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.visus.central.domain.model.Banco;
import com.visus.central.domain.port.in.BancoUseCase;
import com.visus.central.ui.component.CentralLayout;

@Route(value = "bancos", layout = CentralLayout.class)
@PageTitle("Consulta de Bancos")
public class BancosView extends VerticalLayout {
	
	private static final long serialVersionUID = 1L;

	private final BancoUseCase service;
    private final Grid<Banco> grid = new Grid<>(Banco.class, false);
    private final TextField filtro = new TextField();
    private final Button inicio = new Button("Inicio", new Icon(VaadinIcon.HOME));
    

    public BancosView(BancoUseCase service) {
        this.service = service;
        configurarVista();
        configurarFiltro();
        configurarGrilla();
        actualizarLista();
    }
    
    private void configurarVista() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);
        
        filtro.addClassName("campo-estilo-imagen");
        filtro.setPlaceholder("Buscar por nombre...");
        filtro.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        filtro.setClearButtonVisible(true);
        
        inicio.addClassName("btn-volver-home");
        inicio.addClickListener(_ -> UI.getCurrent().navigate(""));
//        
//        Button titulo = new Button("Consulta de Bancos");
////        Button titulo = new Button("Consulta de Bancos", new Icon(VaadinIcon.BANK));
//        titulo.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
//        titulo.setEnabled(false);
        
        // Layout horizontal para acciones (botones arriba)
//        HorizontalLayout acciones = new HorizontalLayout(titulo, filtro, inicio);
        HorizontalLayout acciones = new HorizontalLayout(filtro, inicio);
        acciones.setAlignItems(Alignment.CENTER);
        acciones.setPadding(false);
        acciones.setSpacing(true);
        acciones.setWidthFull();
        acciones.setJustifyContentMode(JustifyContentMode.START); // 👈 Alinear a la izquierda
        
        grid.addClassName("grid-documentacion-dark");
        grid.setSizeFull();
        
        add(acciones, grid);
        setFlexGrow(1, grid);
    }
    
    private void configurarFiltro() {
        filtro.addValueChangeListener(_ -> actualizarLista());
    }

    private void configurarGrilla() {
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setColumnReorderingAllowed(true);
        grid.setHeight("400px");
        
        // 👇 Columnas con nombres claros
        grid.addColumn(Banco::getNombre)
        .setHeader("NOMBRE")
        .setResizable(true);
        
        grid.addColumn(Banco::getIdBcoCen)
        .setHeader("ID BCO CENTRAL")
        .setResizable(true);
        
    }
    
    private void actualizarLista() {
        String texto = filtro.getValue();
        if (texto == null || texto.isBlank()) {
            grid.setItems(service.findAll());
        } else {
            grid.setItems(service.findAll().stream()
                .filter(b -> b.getNombre() != null && 
                       b.getNombre().toLowerCase().contains(texto.toLowerCase()))
                .collect(Collectors.toList()));
        }
    }
    

}
