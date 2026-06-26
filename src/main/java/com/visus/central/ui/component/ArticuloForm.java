package com.visus.central.ui.component;
/*
 * Formulario complejo con múltiples pestañas, campos dependientes, validaciones, formateo de valores, 
 * y un control robusto de condiciones de carrera. 
 * 
 * Formulario con pestañas - Organización clara de la información
 * Campos dependientes - Rubro → Línea con carga bajo demanda y caché
 * Formateo de valores - Precios, márgenes y porcentajes con 2 decimales
 * Validaciones complejas - Con el binder de Vaadin
 * Control de concurrencia - Evitar mezcla de datos al cambiar rápidamente entre artículos
 * Funcionalidad condicional - Campo de bonificación que se activa/desactiva según checkbox
 * Interfaz de usuario responsiva - Con feedback visual (tooltips, estados de campo)
 * Puntos clave del éxito:
 * LoadId único: Cada carga tiene su identificador para evitar mezclas
 * Cancelación inteligente: Las cargas antiguas se cancelan cuando llegan nuevas
 * Caché de datos: Mejora el rendimiento al reutilizar líneas ya cargadas
 * Listeners controlados: Se desactivan durante las actualizaciones programáticas
 * 
 * */

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.visus.central.application.port.in.UnidadUseCase;
import com.visus.central.domain.model.Alicuota;
import com.visus.central.domain.model.Articulo;
import com.visus.central.domain.model.Linea;
import com.visus.central.domain.model.Medida;
import com.visus.central.domain.model.Presentacion;
import com.visus.central.domain.model.Proveedor;
import com.visus.central.domain.model.Rubro;
import com.visus.central.domain.model.Unidad;
import com.visus.central.domain.model.UnidadConCantidad;
import com.visus.central.domain.port.in.AlicuotaUseCase;
import com.visus.central.domain.port.in.ArticuloUseCase;
import com.visus.central.domain.port.in.LineaUseCase;
import com.visus.central.domain.port.in.MedidaUseCase;
import com.visus.central.domain.port.in.PresentacionUseCase;
import com.visus.central.domain.port.in.ProveedorUseCase;
import com.visus.central.domain.port.in.RubroUseCase;
import com.visus.central.domain.model.Estado;
import com.visus.central.domain.model.EstadoArticulo;
import com.visus.central.infraestructure.util.Ean13Generator;
import com.visus.central.infraestructure.util.FormatoUtils;
import com.visus.central.infraestructure.util.SpringContextHelper;

public class ArticuloForm extends AbstractForm<Articulo> {

	private static final long serialVersionUID = 1L;

	// Campos principales
	private TextField codigoInterno = new TextField("Código Interno");
	private TextField codigoBarra = new TextField("Código de Barra");
	private TextField nroLote = new TextField("Número de Lote");
	private TextField descripcion = new TextField("Descripción");

	private IntegerField stock = new IntegerField("En Stock");
	private IntegerField stockMinimo = new IntegerField("Stock Mínimo");
	private IntegerField stockMaximo = new IntegerField("Stock Máximo");

	private final ComboBox<Linea> linea = new ComboBox<>("Línea");
	private final ComboBox<Rubro> rubro = new ComboBox<>("Rubro");

	private final ComboBox<Medida> medida = new ComboBox<>("Medida");
	private final ComboBox<Presentacion> presentacion = new ComboBox<>("Presentación");
	private final ComboBox<Proveedor> proveedor = new ComboBox<>("Proveedor");
	private final UnidadSelector unidades = new UnidadSelector("Pares / Talles");

	private DatePicker fechaCompra = new DatePicker("Fecha de Compra");
	private DatePicker fechaVencimiento = new DatePicker("Fecha de Vencimiento");
	private DatePicker fechaBaja = new DatePicker("Fecha de Baja");
	private DatePicker fechaActPrecios = new DatePicker("Fecha de Actualización de Precios");

	private IntegerField fila = new IntegerField("Fila");
	private IntegerField columna = new IntegerField("Columna");

	private BigDecimalField precioCosto = new BigDecimalField("Precio de Costo");
	private BigDecimalField margenUtilidad = new BigDecimalField("Margen de Utilidad");

	private final ComboBox<Alicuota> alicuota = new ComboBox<>("Alicuota");
	private TextField gravamenDisplay = new TextField();
	private BigDecimalField precioConIvaDisplay = new BigDecimalField("Precio + IVA");
	private BigDecimalField precioVentaDisplay = new BigDecimalField("Precio de Venta");

	private Checkbox esBonificado = new Checkbox("Artículo Bonificado");
	private BigDecimalField bonificacion = new BigDecimalField("Bonificación");

	private ComboBox<EstadoArticulo> estado = new ComboBox<>("Estado");

	// Listas completas para filtrar
	private List<Rubro> todosLosRubros = new ArrayList<>();
	private List<Medida> todasLasMedidas = new ArrayList<>();
	private List<Presentacion> todasLasPresentaciones = new ArrayList<>();
	private List<Proveedor> todosLosProveedores = new ArrayList<>();
	private List<Alicuota> todasLasAlicuotas = new ArrayList<>();

	// Services/UseCases
	private LineaUseCase lineaUseCase;
	private RubroUseCase rubroUseCase;
	private MedidaUseCase medidaUseCase;
	private PresentacionUseCase presentacionUseCase;
	private ProveedorUseCase proveedorUseCase;
	private AlicuotaUseCase alicuotaUseCase;
	private UnidadUseCase unidadUseCase;
	private ArticuloUseCase articuloUseCase;

	private Map<Integer, List<Linea>> cacheLineasPorRubro = new HashMap<>();

	// Control de carga simultánea - SOLUCIÓN SIMPLIFICADA
    private volatile long currentLoadId = 0;
    private volatile boolean isUpdating = false;

