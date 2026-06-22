package com.visus.central.domain.port.out;

import java.util.List;

import com.visus.central.domain.model.NotaCredito;

public interface NotaCreditoRepository {

	NotaCredito save(NotaCredito notaCredito);

	List<NotaCredito> findNoConsumidosByClienteId(Integer idCliente);
}
