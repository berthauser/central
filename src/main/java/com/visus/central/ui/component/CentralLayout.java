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
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.visus.central.domain.port.in.HealthCheckUseCase;
import com.visus.central.domain.port.in.HealthCheckUseCase.DatabaseHealthStatus;
import com.visus.central.infraestructure.security.SecurityUtils;
import com.visus.central.ui.view.ActualizacionPreciosCodigoBarraView;
import com.visus.central.ui.view.ActualizacionPreciosRubroLineaView;
import com.visus.central.ui.view.AlicuotaView;
import com.visus.central.ui.view.ArticulosView;
import com.visus.central.ui.view.BancosView;
import com.visus.central.ui.view.ClienteView;
import com.visus.central.ui.view.CoeficienteView;
import com.visus.central.ui.view.ComprobanteView;
import com.visus.central.ui.view.ConceptoView;
import com.visus.central.ui.view.DepartamentoView;
import com.visus.central.ui.view.FormaDePagoView;
import com.visus.central.ui.view.LineaView;
import com.visus.central.ui.view.LocalidadView;
import com.visus.central.ui.view.MedidaView;
import com.visus.central.ui.view.PresentacionView;
import com.visus.central.ui.view.ProveedorView;
import com.visus.central.ui.view.RubroView;
import com.visus.central.ui.view.UsersView;
import com.visus.central.ui.view.VendedorView;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;

@PageTitle("Visus Central")
public class CentralLayout extends AppLayout implements AfterNavigationObserver {

	private static final long serialVersionUID = 1L;

	private Span databaseStatus = new Span();
	private Instant inicioSesion = Instant.now();
	private HorizontalLayout footer;
	private final HealthCheckUseCase healthCheckUseCase;

	public CentralLayout(HealthCheckUseCase healthCheckUseCase) {
		this.healthCheckUseCase = healthCheckUseCase;

		setPrimarySection(Section.DRAWER);
		addToNavbar(true, createHeader());
		addToDrawer(createDrawer());
		setDrawerOpened(true);
		getElement().getStyle().set("--vaadin-app-layout-drawer-width", "260px");

		checkDatabaseHealth();
	}

	private Component createHeader() {
		// Layout principal del header
		VerticalLayout mainHeader = new VerticalLayout();
		mainHeader.setPadding(false);
		mainHeader.setSpacing(false);
		mainHeader.setMargin(false);
		mainHeader.setWidthFull();

		// Parte superior del header (branding + usuario)
		HorizontalLayout topBar = createTopBar();

		mainHeader.add(topBar);

		return mainHeader;
	}

	private HorizontalLayout createTopBar() {

		HorizontalLayout branding = new HorizontalLayout(new DrawerToggle());
		branding.setAlignItems(FlexComponent.Alignment.CENTER);
		branding.setSpacing(true);
		branding.getStyle()
		.set("margin-right", "15px"); // Separación fija entre branding y menú

		// Crear menú pull-down
		MenuBar menuBar = createPullDownMenu();

		// Área del usuario (derecha) - crearla primero para poder usarla como referencia
		HorizontalLayout userArea = createUserArea();

		// Contenedor izquierdo que agrupa branding y menú
		HorizontalLayout leftSection = new HorizontalLayout();
		leftSection.setAlignItems(FlexComponent.Alignment.CENTER);
		leftSection.setSpacing(false);
		leftSection.setMargin(false);
		leftSection.setPadding(false);
		leftSection.add(branding, menuBar);

		// Layout principal del top bar
		HorizontalLayout topBar = new HorizontalLayout(leftSection, userArea);
		topBar.setWidthFull();
		topBar.setHeightFull();
		topBar.setPadding(false);
		topBar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
		topBar.setAlignItems(FlexComponent.Alignment.CENTER);
		topBar.setHeight("55px");
		topBar.getStyle()
		.set("padding", "4px 12px")
		.set("font-size", "0.9em")
		.set("background-color", "#101b29") 
		.set("color", "white")
		.set("border-bottom", "1px solid #3a5f5f");

		return topBar;
	}