	public ArticuloForm() {
		super(Articulo.class);

		// Cargar CSS personalizado (articulos.css)
		getStyle().set("padding", "0");

		this.lineaUseCase = SpringContextHelper.getBean(LineaUseCase.class);
		this.rubroUseCase = SpringContextHelper.getBean(RubroUseCase.class);
		this.medidaUseCase = SpringContextHelper.getBean(MedidaUseCase.class);
		this.presentacionUseCase = SpringContextHelper.getBean(PresentacionUseCase.class);
		this.proveedorUseCase = SpringContextHelper.getBean(ProveedorUseCase.class);
		this.alicuotaUseCase = SpringContextHelper.getBean(AlicuotaUseCase.class);
		this.unidadUseCase = SpringContextHelper.getBean(UnidadUseCase.class);
		this.articuloUseCase = SpringContextHelper.getBean(ArticuloUseCase.class);

		buildLayout();
		configurarFormatoDecimalesConListeners();
		cargarDatosIniciales();
		configurarBonificacion();
		configurarListeners();
	}

	private void buildLayout() {

		String campoStyle = "campo-estilo-imagen";

		// Configurar campos principales
		configurarCamposBasicos(campoStyle);

		// Crear TabSheet
		TabSheet tabSheet = new TabSheet();
		tabSheet.setWidthFull();

		// Crear pestañas básicas
		tabSheet.add("Información Básica", crearPestañaInformacionBasica());
		tabSheet.add("Categorización", crearPestañaCategorizacion());
		tabSheet.add("Fechas y Ubicación", crearPestañaFechasUbicacion());
		tabSheet.add("Precios y Financiero", crearPestañaPreciosFinanciero());
		tabSheet.add("Estado", crearPestañaEstado());

		// Layout principal
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.setPadding(true);
		mainLayout.setWidthFull();

		mainLayout.add(tabSheet, buildBotonera());
		add(mainLayout);

		bindFields();

	}

	private void configurarCamposBasicos(String campoStyle) {
		// Configurar campos principales
		codigoInterno.addClassName(campoStyle);
		codigoInterno.setWidth("160px");
		codigoBarra.addClassName(campoStyle);
		codigoBarra.setWidth("160px");
		codigoBarra.addBlurListener(_ -> {
			if (codigoBarra.getValue() == null || codigoBarra.getValue().isBlank()) {
				String nuevoCodigo = Ean13Generator.generar();
				codigoBarra.setValue(nuevoCodigo);
			}
		});

		nroLote.addClassName(campoStyle);
		nroLote.setWidth("160px");

		descripcion.addClassName(campoStyle);
		descripcion.setWidth("480px");

		stock.addClassName(campoStyle);
		stock.setWidth("120px");
		stockMinimo.addClassName(campoStyle);
		stockMinimo.setWidth("120px");
		stockMaximo.addClassName(campoStyle);
		stockMaximo.setWidth("120px");

		rubro.addClassName(campoStyle);
		rubro.setWidth("250px");
		linea.addClassName(campoStyle);
		linea.setWidth("250px");
		medida.addClassName(campoStyle);
		medida.setWidth("250px");
		presentacion.addClassName(campoStyle);
		presentacion.setWidth("250px");
		proveedor.addClassName(campoStyle);
		proveedor.setWidth("250px");

		unidades.addClassName(campoStyle);

		fechaCompra.addClassName(campoStyle);
		fechaCompra.setWidth("150px");
		fechaVencimiento.addClassName(campoStyle);
		fechaVencimiento.setWidth("150px");
		fechaBaja.addClassName(campoStyle);;
		fechaBaja.setWidth("150px");
		fechaActPrecios.addClassName(campoStyle);
		fechaActPrecios.setWidth("150px");

		fila.addClassName(campoStyle);
		fila.setWidth("100px");
		columna.addClassName(campoStyle);
		columna.setWidth("100px");

		precioCosto.addClassName(campoStyle);
		precioCosto.setWidth("150px");
		margenUtilidad.addClassName(campoStyle);
		margenUtilidad.setWidth("150px");

		alicuota.addClassName(campoStyle);
		alicuota.setWidth("250px");

		// Configurar campo de gravamen (solo lectura)
		gravamenDisplay.setWidth("100px");
		gravamenDisplay.setReadOnly(true);
		gravamenDisplay.setSuffixComponent(new Span("%"));
		gravamenDisplay.setTooltipText("Porcentaje de gravamen de la alícuota seleccionada");
		gravamenDisplay.getStyle()
		.set("background-color", "var(--lumo-contrast-5pct)")
		.set("color", "var(--lumo-secondary-text-color)")
		.set("font-weight", "500");

		precioConIvaDisplay.setReadOnly(true);
		precioConIvaDisplay.setWidth("150px");
		precioConIvaDisplay.getStyle()
		.set("background-color", "var(--lumo-contrast-5pct)")
		.set("color", "var(--lumo-secondary-text-color)")
		.set("font-weight", "500");

		precioVentaDisplay.setReadOnly(true);
		precioVentaDisplay.setWidth("150px");
		precioVentaDisplay.getStyle()
		.set("background-color", "var(--lumo-contrast-5pct)")
		.set("color", "var(--lumo-secondary-text-color)")
		.set("font-weight", "500");

		// Configurar campo bonificación
		bonificacion.addClassName(campoStyle);
		bonificacion.setWidth("120px");
		Span porcentajeSuffix = new Span("%");
		bonificacion.setSuffixComponent(porcentajeSuffix);
		bonificacion.setClearButtonVisible(true);
		bonificacion.setTooltipText("Porcentaje de bonificación (0-100%)");

		estado.addClassName(campoStyle);
		estado.setWidth("250px");
		estado.setItems(Arrays.asList(EstadoArticulo.values()));
		estado.setItemLabelGenerator(EstadoArticulo::getLabel);
	}

