package com.visus.central.infraestructure.persistence.entity;

import java.math.BigDecimal;

import com.visus.central.infraestructure.converter.ModalidadPagoConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "formasdepago")
public class JpaFormaDePagoEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idformaspago")
    private Integer id;

    @Convert(converter = ModalidadPagoConverter.class)
    @Column(name = "modalidad", nullable = false)
    private ModalidadPago modalidad;
    
    public enum ModalidadPago {
    	Efectivo("Efectivo"),
    	Contado("Contado"),
    	Cuenta_Corriente("Cuenta Corriente"),
    	Tarjeta_Débito("Tarjeta de Débito"),
    	Tarjeta_Crédito("Tarjeta de Crédito"),
    	Transferencia("Transferencia"),
    	Cheque("Cheque");

    	private final String label;

    	ModalidadPago(String label) {
    		this.label = label;
    	}

    	public String getLabel() {
    		return label;
    	}

    	@Override
    	public String toString() {
    		return label;
    	}

    	public static ModalidadPago fromLabel(String label) {
    		for (ModalidadPago p : values()) {
    			if (p.label.equalsIgnoreCase(label)) {
    				return p;
    			}
    		}
    		throw new IllegalArgumentException("Modalidad de pago inválida: " + label);
    	}
    }

    @ManyToOne
    @JoinColumn(name = "idcoeficiente", nullable = false)
    private JpaCoeficienteEntity coeficiente;

    @Column(name = "es_dto_pronto_pago")
    private Boolean esDtoProntoPago;

    @Column(name = "dto_pronto_pago", precision = 15, scale = 2)
    private BigDecimal dtoProntoPago;

    @Column(name = "es_meses_completos")
    private Boolean esMesesCompletos;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ModalidadPago getModalidad() {
		return modalidad;
	}

	public void setModalidad(ModalidadPago modalidad) {
		this.modalidad = modalidad;
	}

	public JpaCoeficienteEntity getCoeficiente() {
		return coeficiente;
	}

	public void setCoeficiente(JpaCoeficienteEntity coeficiente) {
		this.coeficiente = coeficiente;
	}

	public Boolean getEsDtoProntoPago() {
		return esDtoProntoPago;
	}

	public void setEsDtoProntoPago(Boolean esDtoProntoPago) {
		this.esDtoProntoPago = esDtoProntoPago;
	}

	public BigDecimal getDtoProntoPago() {
		return dtoProntoPago;
	}

	public void setDtoProntoPago(BigDecimal dtoProntoPago) {
		this.dtoProntoPago = dtoProntoPago;
	}

	public Boolean getEsMesesCompletos() {
		return esMesesCompletos;
	}

	public void setEsMesesCompletos(Boolean esMesesCompletos) {
		this.esMesesCompletos = esMesesCompletos;
	}

}
