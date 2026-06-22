package com.visus.central.ui.view;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.visus.central.infraestructure.util.BarcodeScannerUtil;
import com.visus.central.infraestructure.util.FormatoUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.UI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.visus.central.infraestructure.persistence.entity.JpaArticuloEntity;
import com.visus.central.domain.model.Articulo;
import com.visus.central.domain.port.in.ArticuloUseCase;
import com.visus.central.ui.component.AbstractForm;
import com.visus.central.ui.component.ArticuloForm;
import com.visus.central.ui.component.CentralLayout;

import jakarta.annotation.PostConstruct;

@Route(value = "articulos", layout = CentralLayout.class)
@PageTitle("Gestión de Artículos")
public class ArticulosView extends AbstractView<Articulo> {

	private static final long serialVersionUID = 1L;

	private final ArticuloUseCase service;

	// Variables para paginación
	private int currentPage = 0;
	// private final int pageSize = 50;
	private long totalElements = 0;
	private int totalPages = 0;
	private String currentFilter = "";

	private Span indicadorBusqueda;
	private Sort currentSort = Sort.by(Sort.Direction.ASC, "descripcion");

	// filtros adicionales
	private ComboBox<String> filtroEstado;

	// Componentes de paginación
	private Button firstPageButton;
	private Button prevPageButton;
	private Button nextPageButton;
	private Button lastPageButton;
	private Span pageInfo;
	private ComboBox<Integer> pageSizeCombo;

	public ArticulosView(ArticuloUseCase service) {
		super("Nuevo Artículo", service);
		this.service = service;

		// DIFERIR CARGA DE DATOS - CRÍTICO
		deferDataLoading();

		// Configurar paginación inmediatamente
		setupPagination();

		// Listeners
		form.addCreateListener(_ -> cargarDatosPaginados());
		form.addUpdateListener(_ -> cargarDatosPaginados());

		// Ordenamiento por columnas
		grid.addSortListener(event -> {
			var sortOrder = event.getSortOrder();
			if (!sortOrder.isEmpty()) {
				var order = sortOrder.get(0);
				String property = order.getSorted().getKey();
				if (property != null && !property.isEmpty()) {
					Sort.Direction dir = order.getDirection() == com.vaadin.flow.data.provider.SortDirection.ASCENDING
							? Sort.Direction.ASC : Sort.Direction.DESC;
					currentSort = Sort.by(dir, property);
				}
			} else {
				currentSort = Sort.by(Sort.Direction.ASC, "descripcion");
			}
			currentPage = 0;
			cargarDatosPaginados();
		});
	}

	@PostConstruct
	public void init() {
		// Cargar primera página de datos después de que todo esté inicializado
		cargarDatosPaginados();
	}

	// MODIFICADO: Sobreescribir actualizarGrilla para usar paginación
	@Override
	protected void actualizarGrilla() {
		// Redirigir al método de carga paginada
		cargarDatosPaginados();
	}

	@Override
	protected void guardar(Articulo model) {
		showLoading("Guardando...");
		try {
			@SuppressWarnings("unused")
			Articulo modelGuardado = service.save(model);

			cargarDatosPaginados();

			form.setVisible(false);
			showSuccess("Registro guardado");
		} finally {
			hideLoading();
		}
	}

	@Override
	protected void eliminar(Articulo articulo) {
		confirmarEliminacion(() -> {
			showLoading("Eliminando...");
			try {
				mostrarConfirmacionEliminacion(articulo);
				service.deleteById(getId(articulo));

				// Recargar los datos paginados después de eliminar
				cargarDatosPaginados();

				form.setVisible(false);
				showSuccess("Registro eliminado");
			} finally {
				hideLoading();
			}
		});
	}