	private VerticalLayout crearPestañaInformacionBasica() {

		VerticalLayout tab = new VerticalLayout();
		tab.setWidthFull();

		// Grupo 1: Datos del artículo
		VerticalLayout grupo1 = new VerticalLayout();
		grupo1.setPadding(false);
		grupo1.add(crearEtiquetaGrupo("Datos del Artículo"));

		HorizontalLayout datosBasicos = new HorizontalLayout();
		datosBasicos.setSpacing(true);
		datosBasicos.add(codigoInterno, codigoBarra, nroLote, descripcion);
		grupo1.add(datosBasicos);

		// Grupo 2: Control de stock
		VerticalLayout grupo2 = new VerticalLayout();
		grupo2.setPadding(false);
		grupo2.add(crearEtiquetaGrupo("Control de Stock"));

		HorizontalLayout stockLayout = new HorizontalLayout();
		stockLayout.setSpacing(true);
		stockLayout.add(stock, stockMinimo, stockMaximo);
		grupo2.add(stockLayout);

		tab.add(grupo1, grupo2);
		return tab;
	}

	private VerticalLayout crearPestañaCategorizacion() {
		VerticalLayout tab = new VerticalLayout();
		tab.setWidthFull();
		tab.setPadding(false);

		// Grupo 1: Clasificación
		VerticalLayout grupo1 = new VerticalLayout();
		grupo1.setPadding(false);
		grupo1.add(crearEtiquetaGrupo("Clasificación"));

		HorizontalLayout clasificacion = new HorizontalLayout();
		clasificacion.setSpacing(true);
		clasificacion.add(rubro, linea);
		grupo1.add(clasificacion);

		// Grupo 2: Especificaciones
		VerticalLayout grupo2 = new VerticalLayout();
		grupo2.setPadding(false);
		grupo2.add(crearEtiquetaGrupo("Especificaciones"));

		HorizontalLayout especificaciones = new HorizontalLayout();
		especificaciones.setSpacing(true);
		especificaciones.add(medida, presentacion, proveedor);
		grupo2.add(especificaciones);

		// Unidades (pares/talles)
		Span tituloUnidades = new Span("Pares / Talles - Cantidades");
		tituloUnidades.getElement().getThemeList().add("badge contrast");
		unidades.setVisible(false);

		tab.add(grupo1, grupo2, tituloUnidades, unidades);
		return tab;
	}

	private VerticalLayout crearPestañaFechasUbicacion() {
		VerticalLayout tab = new VerticalLayout();
		tab.setWidthFull();

		// Grupo 1: Fechas
		VerticalLayout grupo1 = new VerticalLayout();
		grupo1.setPadding(false);
		grupo1.add(crearEtiquetaGrupo("Fechas importantes"));

		HorizontalLayout fechasLayout1 = new HorizontalLayout();
		fechasLayout1.setSpacing(true);
		fechasLayout1.add(fechaCompra, fechaVencimiento, fechaBaja, fechaActPrecios);

		grupo1.add(fechasLayout1);

		// Grupo 2: Ubicación
		VerticalLayout grupo2 = new VerticalLayout();
		grupo2.setPadding(false);
		grupo2.add(crearEtiquetaGrupo("Ubicación en almacén"));

		HorizontalLayout ubicacionLayout = new HorizontalLayout();
		ubicacionLayout.setSpacing(true);
		ubicacionLayout.add(fila, columna);
		grupo2.add(ubicacionLayout);

		tab.add(grupo1, grupo2);
		return tab;
	}

	private HorizontalLayout crearPestañaPreciosFinanciero() {
		HorizontalLayout tab = new HorizontalLayout();
		tab.setWidthFull();

		// Grupo 1: Precios base
		VerticalLayout grupo1 = new VerticalLayout();
		grupo1.setPadding(false);
		grupo1.add(crearEtiquetaGrupo("Precios base"));

		HorizontalLayout preciosLayout = new HorizontalLayout();
		preciosLayout.setSpacing(true);
		preciosLayout.add(precioCosto, margenUtilidad);
		grupo1.add(preciosLayout);

		HorizontalLayout preciosCalculadosLayout = new HorizontalLayout();
		preciosCalculadosLayout.setSpacing(true);
		preciosCalculadosLayout.add(precioConIvaDisplay, precioVentaDisplay);
		grupo1.add(preciosCalculadosLayout);

		// Grupo 2: Tributación
		VerticalLayout grupo2 = new VerticalLayout();
		grupo2.setPadding(false);
		grupo2.add(crearEtiquetaGrupo("Tributación"));
		grupo2.add(alicuota);
		grupo2.add(gravamenDisplay);

		// Grupo 3: Bonificaciones
		VerticalLayout grupo3 = new VerticalLayout();
		grupo3.setPadding(false);
		grupo3.add(crearEtiquetaGrupo("Bonificaciones"));

		HorizontalLayout bonificacionLayout = new HorizontalLayout();
		bonificacionLayout.setSpacing(true);
		bonificacionLayout.setAlignItems(Alignment.CENTER);
		bonificacionLayout.add(esBonificado, bonificacion);
		grupo3.add(bonificacionLayout);

		tab.add(grupo1, grupo2, grupo3);
		return tab;
	}

	private VerticalLayout crearPestañaEstado() {
		VerticalLayout tab = new VerticalLayout();
		tab.setWidthFull();

		VerticalLayout grupo = new VerticalLayout();
		grupo.setPadding(false);
		grupo.add(crearEtiquetaGrupo("Estado del artículo"));
		grupo.add(estado);

		tab.add(grupo);
		return tab;
	}

	@Override
	public void setEntity(Articulo entity) {
		if (entity != null && entity.getId() != null) {
			try {
				entity = articuloUseCase.findById(entity.getId());
			} catch (Exception e) {
				System.err.println("Error al recargar artículo ID=" + entity.getId() + ": " + e.getMessage());
			}
		}
		super.setEntity(entity);
	}