	private MenuBar createPullDownMenu() {
		MenuBar menuBar = new MenuBar();
		menuBar.setOpenOnHover(true);
		menuBar.addThemeVariants(MenuBarVariant.LUMO_SMALL);
		menuBar.getStyle()
		.set("background-color", "#101b29")
		.set("color", "white")
		.set("padding", "0")
		.set("border-radius", "4px")
		.set("border", "1px solid #2a3f4f");

		// Item "Visus"
		MenuItem visusItem = menuBar.addItem("Visus");
		SubMenu visusSubMenu = visusItem.getSubMenu();
		visusSubMenu.addItem("Dashboard");
		visusSubMenu.addSeparator();

		// Ítem "Acerca de..." que abre el diálogo
		MenuItem aboutItem = visusSubMenu.addItem("Acerca de...");
		aboutItem.addClickListener(_ -> {
			AboutDialog aboutDialog = new AboutDialog();
			aboutDialog.openDialog();
		});

		// Item "Usuarios"
		MenuItem usuariosItem = menuBar.addItem("Usuarios");
		SubMenu usuariosSubMenu = usuariosItem.getSubMenu();
		usuariosSubMenu.addItem("Actualización");
		usuariosSubMenu.addItem("Listado");

		// Item "Inventario"
		MenuItem inventarioItem = menuBar.addItem("Inventario");
		SubMenu inventarioSubMenu = inventarioItem.getSubMenu();
		inventarioSubMenu.addItem("Común");
		inventarioSubMenu.addItem("Valorizado");
		inventarioSubMenu.addSeparator();
		inventarioSubMenu.addItem("con Filtros");

		// Item "Precios"
		MenuItem preciosItem = menuBar.addItem("Precios");
		SubMenu preciosSubMenu = preciosItem.getSubMenu();

		// OPCIÓN: Navegar a la ruta o crear instancia manualmente
		MenuItem rubroylineaItem = preciosSubMenu.addItem("Actualizar por Rubros y Líneas");
		rubroylineaItem.addClickListener(_ -> {
			//	        UI.getCurrent().navigate("actualizacion-precios"); // alternativa
			UI.getCurrent().navigate(ActualizacionPreciosRubroLineaView.class);
		});

		MenuItem porCodigoBarraItem = preciosSubMenu.addItem("Actualizar por Código de Barras");
		porCodigoBarraItem.addClickListener(_ -> {
			//	        UI.getCurrent().navigate("actualizacion-precios-codigo-barra");
			UI.getCurrent().navigate(ActualizacionPreciosCodigoBarraView.class);
		});

		// Estilizar los ítems del menú
		menuBar.getChildren().forEach(item -> {
			if (item instanceof MenuItem) {
				MenuItem menuItem = (MenuItem) item;

				// Estilos base para todos los items del menú
				menuItem.getElement().getStyle()
				.set("color", "white")
				.set("font-size", "14px")
				.set("font-weight", "normal")
				.set("padding", "8px 12px")
				.set("border-radius", "4px") // Bordes redondeados por defecto
				.set("margin", "0 2px") // Pequeño margen entre items
				.set("transition", "all 0.2s ease"); // Transición suave

				// Estilo para hover - redondeado más pronunciado
				menuItem.getElement().addEventListener("mouseenter", _ -> {
					menuItem.getElement().getStyle()
					.set("background-color", "#3a5f5f")
					.set("border-radius", "4px")
					.set("box-shadow", "0 2px 4px rgba(0,0,0,0.2)"); // Sombra sutil para efecto 3D
				});

				menuItem.getElement().addEventListener("mouseleave", _ -> {
					menuItem.getElement().getStyle()
					.set("background-color", "transparent")
					.set("border-radius", "4px") // Mantener borde redondeado al salir
					.set("box-shadow", "none"); // Eliminar sombra
				});
				// También para el foco (accesibilidad)
				menuItem.getElement().addEventListener("focus", _ -> {
					menuItem.getElement().getStyle()
					.set("background-color", "#3a5f5f")
					.set("border-radius", "4px")
					.set("outline", "2px solid #5a8f8f")
					.set("outline-offset", "2px");
				});

				menuItem.getElement().addEventListener("blur", _ -> {
					menuItem.getElement().getStyle()
					.set("background-color", "transparent")
					.set("border-radius", "4px")
					.set("outline", "none");
				});
			}
		});

		return menuBar;
	}

