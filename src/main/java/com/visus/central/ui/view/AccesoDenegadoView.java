package com.visus.central.ui.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("acceso-denegado")
@PageTitle("Acceso Denegado")
public class AccesoDenegadoView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	public AccesoDenegadoView() {
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);
		setHeightFull();

		Icon lockIcon = VaadinIcon.LOCK.create();
		lockIcon.setSize("64px");
		lockIcon.setColor("var(--lumo-error-color)");

		H2 titulo = new H2("Acceso Denegado");
		Span mensaje = new Span("No tienes permisos suficientes para acceder a esta secci\u00f3n.");
		mensaje.getStyle().set("color", "var(--lumo-secondary-text-color)");

		Button volver = new Button("Volver al inicio", new Icon(VaadinIcon.HOME));
		volver.addClassName("btn-volver-home");
		volver.addClickListener(_ -> UI.getCurrent().navigate(""));

		add(lockIcon, titulo, mensaje, volver);
	}
}
