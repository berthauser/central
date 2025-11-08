package com.visus.central.ui.component;

import java.util.Arrays;
import java.util.List;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.visus.central.domain.model.Banco;
import com.visus.central.domain.model.Proveedor;
import com.visus.central.domain.port.in.BancoUseCase;
import com.visus.central.infraestructure.persistence.entity.JpaProveedorEntity.Estado;
import com.visus.central.infraestructure.persistence.entity.JpaProveedorEntity.SituacionFiscal;
import com.visus.central.infraestructure.persistence.entity.JpaProveedorEntity.TipoDocumento;
import com.visus.central.infraestructure.util.SpringContextHelper;

public class ProveedorForm extends AbstractForm<Proveedor> {

	private static final long serialVersionUID = 1L;

	private TextField nombreFantasia = new TextField("Nombre Fantasía");
	private TextField nombreReal = new TextField("Nombre Real");
	private TextField telefonoUno = new TextField("Teléfono 1");
	private TextField telefonoDos = new TextField("Teléfono 2");
	private TextField telefonoTres = new TextField("Teléfono 3");
	private ComboBox<SituacionFiscal> situacionFiscal = new ComboBox<>("Situación Fiscal");
	private ComboBox<Banco> banco = new ComboBox<>("Banco");
	private EmailField email = new EmailField("Email");
	private ComboBox<TipoDocumento> documento = new ComboBox<>("Documento");
	private TextField numero = new TextField("Número");
	private ComboBox<Estado> estado = new ComboBox<>("Estado");

	public ProveedorForm() {
		super(Proveedor.class);
		cargarBancos();
		buildLayout();
	}

	private void cargarBancos() {
		try {
            // Obtener el servicio de bancos desde Spring
            BancoUseCase bancoService = SpringContextHelper.getBean(BancoUseCase.class);
            List<Banco> bancos = bancoService.findAll();
            
//            System.out.println("Bancos cargados: " + (bancos != null ? bancos.size() : "NULL"));
            
            if (bancos != null) {
                banco.setItems(bancos);
            } else {
                banco.setItems(List.of());
            }
            
            banco.setItemLabelGenerator(b -> {
                if (b != null) {
                    return b.getNombre() + " (" + 
                           (b.getIdBcoCen() != null ? b.getIdBcoCen() : "N/A") + ")";
                }
                return "";
            });
            
        } catch (Exception e) {
            System.err.println("❌ Error al cargar bancos: " + e.getMessage());
            banco.setItems(List.of());
        }
	}

	private void buildLayout() {
		String campoStyle = "campo-estilo-imagen";

		// Configuración de campos...
		nombreFantasia.addClassName(campoStyle);
		nombreFantasia.setRequired(true);
		nombreFantasia.setWidth("400px");
		nombreFantasia.setMaxLength(50);

		nombreReal.addClassName(campoStyle);
		nombreReal.setRequired(true);
		nombreReal.setWidth("400px");
		nombreReal.setMaxLength(50);

		numero.addClassName(campoStyle);
		numero.setWidth("200px");
		numero.setPlaceholder("Número de documento");

		telefonoUno.addClassName(campoStyle);
		telefonoUno.setWidth("150px");
		telefonoUno.setMaxLength(15);

		telefonoDos.addClassName(campoStyle);
		telefonoDos.setWidth("150px");
		telefonoDos.setMaxLength(15);

		telefonoTres.addClassName(campoStyle);
		telefonoTres.setWidth("150px");
		telefonoTres.setMaxLength(15);

		situacionFiscal.addClassName(campoStyle);
		situacionFiscal.setItems(Arrays.asList(SituacionFiscal.values()));
		situacionFiscal.setItemLabelGenerator(SituacionFiscal::getLabel);
		situacionFiscal.setWidth("300px");
		situacionFiscal.setRequired(true);

		banco.addClassName(campoStyle); // CONFIGURACIÓN DEL COMBOBOX DE BANCOS
		banco.setWidth("300px");
		banco.setPlaceholder("Seleccione un banco");

		email.addClassName(campoStyle);
		email.setWidth("300px");
		email.setMaxLength(100);

		documento.addClassName(campoStyle);
		documento.setItems(Arrays.asList(TipoDocumento.values()));
		documento.setItemLabelGenerator(TipoDocumento::getLabel);
		documento.setWidth("200px");
		documento.setRequired(true);

		estado.addClassName(campoStyle);
		estado.setItems(Arrays.asList(Estado.values()));
		estado.setItemLabelGenerator(Estado::getLabel);
		estado.setWidth("200px");
		
		// ALINEACIÓN HORIZONTAL: Usar HorizontalLayout para organizar en filas
        HorizontalLayout fila1 = new HorizontalLayout(nombreFantasia, nombreReal, situacionFiscal);
        fila1.setSpacing(true);
        fila1.setAlignItems(Alignment.END); // Alinear al bottom para que se vea mejor
        
        HorizontalLayout fila2 = new HorizontalLayout(documento, numero, telefonoUno, telefonoDos, telefonoTres, banco);
        fila2.setSpacing(true);
        fila2.setAlignItems(Alignment.END); // Alinear al bottom para que se vea mejor
		
        HorizontalLayout fila3 = new HorizontalLayout(email, estado);
        fila3.setSpacing(true);
        fila3.setAlignItems(Alignment.END); // Alinear al bottom para que se vea mejor

		add(fila1, fila2, fila3, buildBotonera());
		bindFields();
	}