	private void cargarDatosIniciales() {
		try {
			// Cargar rubros
			todosLosRubros = rubroUseCase.listar();
			rubro.setItems(todosLosRubros);
			rubro.setItemLabelGenerator(Rubro::getDescripcion);

			// No cargar todas las líneas inicialmente
			linea.setItems(new ArrayList<>());
			linea.setItemLabelGenerator(Linea::getDescripcion);
			linea.setEnabled(false);

			// Cargar medidas
			todasLasMedidas = medidaUseCase.listar();
			medida.setItems(todasLasMedidas.stream()
					.sorted(Comparator.comparing(Medida::getDescripcion))
					.collect(Collectors.toList()));
			medida.setItemLabelGenerator(Medida::getDescripcion);

			// Cargar presentaciones
			todasLasPresentaciones = presentacionUseCase.listar();
			presentacion.setItems(todasLasPresentaciones.stream()
					.sorted(Comparator.comparing(Presentacion::getDescripcion))
					.collect(Collectors.toList()));
			presentacion.setItemLabelGenerator(Presentacion::getDescripcion);

			// Cargar proveedores
			todosLosProveedores = proveedorUseCase.findAll();
			proveedor.setItems(todosLosProveedores.stream()
					.sorted(Comparator.comparing(Proveedor::getNombreFantasia))
					.collect(Collectors.toList()));
			proveedor.setItemLabelGenerator(Proveedor::getNombreFantasia);

			// Cargar Alícuotas
			todasLasAlicuotas = alicuotaUseCase.findAll();
			alicuota.setItems(todasLasAlicuotas.stream()
					.sorted(Comparator.comparing(Alicuota::getDescripcion))
					.collect(Collectors.toList()));
			alicuota.setItemLabelGenerator(Alicuota::getDescripcion);

		} catch (Exception e) {
			System.err.println("❌ Error al cargar datos iniciales: " + e.getMessage());
			e.printStackTrace();

			Notification.show("Error al cargar datos: " + e.getMessage(), 
					5000, Notification.Position.MIDDLE)
			.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}

	}

	private void configurarListeners() {
		 // Cuando cambia el Rubro, cargar líneas de ese rubro
        rubro.addValueChangeListener(event -> {
            if (isUpdating) {
                System.out.println("⚠️ Listener de rubro ignorado durante carga");
                return;
            }

            Rubro rubroSeleccionado = event.getValue();
            System.out.println("Rubro seleccionado: " + 
                (rubroSeleccionado != null ? rubroSeleccionado.getDescripcion() : "Ninguno"));

            if (rubroSeleccionado != null) {
                cargarLineasParaRubroSeleccionado(rubroSeleccionado.getId());
            } else {
                // Si no hay rubro seleccionado, no mostrar líneas
                linea.setItems(new ArrayList<>());
                linea.setEnabled(false);
                linea.setPlaceholder("Primero seleccione un rubro");
            }
        });

     // Listener para Línea: cuando cambia, actualizar Rubro si es diferente
        linea.addValueChangeListener(event -> {
            if (isUpdating) return;
            
            Linea lineaSeleccionada = event.getValue();
            if (lineaSeleccionada != null && lineaSeleccionada.getRubro() != null) {
                // Verificar si el rubro actual es diferente al de la línea
                if (rubro.getValue() == null || 
                    !rubro.getValue().getId().equals(lineaSeleccionada.getRubro().getId())) {
                    
                    rubro.setValue(lineaSeleccionada.getRubro());
                }
            }
        });

		// Listener para alícuota
        alicuota.addValueChangeListener(event -> {
            if (isUpdating) return;
            
            if (event.getValue() != null) {
                Alicuota alicuotaSeleccionada = event.getValue();
                BigDecimal gravamenValue = alicuotaSeleccionada.getGravamen();

                if (gravamenValue != null && gravamenValue.compareTo(BigDecimal.ZERO) > 0) {
                    String gravamenFormateado = FormatoUtils.formatPorcentaje(gravamenValue, false);
                    String soloNumero = gravamenFormateado.replace(" %", "");
                    gravamenDisplay.setValue(soloNumero);
                    gravamenDisplay.setTooltipText("Gravamen: " + gravamenFormateado);
                } else {
                    gravamenDisplay.setValue("No aplica");
                    gravamenDisplay.setTooltipText("Esta alícuota no tiene gravamen");
                }
            } else {
                gravamenDisplay.clear();
                gravamenDisplay.setTooltipText("Seleccione una alícuota para ver el gravamen");
            }
			actualizarPreciosCalculados();
        });

		precioCosto.addValueChangeListener(_ -> actualizarPreciosCalculados());
		margenUtilidad.addValueChangeListener(_ -> actualizarPreciosCalculados());

		// Cuando cambia la presentación, cargar unidades disponibles
		presentacion.addValueChangeListener(event -> {
			if (isUpdating) return;
			Presentacion p = event.getValue();
			if (p != null && p.getId() != null) {
				List<Unidad> unidadesDisponibles = unidadUseCase.buscarPorPresentacion(p.getId());
				unidades.setItems(unidadesDisponibles);
				unidades.setVisible(true);
			} else {
				unidades.setItems(new ArrayList<>());
				unidades.setVisible(false);
			}
		});

		// Auto-completar stock según pares/talles seleccionados
		unidades.addValueChangeListener(value -> {
			if (isUpdating) return;
			int count = value.stream().mapToInt(UnidadConCantidad::getCantidad).sum();
			stock.setValue(count);
		});

	}
	
