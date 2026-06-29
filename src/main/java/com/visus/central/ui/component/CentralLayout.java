package com.visus.central.ui.component;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.visus.central.domain.port.in.HealthCheckUseCase;
import com.visus.central.domain.port.in.HealthCheckUseCase.DatabaseHealthStatus;
import com.visus.central.domain.port.in.PasswordChangeUseCase;
import com.visus.central.infraestructure.security.SecurityUtils;
import com.visus.central.infraestructure.util.SpringContextBridge;
import com.visus.central.ui.view.ActualizacionPreciosCodigoBarraView;
import com.visus.central.ui.view.ActualizacionPreciosRubroLineaView;
import com.visus.central.ui.view.AlicuotaView;
import com.visus.central.ui.view.ArticulosView;
import com.visus.central.ui.view.BancosView;
import com.visus.central.ui.view.CajaView;
import com.visus.central.ui.view.ClienteView;
import com.visus.central.ui.view.CoeficienteView;
import com.visus.central.ui.view.ComisionView;
import com.visus.central.ui.view.ComprobanteView;
import com.visus.central.ui.view.ConsultaDeListasView;
import com.visus.central.ui.view.CuentaCorrienteView;
import com.visus.central.ui.view.DevolucionView;
import com.visus.central.ui.view.FacturacionView;
import com.visus.central.ui.view.ImpresionEtiquetasView;
import com.visus.central.ui.view.LineaView;
import com.visus.central.ui.view.ListasView;
import com.visus.central.ui.view.LocalidadView;
import com.visus.central.ui.view.LoginView;
import com.visus.central.ui.view.MedidaView;
import com.visus.central.ui.view.PermisosView;
import com.visus.central.ui.view.PorcentualesView;
import com.visus.central.ui.view.PresentacionView;
import com.visus.central.ui.view.ProveedorView;
import com.visus.central.ui.view.ReglaComisionView;
import com.visus.central.ui.view.RubroView;
import com.visus.central.ui.view.TipoPagoView;
import com.visus.central.ui.view.UsersView;
import com.visus.central.ui.view.VendedorView;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

@PageTitle("Visus Central")
public class CentralLayout extends AppLayout implements AfterNavigationObserver, BeforeEnterObserver {

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
		branding.getStyle().set("margin-right", "15px"); // Separación fija entre branding y menú

		// Crear menú pull-down
		MenuBar menuBar = createPullDownMenu();

		// Área del usuario (derecha) - crearla primero para poder usarla como
		// referencia
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
		topBar.getStyle().set("padding", "4px 12px").set("font-size", "0.9em").set("background-color", "#101b29")
				.set("color", "white").set("border-bottom", "1px solid #3a5f5f");

