package com.visus.central.domain.port.out;

import java.math.BigDecimal;
import java.util.Date;
import com.visus.central.domain.model.Venta;
import com.visus.central.domain.port.out.dto.ReporteCajaDiaria;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public interface ReportGenerator {
	byte[] generarReporteRemito(Venta venta);

	byte[] generarResumenCuentaCorriente(Date fechaEmision, Date fechaVencimiento, BigDecimal montoOriginal,
			BigDecimal saldoPendiente, String estadoCuenta, BigDecimal coeficiente, String nombreCliente,
			String documentoCliente, String domicilioCliente, String telefonoCliente, BigDecimal totalPagado,
			Date fechaReporte, JRBeanCollectionDataSource vencimientosDataSource,
			JRBeanCollectionDataSource aplicacionesDataSource);
	
	byte[] generarReporteCajaDiaria(ReporteCajaDiaria reporte);
}
