package com.visus.central.infraestructure.persistence.entity;

import com.visus.central.infraestructure.converter.EstadoGrupoFamConverter;
import com.visus.central.infraestructure.converter.ParentescoConverter;
import com.visus.central.infraestructure.converter.TDocumentoGrupoFamConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "grupofam")
public class JpaGrupoFamEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idgrupofam")
    private Integer idgrupofam;

	@Column(name = "nombre", nullable = false, length = 100)
	private String nombre;
	
	@ManyToOne
    @JoinColumn(
        name = "idcliente",
        nullable = true
    )
    private JpaClienteEntity cliente;
	
	@Convert(converter = TDocumentoGrupoFamConverter.class) // Esto le dice a JPA: “Usá el conversor para guardar y leer como VARCHAR”.
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

    @Convert(converter = ParentescoConverter.class) // Esto le dice a JPA: “Usá el conversor para guardar y leer como VARCHAR”.
    @Column(name = "parentesco", nullable = false, length = 20)
    private Parentesco parentesco;
    
    public enum Parentesco {
    	Hijo("Hijo/a"), Esposo("Esposo/a"), Nieto("Nieto/a"), Sobrino("Sobrino/a"), Otros("Otros");
    	
    	private final String label;
    	
    	Parentesco(String label) {
    		this.label = label;
    	}
    	
    	@Override
    	public String toString() {
    		return label;
    	}
    	
    	public String getLabel() {
    		return label;
    	}
    	
    	public static Parentesco fromLabel(String label) {
    		for (Parentesco p : values()) {
    			if (p.label.equalsIgnoreCase(label)) {
    				return p;
    			}
    		}
    		throw new IllegalArgumentException("Parentesco inválido: " + label);
    	}
    }
    
    @Convert(converter = EstadoGrupoFamConverter.class) // Esto le dice a JPA: “Usá el conversor para guardar y leer como VARCHAR”.
    @Column(name = "estado", nullable = false, length = 15)
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

	public Integer getIdgrupofam() {
		return idgrupofam;
	}

	public void setIdgrupofam(Integer idgrupofam) {
		this.idgrupofam = idgrupofam;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public JpaClienteEntity getCliente() {
		return cliente;
	}

	public void setCliente(JpaClienteEntity cliente) {
		this.cliente = cliente;
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

	public Parentesco getParentesco() {
		return parentesco;
	}

	public void setParentesco(Parentesco parentesco) {
		this.parentesco = parentesco;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
    

}