	private void cargarLineasParaRubroSeleccionado(Integer idRubro) {
        try {
            System.out.println("🔍 Cargando líneas para rubro seleccionado ID: " + idRubro);

            List<Linea> lineasDelRubro;
            
            // Verificar caché
            if (cacheLineasPorRubro.containsKey(idRubro)) {
                lineasDelRubro = cacheLineasPorRubro.get(idRubro);
                System.out.println("📊 Líneas desde caché: " + lineasDelRubro.size());
            } else {
                // Cargar del servicio
                lineasDelRubro = lineaUseCase.findByRubroId(idRubro);
                lineasDelRubro.sort(Comparator.comparing(Linea::getDescripcion));
                cacheLineasPorRubro.put(idRubro, lineasDelRubro);
                System.out.println("📊 Líneas cargadas del servicio: " + lineasDelRubro.size());
            }

            // Actualizar el combo de líneas
            linea.setItems(lineasDelRubro);
            linea.setEnabled(true);
            linea.setPlaceholder("Seleccione una línea (" + lineasDelRubro.size() + " disponibles)");
            
            // Si había una línea seleccionada que no pertenece al nuevo rubro, limpiarla
            if (linea.getValue() != null && linea.getValue().getRubro() != null &&
                !idRubro.equals(linea.getValue().getRubro().getId())) {
                linea.clear();
            }

        } catch (Exception e) {
            System.err.println("❌ Error al cargar líneas: " + e.getMessage());
            linea.setItems(new ArrayList<>());
            linea.setPlaceholder("Error al cargar líneas");
        }
    }
	

	@Override
	protected void bindFields() {

		binder.forField(codigoInterno)
		.asRequired("El Código Interno es Obligatorio")
		.bind(Articulo::getCodigo_interno, Articulo::setCodigo_interno);

		binder.forField(codigoBarra)
		.bind(Articulo::getCodigo_barra, Articulo::setCodigo_barra);

		binder.forField(descripcion)
		.asRequired("La Descripción es Obligatoria")
		.bind(Articulo::getDescripcion, Articulo::setDescripcion);

		binder.forField(stock)
		.asRequired("La cantidad en Stock es Obligatoria")
		.bind(Articulo::getStock, Articulo::setStock);

		binder.forField(stockMinimo)
		.bind(Articulo::getStock_minimo, Articulo::setStock_minimo);

		binder.forField(stockMaximo)
		.bind(Articulo::getStock_maximo, Articulo::setStock_maximo);

		// VALIDACIÓN PARA LÍNEA Y RUBRO
		// Si el usuario seleccionó la línea del ComboBox, debería ser válida. 

		binder.forField(linea)
		.asRequired("Debe seleccionar una línea")
		.withValidator(linea -> 
		linea.getIdLinea() != null && 
		linea.getDescripcion() != null && 
		!linea.getDescripcion().trim().isEmpty(), 
				"La línea seleccionada no es válida")
		.withValidator(linea -> linea.getRubro() != null, 
				"La línea seleccionada no tiene rubro asociado")
		.withValidator(linea -> {
			Rubro rubroSeleccionado = rubro.getValue();
			// Si no hay rubro seleccionado, es válido (la línea trae su rubro)
			if (rubroSeleccionado == null) return true;
			// Si hay rubro seleccionado, deben coincidir
			return rubroSeleccionado.getId().equals(linea.getRubro().getId());
		}, "La línea seleccionada no pertenece al rubro elegido")
		.bind(Articulo::getLinea, Articulo::setLinea);

		// VALIDACIÓN PARA MEDIDA
		binder.forField(medida)
		.asRequired("Debe seleccionar una medida")
		.withValidator(medida -> {
			if (medida == null) return false;

			// Validar que la medida tenga ID y descripción
			boolean valido = medida.getId() != null && 
					medida.getDescripcion() != null && 
					!medida.getDescripcion().trim().isEmpty();

			if (!valido) {
				System.out.println("❌ Medida inválida: " + medida);
			}

			return valido;
		}, "La medida seleccionada no es válida")
		.bind(Articulo::getMedida, Articulo::setMedida);

		// VALIDACIÓN PARA PRESENTACIÓN
		binder.forField(presentacion)
		.asRequired("Debe seleccionar una presentación")
		.withValidator(presentacion -> {
			if (presentacion == null) return false;

			boolean valido = presentacion.getId() != null && 
					presentacion.getDescripcion() != null && 
					!presentacion.getDescripcion().trim().isEmpty();

			if (!valido) {
				System.out.println("❌ Presentación inválida: " + presentacion);
			}

			return valido;
		}, "La presentación seleccionada no es válida")
		.bind(Articulo::getPresentacion, Articulo::setPresentacion);

		// VALIDACIÓN PARA PROVEEDOR
		binder.forField(proveedor)
		.asRequired("Debe seleccionar un proveedor")
		.withValidator(proveedor -> {
			if (proveedor == null) return false;

			// Validación básica del proveedor
			boolean valido = proveedor.getId() != null && 
					proveedor.getNombreFantasia() != null && 
					!proveedor.getNombreFantasia().trim().isEmpty();

			if (!valido) {
				System.out.println("❌ Proveedor inválido: " + proveedor);
			}

			// Validación adicional: el proveedor debe estar activo
			if (valido && proveedor.getEstado() != null) {
				boolean habilitado = proveedor.getEstado().equals(Estado.Habilitado);
				if (!habilitado) {
					System.out.println("Proveedor inactivo: " + proveedor.getNombreFantasia());
					Notification.show("Proveedor inactivo. Asigne otro Proveedor", 
							5000, Notification.Position.MIDDLE)
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
					// Puedes decidir si permitir proveedores inactivos
					// return false; // Si no quieres permitir proveedores inactivos
				}
			}
			return valido;
		}, "El proveedor seleccionado no es válido")
		.bind(Articulo::getProveedor, Articulo::setProveedor);

		// Binding de fechas
		binder.bind(fechaCompra, Articulo::getFechaCompra, Articulo::setFechaCompra);
		binder.bind(fechaVencimiento, Articulo::getFechaVencimiento, Articulo::setFechaVencimiento);
		binder.bind(fechaBaja, Articulo::getFechaBaja, Articulo::setFechaBaja);
		binder.bind(fechaActPrecios, Articulo::getFechaActualizPrecios, Articulo::setFechaActualizPrecios);

		binder.forField(fila)
		.asRequired("La Fila es Obligatoria")
		.bind(Articulo::getFila, Articulo::setFila);

		binder.forField(columna)
		.asRequired("La Columna es Obligatoria")
		.bind(Articulo::getColumna, Articulo::setColumna);

		// Precio de Costo - no negativo
		binder.forField(precioCosto) // BigDecimalField -> trabaja con BigDecimal
		.withValidator(value -> value == null || value.compareTo(BigDecimal.ZERO) >= 0, 
				"El Precio de Costo no puede ser negativo")
		.withValidator(value -> value == null || value.scale() <= 2,
				"El Precio de Costo no puede tener más de 2 decimales")
		.bind(Articulo::getPrecioCosto, Articulo::setPrecioCosto);

		binder.forField(margenUtilidad)
		.withValidator(value -> value == null || value.compareTo(BigDecimal.ZERO) >= 0, 
				"El Margen de Utilidad no puede ser negativo")
		.withValidator(value -> value == null || value.scale() <= 2,
				"El Margen de Utilidad no puede tener más de 2 decimales")
		.withValidator(value -> value == null || value.precision() <= 18,
				"El pago mínimo no puede tener más de 18 dígitos")
		.bind(Articulo::getMargenUtilidad, Articulo::setMargenUtilidad);

		// VALIDACIÓN PARA ALÍCUOTAS
		binder.forField(alicuota)
		.asRequired("Debe seleccionar una Alícuota")
		.withValidator(alicuota -> {
			if (alicuota == null) return false;

			boolean valido = alicuota.getId() != null && 
					alicuota.getDescripcion() != null && 
					!alicuota.getDescripcion().trim().isEmpty();

			if (!valido) {
				System.out.println("❌ Alícuota inválida: " + alicuota);
			}

			return valido;
		}, "La Alícuota seleccionada no es válida")
		.bind(Articulo::getAlicuota, Articulo::setAlicuota);

		// Binding para esBonificado
		binder.forField(esBonificado)
		.bind(Articulo::getEsBonificado, Articulo::setEsBonificado);

		// Binding para bonificación con lógica condicional
		binder.forField(bonificacion)
		.withValidator(value -> {
			// Si no está bonificado, cualquier valor (incluido null) es válido
			if (!Boolean.TRUE.equals(esBonificado.getValue())) {
				return true;
			}
			// Si está bonificado, no puede ser null y debe ser mayor que 0
			if (value == null) {
				return false;
			}
			return value.compareTo(BigDecimal.ZERO) > 0;
		}, "La Bonificación es requerida cuando el artículo está bonificado")
		.withValidator(value -> {
			if (!Boolean.TRUE.equals(esBonificado.getValue())) {
				return true;
			}
			if (value == null) return false;
			return value.compareTo(new BigDecimal("100")) <= 0;
		}, "La Bonificación no puede ser mayor a 100%")
		.withValidator(value -> {
			if (!Boolean.TRUE.equals(esBonificado.getValue())) {
				return true;
			}
			if (value == null) return false;
			return value.scale() <= 2;
		}, "No puede tener más de 2 decimales")
		.bind(Articulo::getBonificacion, Articulo::setBonificacion);

		binder.bind(estado, Articulo::getEstado, Articulo::setEstado);

	}

