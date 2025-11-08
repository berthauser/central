package com.visus.central.infraestructure.persistence.entity;

import com.visus.central.infraestructure.converter.EstadoProveedorConverter;
import com.visus.central.infraestructure.converter.SFiscalProveedorConverter;
import com.visus.central.infraestructure.converter.TDocumentoProveedorConverter;

import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "proveedores")
public class JpaProveedorEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idproveedor")
    private Integer id;

    @Column(name = "nombre_fantasia", nullable = false, length = 50)
    private String nombreFantasia;

    @Column(name = "nombre_real", nullable = false, length = 50)
    private String nombreReal;
    
    @Convert(converter = SFiscalProveedorConverter.class)
    @Column(name = "situacion_fiscal", nullable = false, length = 30)
    private SituacionFiscal situacionFiscal;
    
    public enum SituacionFiscal {
    	IVA_Responsable_No_Inscripto("IVA Responsable No Inscripto"), IVA_Responsable_Inscripto("IVA Responsable Inscripto"), 
    	IVA_Sujeto_Exento("IVA Sujeto Exento"), Consumidor_Final("Consumidor Final"), Responsable_Monotributo("Responsable Monotributo"),
    	Proveedor_del_Exterior("Proveedor del Exterior"), Cliente_del_Exterior("Cliente del Exterior"), IVA_Liberado_Ley_19640("IVA Liberado Ley 19640"),
    	Monotributista_Social("Monotributista Social"), IVA_No_Alcanzado("IVA No Alcanzado");
    	
		private final String label;

		SituacionFiscal(String label) {
			this.label = label;
		}
		
		@Override
	    public String toString() {
	        return label;
	    }

		public String getLabel() {
			return label;
		}

		public static SituacionFiscal fromLabel(String label) {
			for (SituacionFiscal p : values()) {
				if (p.label.equalsIgnoreCase(label)) {
					return p;
				}
			}
			throw new IllegalArgumentException("Situación Fiscal inválida: " + label);
		}
	}
    
    @Convert(converter = TDocumentoProveedorConverter.class)
    @Column(name = "documento", nullable = false, length = 20)
    private TipoDocumento documento;
    
    public enum TipoDocumento {
    	Cuit_Cuil("CUIT/CUIL"), Dni("DNI"), No_Corresponde("No Corresponde");
    	
		private final String label;

		TipoDocumento(String label) {
			this.label = label;
		}
		
		@Override
	    public String toString() {
	        return label;
	    }

		public String getLabel() {
			return label;
		}

		public static TipoDocumento fromLabel(String label) {
			for (TipoDocumento p : values()) {
				if (p.label.equalsIgnoreCase(label)) {
					return p;
				}
			}
			throw new IllegalArgumentException("Tipo de Documento inválido: " + label);
		}
	}

    @Column(name = "telefono_uno", length = 15)
    private String telefonoUno;

    @Column(name = "telefono_dos", length = 15)
    private String telefonoDos;

    @Column(name = "telefono_tres", length = 15)
    private String telefonoTres;
    
    // RELACIÓN CON BANCO
    @ManyToOne(fetch = FetchType.EAGER) // EAGER para evitar LazyInitializationException
    @JoinColumn(name = "idbanco", referencedColumnName = "idbanco", foreignKey = @ForeignKey(name = "fk_idbanco"))
    private JpaBancoEntity banco;
    
    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "numero")
    private Long numero;
    
    @Convert(converter = EstadoProveedorConverter.class)
    @Column(name = "estado", length = 15)
    private Estado estado;
    
    public enum Estado {
    	Habilitado("Habilitado"), Deshabilitado("Deshabilitado"), Baja("Baja");
    	
		private final String label;

		Estado(String label) {
			this.label = label;
		}
		
		@Override
	    public String toString() {
	        return label;
	    }

		public String getLabel() {
			return label;
		}

		public static Estado fromLabel(String label) {
			for (Estado p : values()) {
				if (p.label.equalsIgnoreCase(label)) {
					return p;
				}
			}
			throw new IllegalArgumentException("Estado inválido: " + label);
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombreFantasia() {
		return nombreFantasia;
	}

	public void setNombreFantasia(String nombreFantasia) {
		this.nombreFantasia = nombreFantasia;
	}

	public String getNombreReal() {
		return nombreReal;
	}

	public void setNombreReal(String nombreReal) {
		this.nombreReal = nombreReal;
	}

	public SituacionFiscal getSituacionFiscal() {
		return situacionFiscal;
	}

	public void setSituacionFiscal(SituacionFiscal situacionFiscal) {
		this.situacionFiscal = situacionFiscal;
	}

	public TipoDocumento getDocumento() {
		return documento;
	}

	public void setDocumento(TipoDocumento documento) {
		this.documento = documento;
	}

	public String getTelefonoUno() {
		return telefonoUno;
	}

	public void setTelefonoUno(String telefonoUno) {
		this.telefonoUno = telefonoUno;
	}

	public String getTelefonoDos() {
		return telefonoDos;
	}

	public void setTelefonoDos(String telefonoDos) {
		this.telefonoDos = telefonoDos;
	}

	public String getTelefonoTres() {
		return telefonoTres;
	}

	public void setTelefonoTres(String telefonoTres) {
		this.telefonoTres = telefonoTres;
	}

	public JpaBancoEntity getBanco() {
		return banco;
	}

	public void setBanco(JpaBancoEntity banco) {
		this.banco = banco;
	}

	// MÉTODO AUXILIAR PARA OBTENER ID DEL BANCO
    public Integer getIdBanco() {
        return banco != null ? banco.getId() : null;
    }
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
    
}