		return topBar;
	}

	private MenuBar createPullDownMenu() {
		MenuBar menuBar = new MenuBar();
		menuBar.setOpenOnHover(true);
		menuBar.addThemeVariants(MenuBarVariant.LUMO_SMALL);
		menuBar.getStyle().set("background-color", "#101b29").set("color", "white").set("padding", "0")
				.set("border-radius", "4px").set("border", "1px solid #2a3f4f");

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

		// Item "Usuarios" (solo ADMIN, sin submenú)
		if (SecurityUtils.isAdmin()) {
			MenuItem usuariosItem = menuBar.addItem("Usuarios");
			usuariosItem.addClickListener(_ -> {
				UI.getCurrent().navigate(UsersView.class);
			});
		}

		// Item "Permisos" (solo ADMIN)
		if (SecurityUtils.isAdmin()) {
			MenuItem permisosItem = menuBar.addItem("Permisos");
			permisosItem.addClickListener(_ -> {
				UI.getCurrent().navigate(PermisosView.class);
			});
		}

		// Item "Caja"
		MenuItem cajaItem = menuBar.addItem("Caja");
		SubMenu cajaSubMenu = cajaItem.getSubMenu();
		MenuItem movManuales = cajaSubMenu.addItem("Movimientos");
		movManuales.addClickListener(_ -> {
			UI.getCurrent().navigate(CajaView.class);
		});
		MenuItem consultaCaja = cajaSubMenu.addItem("Consulta");
		consultaCaja.addClickListener(_ -> {
			UI.getCurrent().navigate(com.visus.central.ui.view.ConsultaCajaView.class);
		});

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
			// UI.getCurrent().navigate("actualizacion-precios"); // alternativa
			UI.getCurrent().navigate(ActualizacionPreciosRubroLineaView.class);
		});

		MenuItem porCodigoBarraItem = preciosSubMenu.addItem("Actualizar por Código de Barras");
		porCodigoBarraItem.addClickListener(_ -> {
			// UI.getCurrent().navigate("actualizacion-precios-codigo-barra");
			UI.getCurrent().navigate(ActualizacionPreciosCodigoBarraView.class);
		});

		preciosSubMenu.addSeparator();

		MenuItem consultaListasItem = preciosSubMenu.addItem("Consulta de Listas");
		consultaListasItem.addClickListener(_ -> {
			UI.getCurrent().navigate(ConsultaDeListasView.class);
		});

		// Item "Facturación"
		MenuItem facturacionItem = menuBar.addItem("Facturación");
		SubMenu facturacionSubMenu = facturacionItem.getSubMenu();

		MenuItem facturarItem = facturacionSubMenu.addItem("Facturar");
		facturarItem.addClickListener(_ -> {
			UI.getCurrent().navigate(FacturacionView.class);
		});
			
		MenuItem devolucionItem = facturacionSubMenu.addItem("Devoluciones");
		devolucionItem.addClickListener(_ -> {
				UI.getCurrent().navigate(DevolucionView.class);
			
		});

		// Estilizar los ítems del menú
		menuBar.getChildren().forEach(item -> {
			if (item instanceof MenuItem) {
				MenuItem menuItem = (MenuItem) item;

				// Estilos base para todos los items del menú
				menuItem.getElement().getStyle().set("color", "white").set("font-size", "14px")
						.set("font-weight", "normal").set("padding", "8px 12px").set("border-radius", "4px") 
						.set("margin", "0 2px") // Pequeño margen entre items
						.set("transition", "all 0.2s ease"); // Transición suave

				// Estilo para hover - redondeado más pronunciado
				menuItem.getElement().addEventListener("mouseenter", _ -> {
					menuItem.getElement().getStyle().set("background-color", "#3a5f5f").set("border-radius", "4px")
							.set("box-shadow", "0 2px 4px rgba(0,0,0,0.2)"); // Sombra sutil para efecto 3D
				});

				menuItem.getElement().addEventListener("mouseleave", _ -> {
					menuItem.getElement().getStyle().set("background-color", "transparent").set("border-radius", "4px") 
							.set("box-shadow", "none"); // Eliminar sombra
				});
				// También para el foco (accesibilidad)
				menuItem.getElement().addEventListener("focus", _ -> {
					menuItem.getElement().getStyle().set("background-color", "#3a5f5f").set("border-radius", "4px")
							.set("outline", "2px solid #5a8f8f").set("outline-offset", "2px");
				});

				menuItem.getElement().addEventListener("blur", _ -> {
					menuItem.getElement().getStyle().set("background-color", "transparent").set("border-radius", "4px")
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

		Span username = new Span(SecurityUtils.getUsername() != null ? SecurityUtils.getUsername() : "Invitado");
		username.getStyle().set("color", "white").set("font-size", "14px");

		Button changePasswordButton = new Button("Cambiar Contraseña", _ -> {
			PasswordChangeUseCase pwdUseCase = SpringContextBridge.getBean(PasswordChangeUseCase.class);
			ChangePasswordDialog dialog = new ChangePasswordDialog(pwdUseCase);
			dialog.open();
		});
		changePasswordButton.getElement().setAttribute("theme", "small");
		changePasswordButton.getStyle().set("color", "white").set("background-color", "#2a3f4f")
				.set("border", "1px solid #3a5f5f").set("font-size", "12px");

		Button logoutButton = new Button("Salir", _ -> {
			UI.getCurrent().getPage().setLocation("/logout");
		});
		logoutButton.getElement().setAttribute("theme", "small");
		logoutButton.getStyle().set("color", "white").set("background-color", "#8b0000")
				.set("border", "1px solid #a00000").set("font-size", "12px");

		HorizontalLayout userLayout = new HorizontalLayout(userIcon, username, changePasswordButton, logoutButton);
		userLayout.setAlignItems(FlexComponent.Alignment.CENTER);
		userLayout.setSpacing(true);

		return userLayout;
	}

	private Component createDrawer() {
		VerticalLayout drawer = new VerticalLayout();
		drawer.setPadding(false);
		drawer.setSpacing(false);
		drawer.setSizeFull();
		drawer.getStyle().set("padding", "0").set("margin", "0").set("background-color", "transparent")
				.set("color", "white").set("overflow", "hidden").set("width", "255px").set("min-width", "255px")
				.set("max-width", "255px").set("border-radius", "0").set("margin-top", "2px")
				.set("margin-bottom", "2px");

		VerticalLayout logoContainer = new VerticalLayout();
		logoContainer.setPadding(false);
		logoContainer.setSpacing(false);
		logoContainer.setMargin(false);
		logoContainer.setWidthFull();
		logoContainer.setHeight("53px");
		logoContainer.getStyle().set("padding", "0").set("background-color", "#101b29")
				.set("border-bottom", "1px solid #3a5f5f").set("display", "flex").set("align-items", "center")
				.set("justify-content", "center").set("border-radius", "0 0 8px 8px").set("margin-bottom", "2px");

		// Icono + Visus + Central con flexbox simple
		Icon appIcon = VaadinIcon.CUBES.create();
		appIcon.setSize("28px");
		appIcon.setColor("#3a5f5f");
		appIcon.getStyle().set("flex-shrink", "0");

		Span visusSpan = new Span("Visus");
		visusSpan.getStyle().set("font-size", "20px").set("font-weight", "bold").set("color", "white")
				.set("line-height", "1.2").set("letter-spacing", "0.5px");

		Span centralSpan = new Span("Central");
		centralSpan.getStyle().set("font-size", "14px").set("color", "lightgray").set("line-height", "1.2")
				.set("letter-spacing", "0.5px");

		VerticalLayout textLayout = new VerticalLayout(visusSpan, centralSpan);
		textLayout.setPadding(false);
		textLayout.setSpacing(false);
		textLayout.setAlignItems(Alignment.START);

		HorizontalLayout headerLayout = new HorizontalLayout(appIcon, textLayout);
		headerLayout.setAlignItems(Alignment.CENTER);
		headerLayout.setSpacing(true);
		headerLayout.getStyle().set("padding", "8px 0");

		logoContainer.add(headerLayout);

		SideNav menu = createMenu();

		VerticalLayout menuWrapper = new VerticalLayout(menu);
		menuWrapper.setPadding(false);
		menuWrapper.setSpacing(false);
		menuWrapper.setMargin(false);
		menuWrapper.getStyle().set("min-height", "0");
		menuWrapper.getStyle().set("overflow-y", "auto");

		drawer.add(logoContainer, menuWrapper);
		drawer.setFlexGrow(1, menuWrapper);
		return drawer;
	}

	private SideNav createMenu() {
		SideNav sideNav = new SideNav();
		sideNav.setAutoExpand(true);

		SideNavItem clientes = new SideNavItem("Clientes");
		clientes.setPrefixComponent(VaadinIcon.USERS.create());
		clientes.addItem(new SideNavItem("Actualización", ClienteView.class));
		clientes.addItem(new SideNavItem("Cuenta Corriente", CuentaCorrienteView.class));
		sideNav.addItem(clientes);

		SideNavItem proveedores = new SideNavItem("Proveedores");
		proveedores.setPrefixComponent(VaadinIcon.BUILDING.create());
		proveedores.addItem(new SideNavItem("Actualización", ProveedorView.class));
		sideNav.addItem(proveedores);

		SideNavItem tablasBasicas = new SideNavItem("Catálogos");
		tablasBasicas.setPrefixComponent(VaadinIcon.TABLE.create());
		tablasBasicas.addItem(new SideNavItem("Localidades", LocalidadView.class));
		tablasBasicas.addItem(new SideNavItem("Vendedores", VendedorView.class));
		tablasBasicas.addItem(new SideNavItem("Porcentuales para Listas", PorcentualesView.class));
		tablasBasicas.addItem(new SideNavItem("Listas de Precios", ListasView.class));
		sideNav.addItem(tablasBasicas);

		SideNavItem areaFinanciera = new SideNavItem("Área Financiera");
		areaFinanciera.setPrefixComponent(VaadinIcon.MONEY.create());
		areaFinanciera.addItem(new SideNavItem("Coeficientes", CoeficienteView.class));
		areaFinanciera.addItem(new SideNavItem("Alícuotas", AlicuotaView.class));
		areaFinanciera.addItem(new SideNavItem("Tipos de Pago", TipoPagoView.class));
		areaFinanciera.addItem(new SideNavItem("Reglas de Comisiones", ReglaComisionView.class));
		areaFinanciera.addItem(new SideNavItem("Comisiones", ComisionView.class));
		areaFinanciera.addItem(new SideNavItem("Comprobantes", ComprobanteView.class));
		areaFinanciera.addItem(new SideNavItem("Consultar Bancos", BancosView.class));
		sideNav.addItem(areaFinanciera);

		SideNavItem articulos = new SideNavItem("Artículos");
		articulos.setPrefixComponent(VaadinIcon.CUBE.create());
		articulos.addItem(new SideNavItem("Actualización", ArticulosView.class));
		articulos.addItem(new SideNavItem("Rubros", RubroView.class));
		articulos.addItem(new SideNavItem("Líneas", LineaView.class));
		articulos.addItem(new SideNavItem("Presentaciones", PresentacionView.class));
		articulos.addItem(new SideNavItem("Medidas", MedidaView.class));
		articulos.addItem(new SideNavItem("Imprimir Etiquetas", ImpresionEtiquetasView.class));
		sideNav.addItem(articulos);

		return sideNav;
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
		layout.getStyle().set("background-color", "#0a121f").set("padding", "4px");

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
			databaseStatus.getStyle().set("border-radius", "2px").set("padding", "2px 2px 2px 2px")
					.set("background-color", "rgba(255, 165, 0, 0.2)").set("color", "orange")
					.set("border", "1px solid orange");
			databaseStatus.getElement().setAttribute("title", "Error al verificar: " + e.getMessage());

			System.err.println("Error checking database health: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void updateDatabaseStatusIndicator(DatabaseHealthStatus status) {
		String statusText = "BD: " + (status.isConnected() ? "ON-LINE" : "OFF-LINE");
		databaseStatus.setText(statusText);

		if (status.isConnected()) {
			databaseStatus.getStyle().set("border-radius", "2px").set("padding", "2px 2px 2px 2px")
					.set("background-color", "rgba(0, 255, 0, 0.2)").set("color", "lightgreen")
					.set("border", "1px solid lightgreen");
		} else {
			databaseStatus.getStyle().set("border-radius", "2px").set("padding", "2px 2px 2px 2px")
					.set("background-color", "rgba(255, 0, 0, 0.2)").set("color", "lightcoral")
					.set("border", "1px solid lightcoral");
		}

		databaseStatus.getElement().setAttribute("title",
				String.format("Tipo: %s | Estado: %s | %s", status.databaseType(), status.status(), status.message()));
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
		copyright.getStyle().set("color", "white").set("font-size", "11px").set("white-space", "nowrap");

		Span rutaActual = new Span("Ruta: " + ruta);
		rutaActual.getStyle().set("color", "white").set("font-size", "11px").set("white-space", "nowrap");

		Span fechaHora = new Span(fechaHoraStr);
		fechaHora.getStyle().set("color", "white").set("font-size", "11px").set("white-space", "nowrap");

		Span duracionSesion = new Span("Sesión: " + duracionMinutos + " min");
		duracionSesion.getStyle().set("color", "white").set("font-size", "11px").set("white-space", "nowrap");

		databaseStatus.getStyle().set("color", "white").set("font-size", "11px").set("white-space", "nowrap");

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

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		if (!SecurityUtils.isAuthenticated() && !event.getNavigationTarget().equals(LoginView.class)) {
			event.forwardTo(LoginView.class);
		}
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);
		attachEvent.getUI().getElement().getThemeList().add("dark");
		attachEvent.getUI().getPage().executeJs("document.documentElement.style.setProperty('color-scheme', 'dark')");
	}
}