	@Override
	protected void setFormValues(Articulo entity) {
		if (entity == null) {
            clearForm();
            return;
        }

        // Generar un nuevo ID para esta carga
        long thisLoadId = System.currentTimeMillis();
        currentLoadId = thisLoadId;
        isUpdating = true;

        System.out.println("=== INICIANDO CARGA ARTÍCULO ID: " + entity.getId() + " (LoadId: " + thisLoadId + ") ===");

        try {
            // Limpiar primero de forma síncrona
            limpiarCamposSincrono();
            
            // Verificar si esta carga sigue siendo la actual
            if (currentLoadId != thisLoadId) {
                System.out.println("⚠️ Carga cancelada para ID: " + entity.getId());
                return;
            }
            
            // Cargar campos básicos
            cargarCamposBasicos(entity, thisLoadId);
            
            // Verificar si esta carga sigue siendo la actual
            if (currentLoadId != thisLoadId) {
                System.out.println("⚠️ Carga cancelada para ID: " + entity.getId());
                return;
            }
            
            // Cargar campos con dependencias
            cargarCamposDependientes(entity, thisLoadId);
            
            System.out.println("✅ CARGA COMPLETADA PARA ARTÍCULO ID: " + entity.getId() + " (LoadId: " + thisLoadId + ")");
            
        } finally {
            if (currentLoadId == thisLoadId) {
                isUpdating = false;
            }
        }
	}

	private void limpiarCamposSincrono() {
		codigoInterno.clear();
		codigoBarra.clear();
		nroLote.clear();
		descripcion.clear();
		stock.clear();
		stockMinimo.setValue(0);
		stockMaximo.setValue(0);
		rubro.clear();
		linea.clear();
		medida.clear();
		presentacion.clear();
		proveedor.clear();
		fechaCompra.clear();
		fechaVencimiento.clear();
		fechaBaja.clear();
		fechaActPrecios.clear();
		fila.clear();
		columna.clear();
		precioCosto.clear();
		margenUtilidad.clear();
		alicuota.clear();
		gravamenDisplay.clear();
		esBonificado.setValue(false);
		bonificacion.clear();
		estado.clear();

		linea.setItems(new ArrayList<>());
		linea.setEnabled(false);
		linea.setPlaceholder("Primero seleccione un rubro");

		unidades.setItems(new ArrayList<>());
		unidades.setVisible(false);
	}

