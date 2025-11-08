package com.visus.central.infraestructure.persistence.entity;

import com.visus.central.infraestructure.converter.ColumnaComprobanteConverter;
import com.visus.central.infraestructure.converter.NombreCortoComprobanteConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "comprobantes")
public class JpaComprobanteEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcomprobante")
    private Integer id;

    @Column(name = "nombre_largo", nullable = false, length = 60)
    private String nombreLargo;

    @Convert(converter = NombreCortoComprobanteConverter.class)
    @Column(name = "nombre_corto", nullable = false, length = 3)
    private NombreCorto nombreCorto;
    
    public enum NombreCorto {
    	FacturaA("FAA"),
    	FacturaB("FAB"),
    	FacturaC("FAC"),
    	Vale("VAL"),
    	Remito("REM"),
    	Nota_de_Débito("NDB"),
    	Nota_de_Crédito("NCR"),
    	Recibo("REC");

    	private final String label;

    	NombreCorto(String label) {
    		this.label = label;
    	}

    	public String getLabel() {
    		return label;
    	}

    	@Override
    	public String toString() {
    		return label;
    	}

    	public static NombreCorto fromLabel(String label) {
    		for (NombreCorto p: values()) {
    			if (p.label.equalsIgnoreCase(label)) {
    				return p;
    			}
    		}
    		throw new IllegalArgumentException("Nombre Corto inválido: " + label);
    	}
    }

    @Column(name = "numero_inicial", nullable = false)
    private Integer numeroInicial = 1;

    @Column(name = "sucursal", nullable = false)
    private Integer sucursal;
    
    @Convert(converter = ColumnaComprobanteConverter.class)
    @Column(name = "columna", nullable = false)
    private Columna columna;
    
    public enum Columna {
    	Débito("DB"),
    	Crédito("CR");

    	private final String label;

    	Columna(String label) {
    		this.label = label;
    	}

    	public String getLabel() {
    		return label;
    	}

    	@Override
    	public String toString() {
    		return label;
    	}

    	public static Columna fromLabel(String label) {
    		for (Columna p: values()) {
    			if (p.label.equalsIgnoreCase(label)) {
    				return p;
    			}
    		}
    		throw new IllegalArgumentException("Columna inválida: " + label);
    	}
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombreLargo() {
		return nombreLargo;
	}

	public void setNombreLargo(String nombreLargo) {
		this.nombreLargo = nombreLargo;
	}

	public NombreCorto getNombreCorto() {
		return nombreCorto;
	}

	public void setNombreCorto(NombreCorto nombreCorto) {
		this.nombreCorto = nombreCorto;
	}

	public Integer getNumeroInicial() {
		return numeroInicial;
	}

	public void setNumeroInicial(Integer numeroInicial) {
		this.numeroInicial = numeroInicial;
	}

	public Integer getSucursal() {
		return sucursal;
	}

	public void setSucursal(Integer sucursal) {
		this.sucursal = sucursal;
	}

	public Columna getColumna() {
		return columna;
	}

	public void setColumna(Columna columna) {
		this.columna = columna;
	}

}
