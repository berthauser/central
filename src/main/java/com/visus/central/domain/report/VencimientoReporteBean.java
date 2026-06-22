package com.visus.central.domain.report;

import java.math.BigDecimal;
import java.util.Date;

public class VencimientoReporteBean {
	private Short numeroCuota;
    private Date fechaVencimiento;
    private BigDecimal montoCuota;
    private BigDecimal saldoCuota;
    private String estadoCuota;
    
    // Constructor
    public VencimientoReporteBean(Short numeroCuota, Date fechaVencimiento, 
                                   BigDecimal montoCuota, BigDecimal saldoCuota, 
                                   String estadoCuota) {
        this.numeroCuota = numeroCuota;
        this.fechaVencimiento = fechaVencimiento;
        this.montoCuota = montoCuota;
        this.saldoCuota = saldoCuota;
        this.estadoCuota = estadoCuota;
    }

	public Short getNumeroCuota() {
		return numeroCuota;
	}

	public void setNumeroCuota(Short numeroCuota) {
		this.numeroCuota = numeroCuota;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public BigDecimal getMontoCuota() {
		return montoCuota;
	}

	public void setMontoCuota(BigDecimal montoCuota) {
		this.montoCuota = montoCuota;
	}

	public BigDecimal getSaldoCuota() {
		return saldoCuota;
	}

	public void setSaldoCuota(BigDecimal saldoCuota) {
		this.saldoCuota = saldoCuota;
	}

	public String getEstadoCuota() {
		return estadoCuota;
	}

	public void setEstadoCuota(String estadoCuota) {
		this.estadoCuota = estadoCuota;
	}
    
    

}