	private void cargarCamposBasicos(Articulo entity, long loadId) {
        if (currentLoadId != loadId) return;

        codigoInterno.setValue(entity.getCodigo_interno() != null ? entity.getCodigo_interno() : "");
        codigoBarra.setValue(entity.getCodigo_barra() != null ? entity.getCodigo_barra() : "");
        nroLote.setValue(entity.getNrolote() != null ? entity.getNrolote() : "");
        descripcion.setValue(entity.getDescripcion() != null ? entity.getDescripcion() : "");

        stock.setValue(entity.getStock() != null ? entity.getStock().intValue() : null);
        stockMinimo.setValue(entity.getStock_minimo() != null ? entity.getStock_minimo().intValue() : null);
        stockMaximo.setValue(entity.getStock_maximo() != null ? entity.getStock_maximo().intValue() : null);

        fila.setValue(entity.getFila() != null ? entity.getFila().intValue() : null);
        columna.setValue(entity.getColumna() != null ? entity.getColumna().intValue() : null);

        fechaCompra.setValue(entity.getFechaCompra());
        fechaVencimiento.setValue(entity.getFechaVencimiento());
        fechaBaja.setValue(entity.getFechaBaja());
        fechaActPrecios.setValue(entity.getFechaActualizPrecios());

        estado.setValue(entity.getEstado());
	}

	private void cargarCamposDependientes(Articulo entity, long loadId) {
        if (currentLoadId != loadId) return;

        if (entity.getPrecioCosto() != null) {
            BigDecimal precioRedondeado = entity.getPrecioCosto().setScale(2, RoundingMode.HALF_UP);
            precioCosto.setValue(precioRedondeado);
        }

        if (entity.getMargenUtilidad() != null) {
            BigDecimal margenRedondeado = entity.getMargenUtilidad().setScale(2, RoundingMode.HALF_UP);
            margenUtilidad.setValue(margenRedondeado);
        }

        if (entity.getAlicuota() != null) {
            alicuota.setValue(entity.getAlicuota());
            BigDecimal gravamenValue = entity.getAlicuota().getGravamen();
            if (gravamenValue != null && gravamenValue.compareTo(BigDecimal.ZERO) > 0) {
                String gravamenFormateado = FormatoUtils.formatPorcentaje(gravamenValue, false);
                String soloNumero = gravamenFormateado.replace(" %", "");
                gravamenDisplay.setValue(soloNumero);
                gravamenDisplay.setTooltipText("Gravamen: " + gravamenFormateado);
            } else {
                gravamenDisplay.setValue("No aplica");
                gravamenDisplay.setTooltipText("Esta alícuota no tiene gravamen");
            }
        }

        esBonificado.setValue(entity.getEsBonificado());
        if (entity.getBonificacion() != null) {
            BigDecimal bonificacionRedondeada = entity.getBonificacion().setScale(2, RoundingMode.HALF_UP);
            bonificacion.setValue(bonificacionRedondeada);
            bonificacion.setReadOnly(!Boolean.TRUE.equals(entity.getEsBonificado()));
        } else {
            bonificacion.setReadOnly(true);
        }

        medida.setValue(entity.getMedida());
        proveedor.setValue(entity.getProveedor());

        if (entity.getPresentacion() != null && entity.getPresentacion().getId() != null) {
            List<Unidad> unidadesDisponibles = unidadUseCase.buscarPorPresentacion(
                entity.getPresentacion().getId());
            unidades.setItems(unidadesDisponibles);
            unidades.setVisible(true);
            if (entity.getUnidades() != null && !entity.getUnidades().isEmpty()) {
                unidades.setValue(new java.util.LinkedHashSet<>(entity.getUnidades()));
            }
        }

        presentacion.setValue(entity.getPresentacion());

        cargarRubroYLinea(entity, loadId);
	}

	private void cargarRubroYLinea(Articulo entity, long loadId) {
		if (entity.getLinea() != null) {
	        Linea lineaArticulo = entity.getLinea();
	        System.out.println("El artículo tiene línea: " + lineaArticulo.getDescripcion());

	        if (lineaArticulo.getRubro() != null) {
	            // Establecer rubro (esto puede disparar listeners, así que hay que tener cuidado)
	            rubro.setValue(lineaArticulo.getRubro());
	            System.out.println("Rubro establecido: " + lineaArticulo.getRubro().getDescripcion());

	            // Cargar líneas para este rubro de forma síncrona
	            cargarLineasPorRubroSincrono(lineaArticulo.getRubro().getId(), lineaArticulo, loadId);
	        } else {
	            rubro.clear();
	            linea.setValue(lineaArticulo);
	        }
	    }
	}
	
	private void cargarLineasPorRubroSincrono(Integer idRubro, Linea lineaObjetivo, long loadId) {
	    try {
	        // Verificar que todavía estamos en la misma carga
	        if (currentLoadId != loadId) {
	            System.out.println("⚠️ Carga de líneas cancelada - LoadId inválido");
	            return;
	        }

	        System.out.println("🔍 Cargando líneas para rubro ID: " + idRubro + " (LoadId: " + loadId + ")");
	        List<Linea> lineasDelRubro;
	        
	        // Verificar caché primero
	        if (cacheLineasPorRubro.containsKey(idRubro)) {
	            lineasDelRubro = cacheLineasPorRubro.get(idRubro);
	            System.out.println("📊 Líneas desde caché: " + lineasDelRubro.size());
	        } else {
	            // Cargar de forma síncrona
	            lineasDelRubro = lineaUseCase.findByRubroId(idRubro);
	            
	            // Ordenar
	            lineasDelRubro.sort(Comparator.comparing(Linea::getDescripcion));
	            
	            // Guardar en caché
	            cacheLineasPorRubro.put(idRubro, lineasDelRubro);
	            System.out.println("📊 Líneas cargadas: " + lineasDelRubro.size());
	        }

	        if (currentLoadId != loadId) {
	            System.out.println("⚠️ Carga de líneas cancelada en UI - LoadId inválido");
	            return;
	        }

	        linea.setItems(lineasDelRubro);
	        linea.setEnabled(true);
	        linea.setPlaceholder("Seleccione una línea (" + lineasDelRubro.size() + " disponibles)");
	        
	        linea.setValue(lineaObjetivo);
	        
	        System.out.println("✅ Líneas establecidas para artículo (LoadId: " + loadId + ")");

	    } catch (Exception e) {
	        System.err.println("❌ Error al cargar líneas: " + e.getMessage());
	        if (currentLoadId != loadId) return;
	        linea.setItems(new ArrayList<>());
	        linea.setPlaceholder("Error al cargar líneas");
	    }
	}

