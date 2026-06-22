package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.ItemPago;
import com.visus.central.domain.port.out.ItemPagoRepository;
import com.visus.central.infraestructure.converter.ItemPagoMapper;
import com.visus.central.infraestructure.persistence.entity.ItemPagoEntity;
import com.visus.central.infraestructure.persistence.repository.JpaItemPagoRepository;

import jakarta.transaction.Transactional;

@Component
public class ItemPagoAdapter implements ItemPagoRepository {

	private final JpaItemPagoRepository itemPagoRepository;
	private final ItemPagoMapper itemPagoMapper;

	public ItemPagoAdapter(JpaItemPagoRepository itemPagoRepository, ItemPagoMapper itemPagoMapper) {
		this.itemPagoRepository = itemPagoRepository;
		this.itemPagoMapper = itemPagoMapper;
	}

	@Override
	public ItemPago save(ItemPago itemPago) {
		ItemPagoEntity entity = itemPagoMapper.toEntity(itemPago);
		ItemPagoEntity saved = itemPagoRepository.save(entity);
		return itemPagoMapper.toModel(saved);
	}

	@Override
	public Optional<ItemPago> findById(Long id) {
		return itemPagoRepository.findById(id).map(itemPagoMapper::toModel);
	}

	@Override
	public List<ItemPago> findByVentaId(Long idVenta) {
		return itemPagoRepository.findByIdVenta(idVenta).stream().map(itemPagoMapper::toModel)
				.collect(Collectors.toList());
	}

	@Override
	public void deleteById(Long id) {
		itemPagoRepository.deleteById(id);
	}

	@Override
	@Transactional
	public void eliminarPorVentaId(Long idVenta) {
		itemPagoRepository.eliminarPorIdVenta(idVenta);

	}

}