	// Sobrescribo buildHeader para agregar filtro de estado
	@Override
	protected HorizontalLayout buildHeader(String textoNuevo) {
		// Primero, obtener el header base de la clase padre
		HorizontalLayout header = super.buildHeader(textoNuevo);

		// Crear TextField para búsqueda por código de barra
		TextField filtroCodigoBarraField = new TextField();
		filtroCodigoBarraField.addClassName("campo-estilo-imagen");
		filtroCodigoBarraField.setPlaceholder("Escanea código de barra");
		filtroCodigoBarraField.setWidth("250px");
		filtroCodigoBarraField.setPrefixComponent(new Icon(VaadinIcon.BARCODE));

		// Botón para limpiar el campo de código de barra
		Button btnLimpiarCodigo = new Button(new Icon(VaadinIcon.CLOSE));
		btnLimpiarCodigo.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
		btnLimpiarCodigo.setTooltipText("Limpiar búsqueda");
		btnLimpiarCodigo.addClickListener(_ -> {
			filtroCodigoBarraField.clear();
			// Volver a mostrar todos los datos
			currentPage = 0;
			cargarDatosPaginados();
		});

		// Layout para el campo de código de barra con botón
		HorizontalLayout layoutCodigoBarra = new HorizontalLayout(filtroCodigoBarraField, btnLimpiarCodigo);
		layoutCodigoBarra.setSpacing(false);
		layoutCodigoBarra.setAlignItems(Alignment.CENTER);
		layoutCodigoBarra.setPadding(false);

		// Configurar el comportamiento del lector
		BarcodeScannerUtil.configureBarcodeScanner(filtroCodigoBarraField, () -> {
			buscarPorCodigoBarra(filtroCodigoBarraField.getValue());
		});

		// Crear ComboBox sin label visible
		filtroEstado = new ComboBox<>();
		filtroEstado.setItems("Todos", "Disponible", "En Existencias", "No Disponible", "Comprometido", "Entrante");
		filtroEstado.setValue("Todos");
		filtroEstado.setWidth("200px");
		filtroEstado.addClassName("campo-estilo-imagen");

		// Simplemente crea el tooltip
		Tooltip.forComponent(filtroEstado)
				.withText("Valor por defecto: 'Todos' - Filtra artículos por su estado actual")
				.withPosition(Tooltip.TooltipPosition.TOP);

		// Agregar listener para recargar datos cuando cambie el filtro
		filtroEstado.addValueChangeListener(_ -> {
			currentPage = 0;
			cargarDatosPaginados();
		});

		// Insertar ambos componentes en el header
		// header tiene: [0] nuevo, [1] filtro (TextField), [2] inicio
		header.addComponentAtIndex(2, layoutCodigoBarra); // Primero el código de barra
		header.addComponentAtIndex(3, filtroEstado); // Luego el estado

		return header;
	}

