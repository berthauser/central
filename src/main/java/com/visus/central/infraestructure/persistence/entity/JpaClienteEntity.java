package com.visus.central.infraestructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.visus.central.infraestructure.converter.EstadoClienteConverter;
import com.visus.central.infraestructure.converter.SFiscalClienteConverter;
import com.visus.central.infraestructure.converter.SexoClienteConverter;
import com.visus.central.infraestructure.converter.TDocumentoClienteConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class JpaClienteEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcliente")
    private Integer id;

    @Column(name = "nombre_fantasia", nullable = false, length = 50)
    private String nombreFantasia;

    @Column(name = "nombre_cliente", nullable = false, length = 50)
    private String nombreCliente;
    
    @Convert(converter = SexoClienteConverter.class) // Esto le dice a JPA: “Usá el conversor para guardar y leer como VARCHAR”.
    @Column(name = "sexo", nullable = false, length = 15)
    private Sexo sexo;
    
    public enum Sexo {
    	Masculino("Masculino"), Femenino("Femenino"), No_Corresponde("No Corresponde");
    	
		private final String label;

		Sexo(String label) {
			this.label = label;
		}
		
		@Override
	    public String toString() {
	        return label;
	    }

		public String getLabel() {
			return label;
		}

		public static Sexo fromLabel(String label) {
			for (Sexo p : values()) {
				if (p.label.equalsIgnoreCase(label)) {
					return p;
				}
			}
			throw new IllegalArgumentException("Sexo no válido: " + label);
		}
	}
    
    @Column(name = "telefono_fijo", length = 15)
    private String telefonoFijo;

    @Column(name = "telefono_movil", length = 15)
    private String telefonoMovil;

    @Convert(converter = TDocumentoClienteConverter.class) // Esto le dice a JPA: “Usá el conversor para guardar y leer como VARCHAR”.
    @Column(name = "documento", nullable = false, length = 20)
    private TipoDocumento tipoDocumento;
    
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
    
    @Column(name = "numero")
    private Long numero;

    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    @Column(name = "fecha_actualizacion")
    private LocalDate fechaActualizacion;

    @Column(name = "fecha_baja")
    private LocalDate fechaBaja;

    @Column(name = "fecha_ultimacompra", nullable = false)
    private LocalDate fechaUltimaCompra;

    @Column(name = "limite_facturasvencidas", nullable = false)
    private Short limiteFacturasVencidas;

    @Column(name = "limite_credito", precision = 18, scale = 2)
    private BigDecimal limiteCredito;

    @Column(name = "pago_minimo", precision = 18, scale = 2)
    private BigDecimal pagoMinimo;

    @Convert(converter = SFiscalClienteConverter.class) // Esto le dice a JPA: “Usá el conversor para guardar y leer como VARCHAR”.
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
    
    @Column(name = "saldo_ctacte", nullable = false, precision = 18, scale = 2)
    private BigDecimal saldoCtaCte;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
    
    @Convert(converter = EstadoClienteConverter.class) // Esto le dice a JPA: “Usá el conversor para guardar y leer como VARCHAR”.
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
			throw new IllegalArgumentException("Estado no válido: " + label);
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

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public String getTelefonoFijo() {
		return telefonoFijo;
	}

	public void setTelefonoFijo(String telefonoFijo) {
		this.telefonoFijo = telefonoFijo;
	}

	public String getTelefonoMovil() {
		return telefonoMovil;
	}

	public void setTelefonoMovil(String telefonoMovil) {
		this.telefonoMovil = telefonoMovil;
	}

	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(LocalDate fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public LocalDate getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setFechaActualizacion(LocalDate fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	public LocalDate getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(LocalDate fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	public LocalDate getFechaUltimaCompra() {
		return fechaUltimaCompra;
	}

	public void setFechaUltimaCompra(LocalDate fechaUltimaCompra) {
		this.fechaUltimaCompra = fechaUltimaCompra;
	}

	public Short getLimiteFacturasVencidas() {
		return limiteFacturasVencidas;
	}

	public void setLimiteFacturasVencidas(Short limiteFacturasVencidas) {
		this.limiteFacturasVencidas = limiteFacturasVencidas;
	}

	public BigDecimal getLimiteCredito() {
		return limiteCredito;
	}

	public void setLimiteCredito(BigDecimal limiteCredito) {
		this.limiteCredito = limiteCredito;
	}

	public BigDecimal getPagoMinimo() {
		return pagoMinimo;
	}

	public void setPagoMinimo(BigDecimal pagoMinimo) {
		this.pagoMinimo = pagoMinimo;
	}

	public SituacionFiscal getSituacionFiscal() {
		return situacionFiscal;
	}

	public void setSituacionFiscal(SituacionFiscal situacionFiscal) {
		this.situacionFiscal = situacionFiscal;
	}

	public BigDecimal getSaldoCtaCte() {
		return saldoCtaCte;
	}

	public void setSaldoCtaCte(BigDecimal saldoCtaCte) {
		this.saldoCtaCte = saldoCtaCte;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
    
}
