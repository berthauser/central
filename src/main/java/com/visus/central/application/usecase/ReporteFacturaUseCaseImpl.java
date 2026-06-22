package com.visus.central.application.usecase;

import org.springframework.beans.factory.annotation.Autowired;

import com.visus.central.domain.model.Venta;
import com.visus.central.domain.port.in.ReporteFacturaUseCase;
import com.visus.central.domain.port.out.ReportGenerator;

public class ReporteFacturaUseCaseImpl implements ReporteFacturaUseCase {

    private final ReportGenerator reportGenerator;

    @Autowired
    public ReporteFacturaUseCaseImpl(ReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

    @Override
    public byte[] generarRemito(Venta venta) {
        return reportGenerator.generarReporteRemito(venta);
    }
}