	@Override
	protected void bindFields() {
		binder.forField(nombreFantasia)
		.asRequired("El nombre fantasía es obligatorio")
		.bind(Proveedor::getNombreFantasia, Proveedor::setNombreFantasia);

		binder.forField(nombreReal)
		.asRequired("El nombre real es obligatorio")
		.bind(Proveedor::getNombreReal, Proveedor::setNombreReal);

		binder.forField(numero)
		.withNullRepresentation("")
		.withConverter(new StringToLongConverter("Debe ser un número válido"))
		.bind(Proveedor::getNumero, Proveedor::setNumero);

		binder.bind(telefonoUno, Proveedor::getTelefonoUno, Proveedor::setTelefonoUno);
		binder.bind(telefonoDos, Proveedor::getTelefonoDos, Proveedor::setTelefonoDos);
		binder.bind(telefonoTres, Proveedor::getTelefonoTres, Proveedor::setTelefonoTres);

		binder.forField(situacionFiscal)
		.asRequired("La Situación Fiscal es obligatoria")
		.bind(Proveedor::getSituacionFiscal, Proveedor::setSituacionFiscal);

		// BINDING DEL COMBOBOX DE BANCOS
		binder.forField(banco)
		.bind(Proveedor::getBanco, Proveedor::setBanco);

		binder.bind(email, Proveedor::getEmail, Proveedor::setEmail);

		binder.forField(documento)
		.asRequired("El tipo de documento es obligatorio")
		.bind(Proveedor::getDocumento, Proveedor::setDocumento);

		binder.bind(estado, Proveedor::getEstado, Proveedor::setEstado);
	}

	@Override
	protected void setFormValues(Proveedor entity) {
		if (entity == null) {
			nombreFantasia.clear();
			nombreReal.clear();
			numero.clear();
			telefonoUno.clear();
			telefonoDos.clear();
			telefonoTres.clear();
			situacionFiscal.clear();
			banco.clear(); // 👈 LIMPIAR COMBOBOX DE BANCOS
			email.clear();
			documento.clear();
			estado.clear();
			return;
		}
		nombreFantasia.setValue(entity.getNombreFantasia() != null ? entity.getNombreFantasia() : "");
		nombreReal.setValue(entity.getNombreReal() != null ? entity.getNombreReal() : "");
		numero.setValue(entity.getNumero() != null ? entity.getNumero().toString() : "");
		telefonoUno.setValue(entity.getTelefonoUno() != null ? entity.getTelefonoUno() : "");
		telefonoDos.setValue(entity.getTelefonoDos() != null ? entity.getTelefonoDos() : "");
		telefonoTres.setValue(entity.getTelefonoTres() != null ? entity.getTelefonoTres() : "");
		situacionFiscal.setValue(entity.getSituacionFiscal());
		banco.setValue(entity.getBanco()); // 👈 ESTABLECER VALOR DEL COMBOBOX
		email.setValue(entity.getEmail() != null ? entity.getEmail() : "");
		documento.setValue(entity.getDocumento());
		estado.setValue(entity.getEstado());
	}

	@Override
	protected void updateCurrentFromBinder() {
		// Todos los campos están bindados
	}
	
}
