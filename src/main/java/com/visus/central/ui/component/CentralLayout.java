package com.visus.central.ui.component;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.visus.central.domain.port.in.HealthCheckUseCase;
import com.visus.central.domain.port.in.HealthCheckUseCase.DatabaseHealthStatus;
import com.visus.central.infraestructure.security.SecurityUtils;
import com.visus.central.ui.view.BancosView;
import com.visus.central.ui.view.ClienteView;
import com.visus.central.ui.view.CoeficienteView;
import com.visus.central.ui.view.ComprobanteView;
import com.visus.central.ui.view.ConceptoView;
import com.visus.central.ui.view.DepartamentoView;
import com.visus.central.ui.view.FormaDePagoView;
import com.visus.central.ui.view.LocalidadView;
import com.visus.central.ui.view.ProveedorView;
import com.visus.central.ui.view.UsersView;
import com.visus.central.ui.view.VendedorView;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;

@PageTitle("Visus Central")
public class CentralLayout extends AppLayout implements AfterNavigationObserver {

	private static final long serialVersionUID = 1L;

//	private String currentRoute = "/"; // valor inicial
	private Span databaseStatus = new Span(); // ← Nuevo elemento para el estado de la BD
	private Instant inicioSesion = Instant.now(); // marca el inicio de sesión
	
	private HorizontalLayout footer;

	private final HealthCheckUseCase healthCheckUseCase; // ← Inyectar el use case

	public CentralLayout(HealthCheckUseCase healthCheckUseCase) { // ← Inyectar por constructor
		this.healthCheckUseCase = healthCheckUseCase;

		setPrimarySection(Section.DRAWER);
		addToNavbar(true, createHeader());
		addToDrawer(createDrawer());
		setDrawerOpened(true); // 
		getElement().getStyle().set("--vaadin-app-layout-drawer-width", "260px");

		// Verificar el estado de la base de datos al inicializar
		checkDatabaseHealth();
	}

	@Override
	public void setContent(Component content) {
		super.setContent(wrapWithFooter(content));
	}

	private Component createHeader() {
		H3 appName = new H3("Visus Central");
		Span nombreApp = new Span(appName);
		nombreApp.addClassName("header-text");

		HorizontalLayout branding = new HorizontalLayout(new DrawerToggle(), nombreApp);
		branding.setAlignItems(FlexComponent.Alignment.CENTER);
		branding.setSpacing(false);

		Icon userIcon = VaadinIcon.USER.create();
		userIcon.setSize("24px");
		userIcon.addClassName("header-user");

		Span username = new Span(SecurityContextHolder.getContext().getAuthentication().getName());
		username.addClassName("header-user");

		Button logoutButton = new Button("Cerrar Sesión", _ -> {
			UI.getCurrent().getPage().setLocation("/logout");
		});

		logoutButton.getElement().setAttribute("theme", "small");
		logoutButton.addClassName("logout-button");

		HorizontalLayout userLayout = new HorizontalLayout(userIcon, username, logoutButton);
		userLayout.setAlignItems(FlexComponent.Alignment.CENTER);
		userLayout.setSpacing(true);

		HorizontalLayout header = new HorizontalLayout(branding, userLayout);
		header.setWidthFull();
		header.setHeightFull();
		header.setPadding(false);
		header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
		header.setHeight("55px");
		header.getStyle()
		.set("padding", "4px 12px")
		.set("font-size", "0.9em")
		.set("background-color", "#101b29") 
		.set("color", "white")
		.set("border-bottom", "1px solid #3a5f5f")
		.set("border-radius", "0 0 8px 8px") // ← Bordes redondeados solo abajo
		.set("margin-bottom", "2px"); // ← Separación mínima con el drawer

		return header;
	}

