package com.visus.central.ui.component;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class AboutDialog extends Dialog {
	
	private static final long serialVersionUID = 1L;

	public AboutDialog() {
        // Configuración básica del diálogo
        setModal(true);
        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
        
        // Deshabilitar los botones de sistema (minimizar, maximizar, cerrar)
        setDraggable(false);
        setResizable(false);
        
        // Estilos del diálogo para ocultar botones de sistema
        getElement().getStyle()
            .set("background-color", "#101b29")
            .set("color", "white")
            .set("border", "1px solid #3a5f5f")
            .set("border-radius", "8px")
            .set("box-shadow", "0 4px 8px rgba(0, 0, 0, 0.3)")
            .set("resize", "none") // Deshabilitar redimensionamiento
            .set("-webkit-app-region", "no-drag"); // Para evitar arrastre en algunos sistemas
        
     // Eliminar cualquier botón de cerrar por defecto
        getElement().executeJs("""
            this.$.overlay.style.setProperty('--vaadin-dialog-close-button-display', 'none');
            // Eliminar botones de sistema si existen
            const overlay = this.$.overlay;
            if (overlay.shadowRoot) {
                const closeBtn = overlay.shadowRoot.querySelector('[part="close-button"]');
                if (closeBtn) closeBtn.style.display = 'none';
                
                // También intentamos eliminar cualquier botón de sistema del shadow DOM
                const systemButtons = overlay.shadowRoot.querySelectorAll('button');
                systemButtons.forEach(btn => {
                    if (btn.textContent.includes('✕') || btn.textContent.includes('×') || 
                        btn.getAttribute('part') === 'close-button') {
                        btn.style.display = 'none';
                    }
                });
            }
        """);
        setWidth("500px");
        setMaxWidth("90%");
        
        // Contenido del diálogo
        createContent();
        
        // Configurar teclas de atajo
        setupShortcuts();
    }
	
	private void createContent() {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setPadding(true);
        mainLayout.setSpacing(true);
        mainLayout.setMargin(false);
        mainLayout.setWidthFull();
        mainLayout.getStyle()
            .set("position", "relative");
        
        // Encabezado
        HorizontalLayout header = createHeader();
        
        // Contenido principal
        HorizontalLayout content = createContentArea();
        
        // Footer
        Div footer = createFooter();
        
        mainLayout.add(header, content, footer);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        
        add(mainLayout);
    }
	
	private HorizontalLayout createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        
        Icon infoIcon = VaadinIcon.INFO_CIRCLE.create();
        infoIcon.setSize("24px");
        infoIcon.setColor("#3a5f5f");
        
        H2 title = new H2("Acerca de...");
        title.getStyle()
            .set("color", "white")
            .set("margin", "0")
            .set("padding", "0")
            .set("font-size", "1.5rem");
        
        header.add(infoIcon, title);
        header.setSpacing(true);
        
        return header;
    }
	
	private HorizontalLayout createContentArea() {
        HorizontalLayout content = new HorizontalLayout();
        content.setWidthFull();
        content.setSpacing(true);
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        
        // Imagen/icono a la izquierda
        Div iconContainer = new Div();
        iconContainer.getStyle()
            .set("display", "flex")
            .set("align-items", "center")
            .set("justify-content", "center")
            .set("width", "120px")
            .set("height", "120px")
            .set("background", "linear-gradient(135deg, #101b29, #2a3f4f)")
            .set("border-radius", "12px")
            .set("border", "2px solid #3a5f5f")
            .set("flex-shrink", "0");
        
        Icon appIcon = VaadinIcon.CUBES.create();
        appIcon.setSize("64px");
        appIcon.setColor("#3a5f5f");
        iconContainer.add(appIcon);
        
     // Usa una imagen:
     // Image logoImage = new Image("/images/visus-logo.png", "Logo Visus Central");
     // logoImage.getStyle()
//          .set("width", "100px")
//          .set("height", "100px")
//          .set("object-fit", "contain");
     // iconContainer.add(logoImage);
        
        
     // Información a la derecha
        VerticalLayout infoLayout = new VerticalLayout();
        infoLayout.setSpacing(true);
        infoLayout.setPadding(false);
        infoLayout.setMargin(false);
        infoLayout.setWidthFull();
        
        // Nombre del sistema
        Div systemName = new Div();
        systemName.getStyle()
            .set("font-size", "2rem")
            .set("font-weight", "bold")
            .set("color", "white")
            .set("margin-bottom", "8px");
        
        Span visus = new Span("Visus");
        visus.getStyle().set("color", "#4CAF50");
        
        Span central = new Span(" Central");
        central.getStyle().set("color", "white");
        
        systemName.add(visus, central);
        
     // Versión
        Div version = new Div();
        version.getStyle()
            .set("font-size", "1rem")
            .set("color", "#a0a0a0")
            .set("margin-bottom", "12px");
        version.setText("Versión 1.0.0");
        
        // Descripción
        Div description = new Div();
        description.getStyle()
            .set("font-size", "0.9rem")
            .set("color", "#cccccc")
            .set("line-height", "1.5");
        
        Paragraph p1 = new Paragraph("Sistema de Gestión Integral para empresas.");
        Paragraph p2 = new Paragraph("Módulos: Clientes, Proveedores, Inventario, Precios y más.");
        
        description.add(p1, p2);
        
        infoLayout.add(systemName, version, description);
        
        content.add(iconContainer, infoLayout);
        
        return content;
    }
	
	private Div createFooter() {
        Div footer = new Div();
        footer.getStyle()
            .set("width", "100%")
            .set("text-align", "center")
            .set("padding", "16px 0 0 0")
            .set("border-top", "1px solid #2a3f4f")
            .set("margin-top", "16px");
        
        Div copyright = new Div();
        copyright.getStyle()
            .set("font-size", "0.9rem")
            .set("color", "#a0a0a0")
            .set("font-style", "italic");
        copyright.setText("Software colaborativo del Grupo Dignitas");
        
        // Año actual
        Span year = new Span(" © 2026");
        year.getStyle()
            .set("color", "#3a5f5f")
            .set("font-weight", "bold");
        
        copyright.add(year);
        footer.add(copyright);
        
        return footer;
    }
	
	private void setupShortcuts() {
        // Tecla ESCAPE para cerrar
        UI.getCurrent().addShortcutListener(
            () -> close(),
            Key.ESCAPE
        );
        
        // También agregamos un overlay click para cerrar
        getElement().addEventListener("click", e -> {
            // Verificar si el clic fue fuera del contenido del diálogo
            if (e.getEventTarget().equals(getElement())) {
                close();
            }
        });
    }
	
	public void openDialog() {
        open();
        // Enfocar el diálogo para que ESC funcione inmediatamente
        getElement().executeJs("this.focus()");
        
        // Forzar a que no se muestren botones de sistema después de abrir
        getElement().executeJs("""
            setTimeout(() => {
                const overlay = this.$.overlay;
                if (overlay && overlay.shadowRoot) {
                    // Ocultar cualquier botón de cerrar
                    const closeButtons = overlay.shadowRoot.querySelectorAll('[part="close-button"], .close-button, [title*="Close"], [aria-label*="close"]');
                    closeButtons.forEach(btn => {
                        btn.style.display = 'none';
                        btn.style.visibility = 'hidden';
                        btn.setAttribute('hidden', 'true');
                    });
                    
                   // Ocultar botones de minimizar y maximizar si existen
                    const systemButtons = overlay.shadowRoot.querySelectorAll('button');
                    systemButtons.forEach(btn => {
                        const btnText = btn.textContent || '';
                        const btnTitle = btn.getAttribute('title') || '';
                        if (btnText.includes('−') || btnText.includes('_') || btnText.includes('🗕') || 
                            btnText.includes('□') || btnText.includes('🗖') || 
                            btnTitle.includes('Minimize') || btnTitle.includes('Maximize')) {
                            btn.style.display = 'none';
                            btn.style.visibility = 'hidden';
                        }
                    });
                }
            }, 50);
        """);
    }
	
	@Override
    public void open() {
        super.open();
        // Asegurarnos de que el diálogo esté en primer plano
        getElement().executeJs("""
            this.style.zIndex = '1000';
            this.$.overlay.style.zIndex = '1000';
        """);
    }

}
