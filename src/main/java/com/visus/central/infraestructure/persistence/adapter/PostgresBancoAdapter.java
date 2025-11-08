package com.visus.central.infraestructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Banco;
import com.visus.central.domain.port.out.BancoRepository;
import com.visus.central.infraestructure.persistence.entity.JpaBancoEntity;
import com.visus.central.infraestructure.persistence.repository.JpaBancoRepository;

import jakarta.transaction.Transactional;

@Component
@Transactional
public class PostgresBancoAdapter implements BancoRepository {

	private final JpaBancoRepository jpaRepository;

	public PostgresBancoAdapter(JpaBancoRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	@Override
	public List<Banco> findAll() {
		return jpaRepository.findAll().stream()
				.map(this::toModel)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<Banco> findById(Integer id) {
		return jpaRepository.findById(id).map(this::toModel);
	}

	@Override
	public Banco save(Banco banco) {
		JpaBancoEntity entity = toEntity(banco);
		JpaBancoEntity saved = jpaRepository.save(entity);
		return toModel(saved);
	}

	@Override
	public void deleteById(Integer id) {
		jpaRepository.deleteById(id);
	}

	private Banco toModel(JpaBancoEntity entity) {
		Banco model = new Banco();
		model.setId(entity.getId());
		model.setNombre(entity.getNombre());
		model.setIdBcoCen(entity.getIdBcoCen()); // 👈 AGREGADO
		return model;
	}

	private JpaBancoEntity toEntity(Banco model) {
		JpaBancoEntity entity = new JpaBancoEntity();
		entity.setId(model.getId());
		entity.setNombre(model.getNombre());
		entity.setIdBcoCen(model.getIdBcoCen()); // 👈 AGREGADO
		return entity;
	}
}