	private Component createDrawer() {
		VerticalLayout drawer = new VerticalLayout();
		drawer.setPadding(false);
		drawer.setSpacing(false);
		drawer.setSizeFull();
		drawer.getStyle()
		.set("padding", "0")
		.set("margin", "0")
		.set("background-color", "transparent")
		.set("color", "white")
		.set("overflow-x", "hidden")
		.set("width", "255px") 
		.set("min-width", "255px")
		.set("max-width", "255px")
		.set("border-radius", "0") // ← QUITAR BORDES REDONDEADOS DEL DRAWER
		.set("margin-top", "2px")
		.set("margin-bottom", "2px");

		VerticalLayout logoContainer = new VerticalLayout();
		logoContainer.setPadding(false);
		logoContainer.setSpacing(false);
		logoContainer.setMargin(false);
		logoContainer.setWidthFull();
		logoContainer.setHeight("53px");
		logoContainer.getStyle()
		.set("padding", "0")
		.set("background-color", "#101b29") 
		.set("border-bottom", "1px solid #3a5f5f")
		.set("display", "flex")
		.set("align-items", "center")
		.set("justify-content", "center")
		.set("border-radius", "0 0 8px 8px") // ← SOLO BORDES INFERIORES REDONDEADOS
		.set("margin-bottom", "2px");

		Image logo = new Image("/images/buldra.png", "");
		logo.setWidth("auto");
		logo.setHeight("40px");
		logo.getStyle()
		.set("border-radius", "4px")
		.set("object-fit", "contain")
		.set("margin", "0")
		.set("padding", "0");
		logoContainer.add(logo);

		VerticalLayout menu = createMenu();

		drawer.add(logoContainer, menu);
		drawer.setFlexGrow(1, menu);
		return drawer;
	}

	private VerticalLayout createMenu() {
		VerticalLayout menuLayout = new VerticalLayout();
		menuLayout.setPadding(false);
		menuLayout.setSpacing(false);
		menuLayout.setMargin(false);
		menuLayout.setWidthFull();
		menuLayout.getStyle()
		.set("background-color", "#101b29")
		.set("color", "white")
		.set("padding", "8px 0")
		.set("border-radius", "8px") // ← MANTENER BORDES REDONDEADOS
		.set("border", "1px solid #2a3f4f")
		.set("margin-top", "1px");

		// Enlace condicional a Usuarios
		if (SecurityUtils.isAdmin()) {
			System.out.println("El usuario es ADMIN");

			// Sección: Usuarios
			Details usuariosSection = createMenuSection(
					"Usuarios", 
					createMenuLink("Usuarios", UsersView.class, 25)
					);
			usuariosSection.getStyle().set("margin-bottom", "4px"); // ← ESPACIO ENTRE SECCIONES
			menuLayout.add(usuariosSection);
		}

		System.out.println("El usuario NO es ADMIN");

		// Sección: CLIENTES
		Details clientesSection = createMenuSection(
				"Clientes",
				createMenuLink("Actualización", ClienteView.class, 25),
				createMenuSpan("Listado", 25)
				);
		clientesSection.getStyle().set("margin-bottom", "4px");
		menuLayout.add(clientesSection);

		// Sección: PROVEEDORES
		Details proveedoresSection = createMenuSection(
				"Proveedores",
				createMenuLink("Actualización", ProveedorView.class, 25),
				createMenuSpan("Listado", 25)
				);
		proveedoresSection.getStyle().set("margin-bottom", "4px");
		menuLayout.add(proveedoresSection);

		// SECCIÓN: TABLAS BÁSICAS
		Details tablasBaseSection = createMenuSection(
				"Tablas Básicas",
				createMenuLink("Departamentos", DepartamentoView.class, 25),
				createMenuLink("Localidades", LocalidadView.class, 25),
				createMenuLink("Vendedores", VendedorView.class, 25),
				createMenuLink("Conceptos", ConceptoView.class, 25)
				);
		tablasBaseSection.getStyle().set("margin-bottom", "4px");
		menuLayout.add(tablasBaseSection);

		// Sección: ÁREA FINANCIERA
		Details areaFinancieraSection = createMenuSection(
				"Área Financiera",
				createMenuLink("Coeficientes", CoeficienteView.class, 25),
				createMenuLink("Formas de Pago", FormaDePagoView.class, 25),
				createMenuLink("Comprobantes", ComprobanteView.class, 25),
				createMenuLink("Consultar Bancos", BancosView.class, 25)
				);
		areaFinancieraSection.getStyle().set("margin-bottom", "4px");
		menuLayout.add(areaFinancieraSection);

		return menuLayout;
	}

