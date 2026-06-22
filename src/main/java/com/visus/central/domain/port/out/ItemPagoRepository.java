package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.ItemPago;

public interface ItemPagoRepository {
	ItemPago save(ItemPago itemPago);

	Optional<ItemPago> findById(Long id);

	List<ItemPago> findByVentaId(Long idVenta);

	void deleteById(Long id);

	void eliminarPorVentaId(Long idVenta);

}