	private HorizontalLayout createUserArea() {
		Icon userIcon = VaadinIcon.USER.create();
		userIcon.setSize("24px");
		userIcon.getStyle().set("color", "white");

		Span username = new Span(SecurityContextHolder.getContext().getAuthentication().getName());
		username.getStyle()
		.set("color", "white")
		.set("font-size", "14px");

		Button logoutButton = new Button("Cerrar Sesión", _ -> {
			UI.getCurrent().getPage().setLocation("/logout");
		});

		logoutButton.getElement().setAttribute("theme", "small");
		logoutButton.getStyle()
		.set("color", "white")
		.set("background-color", "#2a3f4f")
		.set("border", "1px solid #3a5f5f")
		.set("font-size", "12px");

		HorizontalLayout userLayout = new HorizontalLayout(userIcon, username, logoutButton);
		userLayout.setAlignItems(FlexComponent.Alignment.CENTER);
		userLayout.setSpacing(true);

		return userLayout;
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
		.set("border-radius", "0")
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
		.set("border-radius", "0 0 8px 8px")
		.set("margin-bottom", "2px");

//		// Usamos una tabla simple para control preciso
//		Div tableContainer = new Div();
//		tableContainer.getStyle()
//		.set("display", "table")
//		.set("margin", "0 auto")
//		.set("padding", "8px 0");
//
//		// Fila 1: Visus
//		Div visusRow = new Div();
//		visusRow.getStyle()
//		.set("display", "table-row");
//
//		Div visusCell = new Div();
//		visusCell.getStyle()
//		.set("display", "table-cell")
//		.set("text-align", "center")
//		.set("vertical-align", "middle");
//
//		Span visusSpan = new Span("Visus");
//		visusSpan.getStyle()
//		.set("font-size", "20px")
//		.set("font-weight", "bold")
//		.set("color", "white")
//		.set("line-height", "1")
//		.set("letter-spacing", "0.5px")
//		.set("margin", "0")
//		.set("padding", "0")
//		.set("display", "block");
//
//		visusCell.add(visusSpan);
//		visusRow.add(visusCell);
		
		// Usamos una tabla simple para control preciso
		Div tableContainer = new Div();
		tableContainer.getStyle()
		.set("display", "table")
		.set("margin", "0 auto")
		.set("padding", "8px 0");

		// Fila 1: Icono + Visus
		Div visusRow = new Div();
		visusRow.getStyle()
		.set("display", "table-row");

		Div visusCell = new Div();
		visusCell.getStyle()
		.set("display", "table-cell")
		.set("text-align", "center")
		.set("vertical-align", "middle")
		.set("position", "relative"); // Para posicionar icono relativo a texto

		// Crear contenedor para el icono
		Div iconContainer = new Div();
		iconContainer.getStyle()
		.set("position", "absolute")
		.set("left", "-35px") // Ajusta esta posición para que quede bien alineado
		.set("top", "50%")
		.set("transform", "translateY(-50%)")
		.set("width", "auto")
		.set("height", "auto");

		Icon appIcon = VaadinIcon.CUBES.create();
		appIcon.setSize("24px"); // Tamaño reducido para no ocupar mucho espacio
		appIcon.setColor("#3a5f5f");
		iconContainer.add(appIcon);

		// Contenedor para el texto "Visus"
		Div visusTextContainer = new Div();
		visusTextContainer.getStyle()
		.set("display", "inline-block")
		.set("margin-left", "4px"); // Espacio para el icono

		Span visusSpan = new Span("Visus");
		visusSpan.getStyle()
		.set("font-size", "20px")
		.set("font-weight", "bold")
		.set("color", "white")
		.set("line-height", "1")
		.set("letter-spacing", "0.5px")
		.set("margin", "0")
		.set("padding", "0")
		.set("display", "block");

		visusTextContainer.add(visusSpan);
		visusCell.add(iconContainer, visusTextContainer); // Agregar ambos elementos
		visusRow.add(visusCell);

		// Fila 2: Central (alineado debajo de la 'u' de Visus)
		Div centralRow = new Div();
		centralRow.getStyle()
		.set("display", "table-row");

		Div centralCell = new Div();
		centralCell.getStyle()
		.set("display", "table-cell");
//		.set("padding-top", ""); // Pequeño espacio entre las filas

		// Contenedor para Central con padding-left específico
		Div centralWrapper = new Div();
		centralWrapper.getStyle()
		.set("display", "inline-block")
		.set("margin-left", "38px"); // Ajusta este valor para alinear con la 'u' de Visus

		Span centralSpan = new Span("Central");
		centralSpan.getStyle()
		.set("font-size", "14px")
		.set("color", "lightgray")
		.set("margin", "0")
		.set("padding", "0")
		.set("line-height", "1")
		.set("letter-spacing", "0.5px");

		centralWrapper.add(centralSpan);
		centralCell.add(centralWrapper);
		centralRow.add(centralCell);

		tableContainer.add(visusRow, centralRow);
		logoContainer.add(tableContainer);
		
		

		// Fila 2: Central (alineado debajo de la 'u')
//		Div centralRow = new Div();
//		centralRow.getStyle()
//		.set("display", "table-row");
//
//		Div centralCell = new Div();
//		centralCell.getStyle()
//		.set("display", "table-cell")
//		.set("padding-top", "2px"); // Pequeño espacio entre las filas
//
//		// Contenedor para Central con padding-left específico
//		Div centralWrapper = new Div();
//		centralWrapper.getStyle()
//		.set("display", "inline-block")
//		.set("margin-left", "38px"); // Ajusta este valor para alinear con la 'u' de Visus
//
//		Span centralSpan = new Span("Central");
//		centralSpan.getStyle()
//		.set("font-size", "14px")
//		.set("color", "lightgray")
//		.set("margin", "0")
//		.set("padding", "0")
//		.set("line-height", "1")
//		.set("letter-spacing", "0.5px");

		centralWrapper.add(centralSpan);
		centralCell.add(centralWrapper);
		centralRow.add(centralCell);

		tableContainer.add(visusRow, centralRow);
		logoContainer.add(tableContainer);

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
		.set("padding", "4px 0")
		.set("border-radius", "8px")
		.set("border", "1px solid #2a3f4f")
		.set("margin-top", "1px");

		// Enlace condicional a Usuarios
		if (SecurityUtils.isAdmin()) {
			System.out.println("El usuario es ADMIN");

			// Sección: Usuarios
			Details usuariosSection = createMenuSection(
					"Usuarios", 
					createMenuLink("Usuarios", UsersView.class, 35)
					);
			usuariosSection.getStyle().set("margin-bottom", "2px");
			menuLayout.add(usuariosSection);
		}

		System.out.println("El usuario NO es ADMIN");

		// Sección: CLIENTES
		Details clientesSection = createMenuSection(
				"Clientes",
				createMenuLink("Actualización", ClienteView.class, 35),
				createMenuSpan("Cuenta Corriente", 35),
				createMenuSpan("Listado", 35)
				);
		clientesSection.getStyle().set("margin-bottom", "2px");
		menuLayout.add(clientesSection);

		// Sección: PROVEEDORES
		Details proveedoresSection = createMenuSection(
				"Proveedores",
				createMenuLink("Actualización", ProveedorView.class, 35),
				createMenuSpan("Listado", 35)
				);
		proveedoresSection.getStyle().set("margin-bottom", "2px");
		menuLayout.add(proveedoresSection);

		// SECCIÓN: TABLAS BÁSICAS
		Details tablasBaseSection = createMenuSection(
				"Tablas Básicas",
				createMenuLink("Departamentos", DepartamentoView.class, 35),
				createMenuLink("Localidades", LocalidadView.class, 35),
				createMenuLink("Vendedores", VendedorView.class, 35),
				createMenuLink("Conceptos", ConceptoView.class, 35)
				);
		tablasBaseSection.getStyle().set("margin-bottom", "2px");
		menuLayout.add(tablasBaseSection);

		// Sección: ÁREA FINANCIERA
		Details areaFinancieraSection = createMenuSection(
				"Área Financiera",
				createMenuLink("Coeficientes", CoeficienteView.class, 35),
				createMenuLink("Alícuotas", AlicuotaView.class, 35),
				createMenuLink("Formas de Pago", FormaDePagoView.class, 35),
				createMenuLink("Comprobantes", ComprobanteView.class, 35),
				createMenuLink("Consultar Bancos", BancosView.class, 35)
				);
		areaFinancieraSection.getStyle().set("margin-bottom", "2px");
		menuLayout.add(areaFinancieraSection);

		// Sección: ARTÍCULOS
		Details articulosSection = createMenuSection(
				"Artículos",
				createMenuLink("Actualización", ArticulosView.class, 35),
				createMenuLink("Rubros", RubroView.class, 35),
				createMenuLink("Líneas", LineaView.class, 35),
				createMenuLink("Presentaciones", PresentacionView.class, 35),
				createMenuLink("Medidas", MedidaView.class, 35)
				);
		articulosSection.getStyle().set("margin-bottom", "2px");
		menuLayout.add(articulosSection);

		return menuLayout;
	}