	private Details createMenuSection(String title, Component... components) {
		VerticalLayout content = new VerticalLayout();
		content.setPadding(false);
		content.setSpacing(false);
		content.setMargin(false);
		content.setWidthFull();
		content.getStyle()
		.set("padding", "4px 0")
		.set("background-color", "transparent"); // ← Fondo transparente

		for (Component component : components) {
			content.add(component);
		}

		Div summaryDiv = new Div();
		summaryDiv.setText(title);
		summaryDiv.getStyle()
		.set("background-color", "transparent")
		.set("padding", "12px 16px")
		.set("font-weight", "600")
		.set("font-size", "14px")
		.set("color", "white")
		.set("cursor", "pointer")
		.set("user-select", "none")
		.set("margin", "0 8px")
		.set("width", "calc(100% - 16px)")
		.set("border-radius", "6px") // ← Bordes redondeados consistentes
		.set("transition", "all 0.2s ease");

		Details details = new Details(summaryDiv, content);
		details.setOpened(true);
		details.setWidthFull();

		details.getElement().getStyle()
		.set("border", "none")
		.set("border-radius", "0")
		.set("box-shadow", "none")
		.set("margin", "0")
		.set("padding", "0")
		.set("width", "100%")
		.set("background-color", "transparent"); // ← Fondo transparente

		summaryDiv.getElement().addEventListener("mouseenter", _ -> {
			summaryDiv.getStyle()
			.set("background-color", "#3a5f5f");
		});

		summaryDiv.getElement().addEventListener("mouseleave", _ -> {
			summaryDiv.getStyle()
			.set("background-color", "transparent");
		});

		return details;
	}

	private RouterLink createMenuLink(String text, Class<? extends Component> viewClass, int marginLeft) {
		RouterLink link = new RouterLink(text, viewClass);
		link.getStyle()
		.set("margin-left", marginLeft + "px")
		.set("font-size", "0.9em")
		.set("font-weight", "400")
		.set("color", "#e0e0e0")
		.set("text-decoration", "none")
		.set("padding", "10px 16px")
		.set("display", "block")
		.set("border-radius", "6px")
		.set("margin", "0 8px")
		.set("width", "calc(100% - " + (marginLeft + 16) + "px)")
		.set("box-sizing", "border-box")
		.set("transition", "all 0.2s ease")
		.set("background-color", "transparent"); // ← TRANSPARENTE POR DEFECTO

		// Efecto hover
		link.getElement().addEventListener("mouseenter", _ -> {
			link.getStyle()
			.set("background-color", "#3a5f5f")
			.set("color", "white");
		});

		link.getElement().addEventListener("mouseleave", _ -> {
			link.getStyle()
			.set("background-color", "transparent")
			.set("color", "#e0e0e0");
		});

		return link;

	};

	private Span createMenuSpan(String text, int marginLeft) {
		Span span = new Span(text);
		span.getStyle()
		.set("margin-left", marginLeft + "px")
		.set("font-size", "0.9em")
		.set("font-weight", "400")
		.set("color", "#a0a0a0")
		.set("padding", "10px 16px")
		.set("display", "block")
		.set("border-radius", "6px")
		.set("margin", "0 8px")
		.set("width", "calc(100% - " + (marginLeft + 16) + "px)")
		.set("box-sizing", "border-box")
		.set("background-color", "transparent"); // ← TRANSPARENTE
		return span;
	}

	private Component wrapWithFooter(Component view) {
	    VerticalLayout layout = new VerticalLayout();
	    layout.setSizeFull();
	    layout.setPadding(false);
	    layout.setSpacing(false);
	    layout.getStyle()
	        .set("background-color", "#0a121f")
	        .set("padding", "4px");

	    VerticalLayout contentWrapper = new VerticalLayout(view);
	    contentWrapper.setSizeFull();
	    contentWrapper.setPadding(false);
	    contentWrapper.setSpacing(false);
	    contentWrapper.getStyle().set("overflow", "auto");

	    layout.add(contentWrapper);

	    // Asegurar que el footer exista (aunque esté vacío por ahora)
	    if (footer == null) {
	        updateFooter("/", 0, "Cargando...");
	    }
	    layout.add(footer);

	    layout.setFlexGrow(1, contentWrapper);
	    return layout;
	}

