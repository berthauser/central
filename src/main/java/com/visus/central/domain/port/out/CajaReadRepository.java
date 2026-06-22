package com.visus.central.domain.port.out;

import com.visus.central.domain.port.out.dto.ReporteCajaDiaria;

public interface CajaReadRepository {
	
	ReporteCajaDiaria obtenerReporteCajaDiaria(Integer idCaja);

}
