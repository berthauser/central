package com.visus.central.ui.view;

import com.vaadin.flow.component.html.H2;
//import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.visus.central.ui.component.CentralLayout;

@Route(value = "/", layout = CentralLayout.class)
@PageTitle("Inicio")
public class HomeView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	public HomeView() {
        add(new H2("Bienvenido a Visus Central"));
        // Podés agregar navegación o contenido adicional
    }
    
}