	private void checkDatabaseHealth() {
		try {
			DatabaseHealthStatus status = healthCheckUseCase.checkDatabaseHealth();
			updateDatabaseStatusIndicator(status);
		} catch (Exception e) {
			// Manejo de errores mejorado
			databaseStatus.setText("BD: ERROR");
			databaseStatus.getStyle()
			.set("border-radius", "2px")
			.set("padding", "2px 2px 2px 2px")
			.set("background-color", "rgba(255, 165, 0, 0.2)")
			.set("color", "orange")
			.set("border", "1px solid orange");
			databaseStatus.getElement().setAttribute("title", 
					"Error al verificar: " + e.getMessage());

			// Log del error para debugging
			System.err.println("Error checking database health: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void updateDatabaseStatusIndicator(DatabaseHealthStatus status) {
		String statusText = "BD: " + (status.isConnected() ? "CONECTADA" : "DESCONECTADA");
		databaseStatus.setText(statusText);

		if (status.isConnected()) {
			databaseStatus.getStyle()
			.set("border-radius", "2px")
			.set("padding", "2px 2px 2px 2px")
			.set("background-color", "rgba(0, 255, 0, 0.2)") // Verde semitransparente
			.set("color", "lightgreen")
			.set("border", "1px solid lightgreen");
		} else {
			databaseStatus.getStyle()
			.set("border-radius", "2px")
			.set("padding", "2px 2px 2px 2px")
			.set("background-color", "rgba(255, 0, 0, 0.2)") // Rojo semitransparente
			.set("color", "lightcoral")
			.set("border", "1px solid lightcoral");
		}

		// Tooltip con más detalles
		databaseStatus.getElement().setAttribute("title", 
				String.format("Tipo: %s | Estado: %s | %s", 
						status.databaseType(), 
						status.status(), 
						status.message())
				);
	}
	
	private void updateFooter(String ruta, long duracionMinutos, String fechaHoraStr) {
	    // Si el footer aún no existe, créalo
	    if (footer == null) {
	        footer = new HorizontalLayout();
	        footer.setWidthFull();
	        footer.setHeight("32px");
	        footer.setPadding(true);
	        footer.setSpacing(true);
	        footer.addClassName("footer-dark");
	        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
	        footer.setAlignItems(FlexComponent.Alignment.CENTER);
	    } else {
	        // Limpia los hijos actuales
	        footer.removeAll();
	    }

	    // Copyright
	    Icon copyrightIcon = VaadinIcon.COPYRIGHT.create();
	    copyrightIcon.setColor("white");
	    copyrightIcon.getStyle().set("font-size", "9px");
	    Span copyright = new Span(copyrightIcon, new Text(" Dignitas 2025"));
	    copyright.getStyle()
	        .set("color", "white")
	        .set("font-size", "11px")
	        .set("white-space", "nowrap");

	    // Datos actuales
	    Span rutaActual = new Span("Ruta: " + ruta);
	    rutaActual.getStyle()
	        .set("color", "white")
	        .set("font-size", "11px")
	        .set("white-space", "nowrap");

	    Span fechaHora = new Span(fechaHoraStr);
	    fechaHora.getStyle()
	        .set("color", "white")
	        .set("font-size", "11px")
	        .set("white-space", "nowrap");

	    Span duracionSesion = new Span("Sesión: " + duracionMinutos + " min");
	    duracionSesion.getStyle()
	        .set("color", "white")
	        .set("font-size", "11px")
	        .set("white-space", "nowrap");

	    // Asegurar estilo de databaseStatus
	    databaseStatus.getStyle()
	        .set("color", "white")
	        .set("font-size", "11px")
	        .set("white-space", "nowrap");

	    footer.add(copyright, rutaActual, fechaHora, duracionSesion, databaseStatus);
	}
	
	@Override
	public void afterNavigation(AfterNavigationEvent event) {
	    String path = event.getLocation().getPath();
	    long minutos = Duration.between(inicioSesion, Instant.now()).toMinutes();
	    ZonedDateTime ahora = ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
	    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm z");
	    String formattedDate = ahora.format(formato);

//	    currentRoute = path; // si aún lo usas en otro lado
	    updateFooter(path, minutos, formattedDate);
	}

}