	private Details createMenuSection(String title, Component... components) {
		VerticalLayout content = new VerticalLayout();
		content.setPadding(false);
		content.setSpacing(false);
		content.setMargin(false);
		content.setWidthFull();
		content.getStyle()
		.set("padding", "2px 0")
		.set("background-color", "transparent");

		for (Component component : components) {
			content.add(component);
		}

		Div summaryDiv = new Div();
		summaryDiv.setText(title);
		summaryDiv.getStyle()
		.set("background-color", "transparent")
		.set("padding", "8px 16px")
		.set("font-weight", "600")
		.set("font-size", "14px")
		.set("color", "white")
		.set("cursor", "pointer")
		.set("user-select", "none")
		.set("margin", "0 0")
		.set("width", "calc(100% - 16px)")
		.set("border-radius", "4px")
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
		.set("background-color", "transparent");

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
		.set("font-size", "0.85em")
		.set("font-weight", "400")
		.set("color", "#e0e0e0")
		.set("text-decoration", "none")
		.set("padding", "6px 16px")
		.set("display", "block")
		.set("border-radius", "4px")
		.set("margin", "0 8px")
		.set("width", "calc(100% - " + (marginLeft + 16) + "px)")
		.set("box-sizing", "border-box")
		.set("transition", "all 0.2s ease")
		.set("background-color", "transparent");

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
	}

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
		.set("background-color", "transparent");
		return span;
	}

	@Override
	public void setContent(Component content) {
		super.setContent(wrapWithFooter(content));
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
			databaseStatus.setText("BD: ERROR");
			databaseStatus.getStyle()
			.set("border-radius", "2px")
			.set("padding", "2px 2px 2px 2px")
			.set("background-color", "rgba(255, 165, 0, 0.2)")
			.set("color", "orange")
			.set("border", "1px solid orange");
			databaseStatus.getElement().setAttribute("title", 
					"Error al verificar: " + e.getMessage());

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
			.set("background-color", "rgba(0, 255, 0, 0.2)")
			.set("color", "lightgreen")
			.set("border", "1px solid lightgreen");
		} else {
			databaseStatus.getStyle()
			.set("border-radius", "2px")
			.set("padding", "2px 2px 2px 2px")
			.set("background-color", "rgba(255, 0, 0, 0.2)")
			.set("color", "lightcoral")
			.set("border", "1px solid lightcoral");
		}

		databaseStatus.getElement().setAttribute("title", 
				String.format("Tipo: %s | Estado: %s | %s", 
						status.databaseType(), 
						status.status(), 
						status.message())
				);
	}

	private void updateFooter(String ruta, long duracionMinutos, String fechaHoraStr) {
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
			footer.removeAll();
		}

		Icon copyrightIcon = VaadinIcon.COPYRIGHT.create();
		copyrightIcon.setColor("white");
		copyrightIcon.getStyle().set("font-size", "9px");
		Span copyright = new Span(copyrightIcon, new Text(" Dignitas 2026"));
		copyright.getStyle()
		.set("color", "white")
		.set("font-size", "11px")
		.set("white-space", "nowrap");

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

		updateFooter(path, minutos, formattedDate);
	}
}