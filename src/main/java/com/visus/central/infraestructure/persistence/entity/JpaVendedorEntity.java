package com.visus.central.infraestructure.persistence.entity;

import java.util.Objects;

import com.visus.central.infraestructure.converter.SFiscalVendedorConverter;
import com.visus.central.infraestructure.converter.TDocumentoVendedorConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vendedores")
public class JpaVendedorEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idvendedor")
    private Integer id;

    @Column(name = "numero")
    private Long numero;

    @Column(name = "nombre", nullable = false, length = 60)
    private String nombre;

    @Column(name = "telefono", length = 15)
    private String telefono;

    @Column(name = "email", length = 100)
    private String email;

    @Convert(converter = SFiscalVendedorConverter.class) // Esto le dice a JPA: “Usá el conversor para guardar y leer como VARCHAR”.
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
    
    @Convert(converter = TDocumentoVendedorConverter.class) // Esto le dice a JPA: “Usá el conversor para guardar y leer como VARCHAR”.
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
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public SituacionFiscal getSituacionFiscal() {
		return situacionFiscal;
	}

	public void setSituacionFiscal(SituacionFiscal situacionFiscal) {
		this.situacionFiscal = situacionFiscal;
	}

	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, id, nombre, numero, situacionFiscal, telefono, tipoDocumento);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JpaVendedorEntity other = (JpaVendedorEntity) obj;
		return Objects.equals(email, other.email) && Objects.equals(id, other.id)
				&& Objects.equals(nombre, other.nombre) && Objects.equals(numero, other.numero)
				&& situacionFiscal == other.situacionFiscal && Objects.equals(telefono, other.telefono)
				&& tipoDocumento == other.tipoDocumento;
	}

	@Override
	public String toString() {
		return "JpaVendedorEntity [id=" + id + ", numero=" + numero + ", nombre=" + nombre + ", telefono=" + telefono
				+ ", email=" + email + ", situacionFiscal=" + situacionFiscal + ", tipoDocumento=" + tipoDocumento
				+ "]";
	}
	
}
