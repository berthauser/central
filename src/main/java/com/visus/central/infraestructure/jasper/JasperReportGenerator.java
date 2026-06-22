package com.visus.central.infraestructure.jasper;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Venta;
import com.visus.central.domain.port.out.ReportGenerator;
import com.visus.central.domain.port.out.dto.MovimientoReporteBean;
import com.visus.central.domain.port.out.dto.ReporteCajaDiaria;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;

@Component
public class JasperReportGenerator implements ReportGenerator {

	@Override
	public byte[] generarReporteRemito(Venta venta) {
		try (InputStream reportStream = new ClassPathResource("reports/rptRemito.jrxml").getInputStream()) {
			// 1. Compilar el archivo JRXML a JasperReport
			JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

			// 2. Parámetros del reporte (definidos en JasperStudio)
			Map<String, Object> params = new HashMap<>();
			params.put("nroComprobante", venta.getNumeroComprobante());
			params.put("fechaEmision", venta.getFechaVenta());
			params.put("dniCliente", String.valueOf(venta.getCliente().getNumero()));
			params.put("razonSocialCliente", venta.getCliente().getNombreCliente());
			params.put("condicionIVACliente", venta.getCliente().getSituacionFiscal().toString());
			params.put("vendedor", venta.getVendedor().getNombre());

			// Lo hago acá para no tocar el modelo
			String condicionVenta = construirCondicionVenta(venta);
//            	    + " - " + venta.getCoeficiente().getCuotas() + " cuotas";

			params.put("condicionVenta", condicionVenta);

			// 3. Fuente de datos: lista de ítems de la venta
			JRBeanCollectionDataSource itemsDS = new JRBeanCollectionDataSource(venta.getItems());
			params.put("dataSource", itemsDS);

			params.put("subtotal", venta.getSubtotal());
			params.put("subtotalPorcentaje", venta.getSubtotalPorcentaje());
			params.put("porcentaje", venta.getPorcentaje());
			params.put("total", venta.getTotal());

			// 4. Llenar el reporte y obtener JasperPrint
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());

			// 5. Exportar a PDF y devolver como byte[]
			return JasperExportManager.exportReportToPdf(jasperPrint);

		} catch (Exception e) {
			throw new RuntimeException("Error al generar reporte de remito", e);
		}
	}

	@Override
	public byte[] generarResumenCuentaCorriente(Date fechaEmision, Date fechaVencimiento, BigDecimal montoOriginal,
			BigDecimal saldoPendiente, String estadoCuenta, BigDecimal coeficiente, String nombreCliente,
			String documentoCliente, String domicilioCliente, String telefonoCliente, BigDecimal totalPagado,
			Date fechaReporte, JRBeanCollectionDataSource vencimientosDataSource,
			JRBeanCollectionDataSource aplicacionesDataSource) {
		try {
			// 1. Cargar el archivo JRXML desde resources
			InputStream reportStream = new ClassPathResource("reports/rptResumenCuenta.jrxml").getInputStream();

			// 2. Compilar el reporte
			JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

			// 3. Parámetros del reporte
			Map<String, Object> params = new HashMap<>();
			params.put("fechaEmision", fechaEmision);
			params.put("fechaVencimiento", fechaVencimiento);
			params.put("montoOriginal", montoOriginal);
			params.put("saldoPendiente", saldoPendiente);
			params.put("estadoCuenta", estadoCuenta);
			params.put("coeficiente", coeficiente);
			params.put("nombreCliente", nombreCliente);
			params.put("documentoCliente", documentoCliente);
			params.put("domicilioCliente", domicilioCliente);
			params.put("telefonoCliente", telefonoCliente);
			params.put("totalPagado", totalPagado);
			params.put("fechaReporte", fechaReporte);
			params.put("vencimientosDataSource", vencimientosDataSource);
			params.put("aplicacionesDataSource", aplicacionesDataSource);

			// 4. Llenar el reporte (usar JREmptyDataSource porque los datos están en los
			// DataSources)
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());

			// 5. Exportar a PDF
			return JasperExportManager.exportReportToPdf(jasperPrint);

		} catch (Exception e) {
			throw new RuntimeException("Error al generar resumen de cuenta corriente", e);
		}
	}

	@Override
	public byte[] generarReporteCajaDiaria(ReporteCajaDiaria reporte) {
		try (InputStream reportStream = new ClassPathResource("reports/rptCajaDiaria.jrxml").getInputStream()) {
			JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

			// Parámetros
			Map<String, Object> params = new HashMap<>();
			params.put("idCaja", reporte.idCaja());
			params.put("fechaEmision", LocalDate.now());
			params.put("fechaApertura", reporte.fechaApertura());
			params.put("horaApertura", reporte.horaApertura());
			params.put("fechaCierre", reporte.fechaCierre());
			params.put("horaCierre", reporte.horaCierre());
			params.put("saldoInicial", reporte.saldoInicial());
			params.put("totalIngresos", reporte.totalIngresos());
			params.put("totalEgresos", reporte.totalEgresos());
			params.put("saldoFinal", reporte.saldoFinal());

			// Convertir lista de records a lista de beans con getters
			List<MovimientoReporteBean> movimientosBeans = reporte.movimientos().stream()
					.map(MovimientoReporteBean::new).collect(Collectors.toList());

			// DataSource de movimientos
			JRBeanCollectionDataSource movimientosDS = new JRBeanCollectionDataSource(movimientosBeans);
			params.put("movimientosDataSource", movimientosDS);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
			return JasperExportManager.exportReportToPdf(jasperPrint);

		} catch (Exception e) {
			throw new RuntimeException("Error al generar reporte de caja diaria", e);
		}
	}

	private String construirCondicionVenta(Venta venta) {
		if (venta.getMediosPago() == null || venta.getMediosPago().isEmpty()) {
			return "No especificado";
		}

		return venta.getMediosPago().stream().map(ip -> ip.getTipoPago().getDescripcion())
				.collect(Collectors.joining(" + "));
	}
}