	@Override
	protected List<Grid.Column<Articulo>> buildColumns(Grid<Articulo> grid) {
		return List.of(
				grid.addColumn(Articulo::getCodigo_interno).setHeader("CÓDIGO INTERNO").setAutoWidth(true)
						.setResizable(true).setSortable(true).setKey("codigoInterno"),

				grid.addColumn(Articulo::getCodigo_barra).setHeader("CÓDIGO DE BARRA").setAutoWidth(true)
						.setResizable(true).setSortable(true).setKey("codigoBarra"),

				grid.addColumn(Articulo::getDescripcion).setHeader("DESCRIPCIÓN").setAutoWidth(true).setResizable(true)
						.setSortable(true).setKey("descripcion"),

				// STOCK formateado con separadores de miles
				grid.addColumn(articulo -> FormatoUtils.formatNumero(articulo.getStock())).setHeader("STOCK")
						.setAutoWidth(true).setResizable(true).setTextAlign(ColumnTextAlign.END)
						.setSortable(true).setKey("stock"),

				// STOCK MÍNIMO formateado con separadores de miles
				grid.addColumn(articulo -> FormatoUtils.formatNumero(articulo.getStock_minimo()))
						.setHeader("STOCK MÍNIMO").setAutoWidth(true).setResizable(true)
						.setTextAlign(ColumnTextAlign.END).setSortable(true).setKey("stock_minimo"),

				// STOCK MÁXIMO formateado con separadores de miles
				grid.addColumn(articulo -> FormatoUtils.formatNumero(articulo.getStock_maximo()))
						.setHeader("STOCK MÁXIMO").setAutoWidth(true).setResizable(true)
						.setTextAlign(ColumnTextAlign.END).setSortable(true).setKey("stock_maximo"),

				grid.addColumn(articulo -> articulo.getLinea() != null ? articulo.getLinea().getDescripcion() : "")
						.setHeader("LÍNEA").setAutoWidth(true).setResizable(true),

				// Columna RUBRO
				grid.addColumn(articulo -> {
					if (articulo.getLinea() != null && articulo.getLinea().getRubro() != null) {
						return articulo.getLinea().getRubro().getDescripcion();
					}
					return "Sin Rubro";
				}).setHeader("RUBRO").setAutoWidth(true).setResizable(true).setFlexGrow(1), // Puede crecer un poco más

				grid.addColumn(articulo -> articulo.getMedida() != null ? articulo.getMedida().getDescripcion() : "")
						.setHeader("MEDIDA").setAutoWidth(true).setResizable(true),

				grid.addColumn(
						articulo -> articulo.getPresentacion() != null ? articulo.getPresentacion().getDescripcion()
								: "")
						.setHeader("PRESENTACIÓN").setAutoWidth(true).setResizable(true),

				// Columna 1: Descripción de la Alícuota (mostrar exactamente lo que hay en BD)
				grid.addColumn(articulo -> articulo.getAlicuota() != null ? articulo.getAlicuota().getDescripcion()
						: "Sin Asignar").setHeader("ALÍCUOTA").setAutoWidth(true).setResizable(true),

				// Columna 2: Gravamen usando FormatoUtils
				grid.addColumn(articulo -> {
					if (articulo.getAlicuota() == null) {
						return "No Aplica";
					}
					BigDecimal gravamen = articulo.getAlicuota().getGravamen();
					return FormatoUtils.formatGravamen(gravamen);
				}).setHeader("GRAVAMEN").setAutoWidth(true).setResizable(true).setTextAlign(ColumnTextAlign.END),

				// PRECIO DE COSTO formateado como pesos argentinos y con 2 decimales fijos
				grid.addColumn(articulo -> {
					BigDecimal precio = articulo.getPrecioCosto();
					if (precio == null) {
						return "";
					}
					return FormatoUtils.formatPesos(precio.setScale(2, RoundingMode.HALF_UP));
				}).setHeader("PRECIO DE COSTO").setAutoWidth(true).setResizable(true).setTextAlign(ColumnTextAlign.END)
						.setSortable(true).setKey("precio_costo"),

				// MARGEN DE UTILIDAD formateado como porcentaje
				grid.addColumn(articulo -> {
					BigDecimal margen = articulo.getMargenUtilidad();
					if (margen == null) {
						return "";
					}
					// El valor en BD es 65,29 (porcentaje entero), por lo tanto esDecimal = false
					return FormatoUtils.formatPorcentaje(margen, false);
				}).setHeader("MARGEN DE UTILIDAD").setAutoWidth(true).setResizable(true)
						.setTextAlign(ColumnTextAlign.END)
						.setSortable(true).setKey("margen_utilidad"),

				// PRECIO CON IVA (sin margen)
				grid.addColumn(articulo -> {
					BigDecimal precioCosto = articulo.getPrecioCosto();
					BigDecimal gravamen = articulo.getAlicuota() != null ? articulo.getAlicuota().getGravamen()
							: BigDecimal.ZERO;

					if (precioCosto == null) {
						return "";
					}

					BigDecimal factor = BigDecimal.ONE
							.add(gravamen != null ? gravamen.divide(new BigDecimal("100")) : BigDecimal.ZERO);
					BigDecimal precioConIva = precioCosto.multiply(factor);

					return FormatoUtils.formatPesos(precioConIva);
				}).setHeader("PRECIO + IVA").setAutoWidth(true).setResizable(true).setTextAlign(ColumnTextAlign.END),

				// PRECIO DE VENTA (con margen e IVA)
				grid.addColumn(articulo -> {
					BigDecimal precioCosto = articulo.getPrecioCosto();
					BigDecimal margen = articulo.getMargenUtilidad();
					BigDecimal gravamen = articulo.getAlicuota() != null ? articulo.getAlicuota().getGravamen()
							: BigDecimal.ZERO;

					if (precioCosto == null) {
						return "";
					}

					BigDecimal precioConMargen = precioCosto;
					if (margen != null) {
						BigDecimal factorMargen = BigDecimal.ONE.add(margen.divide(new BigDecimal("100")));
						precioConMargen = precioCosto.multiply(factorMargen);
					}

					BigDecimal precioFinal = precioConMargen;
					if (gravamen != null && gravamen.compareTo(BigDecimal.ZERO) > 0) {
						BigDecimal factorGravamen = BigDecimal.ONE.add(gravamen.divide(new BigDecimal("100")));
						precioFinal = precioConMargen.multiply(factorGravamen);
					}

					return FormatoUtils.formatPesos(precioFinal);
				}).setHeader("PRECIO DE VENTA").setAutoWidth(true).setResizable(true).setTextAlign(ColumnTextAlign.END),

				grid.addColumn(new ComponentRenderer<>(articulo -> {
					Span estado = new Span();

					if (articulo.getEstado() != null) {
						estado.setText(articulo.getEstado().toString());

						switch (articulo.getEstado()) {
						case Disponible:
						case EnExistencias:
							estado.getElement().getThemeList().add("badge success");
							break;
						case NoDisponible:
							estado.getElement().getThemeList().add("badge error");
							break;
						case Comprometido:
							estado.getElement().getThemeList().add("badge warning");
							break;
						case Entrante:
							estado.getElement().getThemeList().add("badge contrast");
							break;
						default:
							// Sin estilo especial
						}
					}
					return estado;
				})).setHeader("ESTADO").setAutoWidth(true).setResizable(true),

				// ACCIONES
				grid.addComponentColumn(articulo -> {
					HorizontalLayout layout = new HorizontalLayout();
					layout.setSpacing(true);

					// Botón duplicar: siempre visible
					Button duplicar = new Button(new Icon(VaadinIcon.COPY));
					duplicar.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
					duplicar.setTooltipText("Duplicar artículo");
					duplicar.addClickListener(_ -> duplicarArticulo(articulo));
					layout.add(duplicar);

					// Botón reactivar: solo cuando está NoDisponible
					if (articulo.getEstado() == JpaArticuloEntity.Estado.NoDisponible) {
						Button reactivar = new Button("Activar", new Icon(VaadinIcon.CHECK));
						reactivar.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);
						reactivar.addClickListener(_ -> reactivarArticulo(articulo));
						layout.add(reactivar);
					}

					return layout;
				}).setHeader("ACCIONES").setWidth("180px").setFlexGrow(0)

		);
	}

	@Override
	protected AbstractForm<Articulo> buildForm() {
		ArticuloForm form = new ArticuloForm();
		return form; // Simple y sin dependencias
	}

	@Override
	protected void aplicarFiltro(String criterio) {
		currentFilter = criterio != null ? criterio.trim() : "";
		currentPage = 0; // Volver a la primera página
		cargarDatosPaginados();
	}

	private void mostrarConfirmacionEliminacion(Articulo articulo) {
		Dialog dialogo = new Dialog();
		dialogo.setHeaderTitle("Dar de baja artículo");

		Span mensaje = new Span("¿Está seguro de dar de baja el Artículo \"" + articulo.getDescripcion()
				+ "\"? El artículo cambiará a estado 'No Disponible'.");
		mensaje.getStyle().set("font-weight", "500");

		Button aceptar = new Button("Dar de baja", _ -> {
			showLoading("Procesando baja...");
			try {
				// Usamos deleteById que ahora cambia el estado
				service.deleteById(articulo.getId());
				filtro.clear();
//				actualizarLista();
				form.setVisible(false);
				Notification.show("Artículo dado de baja correctamente", 3000, Notification.Position.BOTTOM_CENTER)
						.addThemeVariants(NotificationVariant.LUMO_WARNING); // Cambiar a warning en lugar de success
			} finally {
				hideLoading();
			}
			dialogo.close();
		});

		Button cancelar = new Button("Cancelar", _ -> dialogo.close());

		aceptar.addThemeVariants(ButtonVariant.LUMO_ERROR);
		cancelar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		dialogo.getFooter().add(aceptar, cancelar);
		dialogo.add(mensaje);
		dialogo.open();
	}

	private void setupPagination() {

		// Inicializar pageSizeCombo primero
		pageSizeCombo = new ComboBox<>();
		pageSizeCombo.setItems(25, 50, 100, 200);
		pageSizeCombo.setValue(50);

		// Ahora inicializar los demás componentes
		firstPageButton = new Button("Primera", new Icon(VaadinIcon.FAST_BACKWARD));
		prevPageButton = new Button("Anterior", new Icon(VaadinIcon.CHEVRON_LEFT));
		nextPageButton = new Button("Siguiente", new Icon(VaadinIcon.CHEVRON_RIGHT));
		lastPageButton = new Button("Última", new Icon(VaadinIcon.FAST_FORWARD));
		// ... resto de componentes

		// El listener puede usar cargarDatosPaginados porque pageSizeCombo ya está
		// inicializado
		pageSizeCombo.addValueChangeListener(_ -> {
			currentPage = 0;
			cargarDatosPaginados();
		});

		pageInfo = new Span();

		// Simplemente crea el tooltip
		Tooltip.forComponent(pageSizeCombo).withText("Valor por defecto: 50 - Registros por página")
				.withPosition(Tooltip.TooltipPosition.TOP);

		// Estilos de botones
		firstPageButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
		prevPageButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
		nextPageButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
		lastPageButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);

		// Listeners
		firstPageButton.addClickListener(_ -> {
			currentPage = 0;
			cargarDatosPaginados();
		});

		prevPageButton.addClickListener(_ -> {
			if (currentPage > 0) {
				currentPage--;
				cargarDatosPaginados();
			}
		});

		nextPageButton.addClickListener(_ -> {
			if (currentPage < totalPages - 1) {
				currentPage++;
				cargarDatosPaginados();
			}
		});

		lastPageButton.addClickListener(_ -> {
			currentPage = totalPages > 0 ? totalPages - 1 : 0;
			cargarDatosPaginados();
		});

		// Layout de paginación
		HorizontalLayout paginationLayout = new HorizontalLayout(firstPageButton, prevPageButton, pageInfo,
				nextPageButton, lastPageButton, pageSizeCombo);

		paginationLayout.setSpacing(true);
		paginationLayout.setAlignItems(Alignment.CENTER);
		paginationLayout.setWidthFull();
		paginationLayout.setJustifyContentMode(JustifyContentMode.CENTER);

		// Agregar debajo del grid
		add(paginationLayout);

	};

	private void cargarDatosPaginados() {
		try {
			// COMPROBACIÓN DE SEGURIDAD: si los componentes no están listos, salir
			if (pageSizeCombo == null || filtroEstado == null) {
				System.err.println("Componentes no inicializados. Abortando carga de datos.");
				return;
			}

			// Si hay una búsqueda por código de barra activa, no cargamos datos paginados
			if (estaBuscandoPorCodigoBarra()) {
				return;
			}

			UI.getCurrent().setPollInterval(100);

			// Usar valor por defecto si pageSizeCombo no tiene valor
			Integer pageSizeValue = pageSizeCombo.getValue();
			if (pageSizeValue == null) {
				pageSizeValue = 50;
			}

			Pageable pageable = PageRequest.of(currentPage, pageSizeValue, currentSort);

			Page<Articulo> page;

			// Obtener valor del filtro de estado con comprobación de null
			String estadoSeleccionado = "Todos"; // valor por defecto
			if (filtroEstado != null) {
				estadoSeleccionado = filtroEstado.getValue();
				if (estadoSeleccionado == null) {
					estadoSeleccionado = "Todos";
				}
			}

			// Lógica de filtrado simplificada (sin rubro)
			if (currentFilter == null || currentFilter.trim().isEmpty()) {
				// Sin filtro de descripción
				if ("Todos".equals(estadoSeleccionado)) {
					page = service.buscarPorDescripcion("", pageable);
				} else {
					try {
						JpaArticuloEntity.Estado estadoEnum = JpaArticuloEntity.Estado.fromLabel(estadoSeleccionado);
						page = service.buscarPorEstado(estadoEnum, pageable);
					} catch (IllegalArgumentException e) {
						// Estado inválido, mostrar todos
						page = service.buscarPorDescripcion("", pageable);
					}
				}
			} else {
				// Con filtro de descripción
				if ("Todos".equals(estadoSeleccionado)) {
					page = service.buscarPorDescripcion(currentFilter, pageable);
				} else {
					try {
						JpaArticuloEntity.Estado estadoEnum = JpaArticuloEntity.Estado.fromLabel(estadoSeleccionado);
						page = service.buscarPorDescripcionYEstado(currentFilter, estadoEnum, pageable);
					} catch (IllegalArgumentException e) {
						// Estado inválido, solo filtrar por descripción
						page = service.buscarPorDescripcion(currentFilter, pageable);
					}
				}
			}

			// Actualizar datos
			grid.setItems(page.getContent());
			totalElements = page.getTotalElements();
			totalPages = page.getTotalPages();

			// Habilitar paginación
			setPaginationDisabled(false);

			// Actualizar controles
			updatePaginationControls();

		} finally {
			UI.getCurrent().setPollInterval(-1);
		}
	}

	private void updatePaginationControls() {
		// COMPROBACIÓN DE SEGURIDAD
		if (pageSizeCombo == null) {
			return;
		}

		// Actualizar información de página
		int pageSize = pageSizeCombo.getValue() != null ? pageSizeCombo.getValue() : 50;
		int from = currentPage * pageSize + 1;
		int to = Math.min((currentPage + 1) * pageSize, (int) totalElements);

		pageInfo.setText(String.format("Página %d de %d - Mostrando %d-%d de %d registros", currentPage + 1,
				Math.max(totalPages, 1), from, to, totalElements));

		// Habilitar/deshabilitar botones con comprobaciones de null
		if (firstPageButton != null)
			firstPageButton.setEnabled(currentPage > 0);
		if (prevPageButton != null)
			prevPageButton.setEnabled(currentPage > 0);
		if (nextPageButton != null)
			nextPageButton.setEnabled(currentPage < totalPages - 1);
		if (lastPageButton != null)
			lastPageButton.setEnabled(currentPage < totalPages - 1);
	}

	// Método para duplicar un artículo
	private void duplicarArticulo(Articulo articulo) {
		showLoading("Duplicando...");
		try {
			Articulo duplicado = new Articulo();
			duplicado.setCodigo_interno(articulo.getCodigo_interno());
			duplicado.setCodigo_barra(null);
			duplicado.setDescripcion(articulo.getDescripcion());
			duplicado.setNrolote(articulo.getNrolote());
			duplicado.setStock(articulo.getStock());
			duplicado.setStock_minimo(articulo.getStock_minimo());
			duplicado.setStock_maximo(articulo.getStock_maximo());
			duplicado.setLinea(articulo.getLinea());
			duplicado.setMedida(articulo.getMedida());
			duplicado.setPresentacion(articulo.getPresentacion());
			duplicado.setProveedor(articulo.getProveedor());
			duplicado.setFechaCompra(articulo.getFechaCompra());
			duplicado.setFechaVencimiento(articulo.getFechaVencimiento());
			duplicado.setFechaBaja(articulo.getFechaBaja());
			duplicado.setFechaActualizPrecios(articulo.getFechaActualizPrecios());
			duplicado.setFila(articulo.getFila());
			duplicado.setColumna(articulo.getColumna());
			duplicado.setPrecioCosto(articulo.getPrecioCosto());
			duplicado.setMargenUtilidad(articulo.getMargenUtilidad());
			duplicado.setAlicuota(articulo.getAlicuota());
			duplicado.setEsBonificado(articulo.getEsBonificado());
			duplicado.setBonificacion(articulo.getBonificacion());
			duplicado.setEstado(articulo.getEstado());

			// Guardar inmediatamente para obtener un ID,
			// luego abrir el formulario en modo edición
			Articulo guardado = service.save(duplicado);
			cargarDatosPaginados();
			form.setEntity(guardado);
			form.setVisible(true);
			grid.setHeight("350px");
			showSuccess("Artículo duplicado correctamente");
		} finally {
			hideLoading();
		}
	}

	// Método para reactivar
	private void reactivarArticulo(Articulo articulo) {
		Dialog dialogo = new Dialog();
		dialogo.setHeaderTitle("Reactivar artículo");

		Span mensaje = new Span("¿Reactivar el artículo \"" + articulo.getDescripcion() + "\"?");

		Button aceptar = new Button("Reactivar", _ -> {
			showLoading("Reactivando...");
			try {
				service.cambiarEstado(articulo.getId(), "En Existencias");
				cargarDatosPaginados();
				Notification.show("Artículo reactivado", 3000, Notification.Position.BOTTOM_CENTER)
						.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			} finally {
				hideLoading();
			}
			dialogo.close();
		});

		Button cancelar = new Button("Cancelar", _ -> dialogo.close());

		dialogo.getFooter().add(aceptar, cancelar);
		dialogo.add(mensaje);
		dialogo.open();
	}

	private void buscarPorCodigoBarra(String codigoBarra) {

		if (codigoBarra == null || codigoBarra.trim().isEmpty()) {
			return;
		}

		codigoBarra = codigoBarra.trim();

		// Obtener el header
		HorizontalLayout header = (HorizontalLayout) getComponentAt(0);

		// Crear o actualizar el indicador
		if (indicadorBusqueda == null) {
			indicadorBusqueda = new Span();
			indicadorBusqueda.getElement().getThemeList().add("badge contrast");
			indicadorBusqueda.getElement().getStyle().set("margin-left", "auto");
			header.add(indicadorBusqueda);
		}

		// Actualizar texto y hacer visible
		indicadorBusqueda.setText("Buscando código: " + codigoBarra);
		indicadorBusqueda.setVisible(true);

		showLoading("Buscando artículo...");

		try {
			Optional<Articulo> articuloOpt = service.buscarPorCodigoBarra(codigoBarra);

			if (articuloOpt.isPresent()) {
				Articulo articulo = articuloOpt.get();
				grid.setItems(List.of(articulo));
				setPaginationDisabled(true);
				grid.select(articulo);

				Notification.show("✓ Artículo encontrado: " + articulo.getDescripcion(), 3000,
						Notification.Position.BOTTOM_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			} else {
				grid.setItems(List.of());
				setPaginationDisabled(true);

				Notification
						.show("✗ No se encontró el código: " + codigoBarra, 3000, Notification.Position.BOTTOM_CENTER)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		} finally {
			hideLoading();
			// Ocultar el indicador inmediatamente (sin Timer)
			// El usuario ya ve el resultado en la notificación
			indicadorBusqueda.setVisible(false);
			indicadorBusqueda.setText("");
		}
	}

	private void setPaginationDisabled(boolean disabled) {
		if (firstPageButton != null)
			firstPageButton.setEnabled(!disabled);
		if (prevPageButton != null)
			prevPageButton.setEnabled(!disabled);
		if (nextPageButton != null)
			nextPageButton.setEnabled(!disabled);
		if (lastPageButton != null)
			lastPageButton.setEnabled(!disabled);
		if (pageSizeCombo != null)
			pageSizeCombo.setEnabled(!disabled);
	}

	private boolean estaBuscandoPorCodigoBarra() {
		// Verificar si hay un código de barra en algún campo de filtro
		// Podrías agregar un flag para controlar esto
		return false; // Cambia según tu implementación
	}

}
