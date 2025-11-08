package com.visus.central.infraestructure.persistence.entity;

import java.util.Objects;

import com.visus.central.infraestructure.converter.TipoDomicilioConverter;
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
@Table(name = "domicilios")
public class JpaDomicilioEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddomicilio")
    private Integer id;

	@Convert(converter = TipoDomicilioConverter.class) // Esto le dice a JPA: “Usá el conversor para guardar y leer como VARCHAR”.
    @Column(name = "tipodomicilio", nullable = false, length = 30)
	private TipoDomicilio tipoDomicilio;

	public enum TipoDomicilio {
		Fiscal_Pcial_Jurisdicción_Sede("Fiscal Pcial Jurisdicción Sede"), Principal_de_Actividades("Principal de Actividades"), 
		Fiscal_Jurisdiccional("Fiscal Jurisdiccional"), Sin_Declarar("Sin Declarar");
		
		private final String label;

		TipoDomicilio(String label) {
			this.label = label;
		}
		
		@Override
	    public String toString() {
	        return label;
	    }

		public String getLabel() {
			return label;
		}

		public static TipoDomicilio fromLabel(String label) {
			for (TipoDomicilio p : values()) {
				if (p.label.equalsIgnoreCase(label)) {
					return p;
				}
			}
			throw new IllegalArgumentException("Tipo de Domicilio inválido: " + label);
		}
	}

    @Column(name = "calle", nullable = false, length = 60)
    private String calle;

    @Column(name = "numero", nullable = false)
    private Short numero;

    @Column(name = "barrio", length = 60)
    private String barrio;

    @Column(name = "manzana", length = 15)
    private String manzana;

    @Column(name = "casa", length = 10)
    private String casa;

    @Column(name = "sector", length = 10)
    private String sector;

    @Column(name = "depto", length = 10)
    private String depto;

    @Column(name = "oficina", length = 10)
    private String oficina;

    @Column(name = "lote", length = 10)
    private String lote;

    @Column(name = "idlocalidad", nullable = false)
    private Integer idLocalidad;

    @ManyToOne
    @JoinColumn(
        name = "idcliente",
        nullable = true
    )
    private JpaClienteEntity cliente;

    @ManyToOne
    @JoinColumn(
        name = "idvendedor",
        nullable = true
    )
    private JpaVendedorEntity vendedor;

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TipoDomicilio getTipoDomicilio() {
		return tipoDomicilio;
	}

	public void setTipoDomicilio(TipoDomicilio tipoDomicilio) {
		this.tipoDomicilio = tipoDomicilio;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public Short getNumero() {
		return numero;
	}

	public void setNumero(Short numero) {
		this.numero = numero;
	}

	public String getBarrio() {
		return barrio;
	}

	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}

	public String getManzana() {
		return manzana;
	}

	public void setManzana(String manzana) {
		this.manzana = manzana;
	}

	public String getCasa() {
		return casa;
	}

	public void setCasa(String casa) {
		this.casa = casa;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getDepto() {
		return depto;
	}

	public void setDepto(String depto) {
		this.depto = depto;
	}

	public String getOficina() {
		return oficina;
	}

	public void setOficina(String oficina) {
		this.oficina = oficina;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public Integer getIdLocalidad() {
		return idLocalidad;
	}

	public void setIdLocalidad(Integer idLocalidad) {
		this.idLocalidad = idLocalidad;
	}

	public JpaClienteEntity getCliente() {
		return cliente;
	}

	public void setCliente(JpaClienteEntity cliente) {
		this.cliente = cliente;
	}

	public JpaVendedorEntity getVendedor() {
		return vendedor;
	}

	public void setVendedor(JpaVendedorEntity vendedor) {
		this.vendedor = vendedor;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JpaDomicilioEntity)) return false;
        JpaDomicilioEntity that = (JpaDomicilioEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