	@Override
	public void clearForm() {
		// Cancelar cualquier carga en progreso
		currentLoadId = System.currentTimeMillis();

		super.clearForm();

		UI.getCurrent().access(() -> {
			// Limpiar todos los combos y campos
			rubro.clear();
			linea.clear();
			medida.clear();
			presentacion.clear();
			proveedor.clear();
			alicuota.clear();
			gravamenDisplay.clear();

			// Resetear estado de bonificación
			esBonificado.setValue(false);
			bonificacion.clear();
			bonificacion.setReadOnly(true);

			// Resetear estado de combos dependientes
			linea.setItems(new ArrayList<>());
			linea.setEnabled(false);
			linea.setPlaceholder("Primero seleccione un rubro");

			// Resetear tooltip del gravamen
			gravamenDisplay.setTooltipText("Seleccione una alícuota para ver el gravamen");

			unidades.setItems(new ArrayList<>());
			unidades.setVisible(false);
		});

		stockMinimo.setValue(0);
		stockMaximo.setValue(0);

		binder.setBean(current);
	}

	@Override
	protected void updateCurrentFromBinder() {

		System.out.println("=== ArticuloForm.updateCurrentFromBinder() ===");

		if (current != null) {
			current.setUnidades(new ArrayList<>(unidades.getValue()));
		}

	}
	
	private static Span crearEtiquetaGrupo(String texto) {
		Span etiqueta = new Span(texto);
		etiqueta.getElement().getThemeList().add("badge contrast");
		return etiqueta;
	}

	private void actualizarPreciosCalculados() {
		BigDecimal costo = precioCosto.getValue();
		BigDecimal margen = margenUtilidad.getValue();
		Alicuota alicuotaSel = alicuota.getValue();
		if (costo == null) costo = BigDecimal.ZERO;
		if (margen == null) margen = BigDecimal.ZERO;
		BigDecimal gravamen = BigDecimal.ZERO;
		if (alicuotaSel != null && alicuotaSel.getGravamen() != null) {
			gravamen = alicuotaSel.getGravamen();
		}
		BigDecimal factorIva = BigDecimal.ONE.add(gravamen.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
		BigDecimal conIva = costo.multiply(factorIva).setScale(2, RoundingMode.HALF_UP);
		BigDecimal venta = conIva.multiply(BigDecimal.ONE.add(margen.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)))
				.setScale(2, RoundingMode.HALF_UP);
		precioConIvaDisplay.setValue(conIva);
		precioVentaDisplay.setValue(venta);
	}

	private void configurarBonificacion() {
		// Configurar estado inicial
        bonificacion.setReadOnly(true);
        bonificacion.setClearButtonVisible(true);

        // Listener para el checkbox esBonificado
        esBonificado.addValueChangeListener(event -> {
            if (isUpdating) return;
            
            boolean estaBonificado = Boolean.TRUE.equals(event.getValue());

            if (estaBonificado) {
                // Habilitar el campo bonificación
                bonificacion.setReadOnly(false);
            } else {
                // Deshabilitar el campo
                bonificacion.setReadOnly(true);
            }
        });
        
        // Listener para el campo bonificación
        bonificacion.addValueChangeListener(event -> {
            if (isUpdating) return;
            
            BigDecimal valor = event.getValue();

            if (valor == null) {
                // Si el usuario borra el valor, desmarcar el checkbox
                esBonificado.setValue(false);
                return;
            }

            // Redondear a 2 decimales
            BigDecimal redondeado = valor.setScale(2, RoundingMode.HALF_UP);
            if (!redondeado.equals(valor)) {
                bonificacion.setValue(redondeado);
                return;
            }

            // Validar rango 0-100
            if (redondeado.compareTo(BigDecimal.ZERO) < 0) {
                bonificacion.setValue(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                esBonificado.setValue(false);
            } else if (redondeado.compareTo(new BigDecimal("100")) > 0) {
                bonificacion.setValue(new BigDecimal("100").setScale(2, RoundingMode.HALF_UP));
            }
        });
    }
	
	private void configurarFormatoDecimalesConListeners() {
		// Configurar listener para precioCosto
		precioCosto.addValueChangeListener(event -> {
			if (event.getValue() != null) {
				BigDecimal valorRedondeado = event.getValue().setScale(2, RoundingMode.HALF_UP);
				if (!valorRedondeado.equals(event.getValue())) {
					precioCosto.setValue(valorRedondeado);
				}
			}
		});

		// Configurar listener para margenUtilidad
		margenUtilidad.addValueChangeListener(event -> {
			if (event.getValue() != null) {
				BigDecimal valorRedondeado = event.getValue().setScale(2, RoundingMode.HALF_UP);
				if (!valorRedondeado.equals(event.getValue())) {
					margenUtilidad.setValue(valorRedondeado);
				}
			}
		});

		// Configurar listener para bonificacion
		bonificacion.addValueChangeListener(event -> {
			if (event.getValue() != null) {
				BigDecimal valorRedondeado = event.getValue().setScale(2, RoundingMode.HALF_UP);
				if (!valorRedondeado.equals(event.getValue())) {
					bonificacion.setValue(valorRedondeado);
				}
			}
		});

		// También forzar formato cuando se pierde el foco
		precioCosto.addBlurListener(_ -> {
			if (precioCosto.getValue() != null) {
				precioCosto.setValue(precioCosto.getValue().setScale(2, RoundingMode.HALF_UP));
			}
		});

		margenUtilidad.addBlurListener(_ -> {
			if (margenUtilidad.getValue() != null) {
				margenUtilidad.setValue(margenUtilidad.getValue().setScale(2, RoundingMode.HALF_UP));
			}
		});

		bonificacion.addBlurListener(_ -> {
			if (bonificacion.getValue() != null) {
				bonificacion.setValue(bonificacion.getValue().setScale(2, RoundingMode.HALF_UP));
			}
		});
	}


}
