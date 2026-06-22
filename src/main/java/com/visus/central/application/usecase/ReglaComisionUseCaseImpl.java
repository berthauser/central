package com.visus.central.application.usecase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.visus.central.domain.model.ReglaComision;
import com.visus.central.domain.model.TipoBaseComisionable;
import com.visus.central.domain.model.TipoCalculoComision;
import com.visus.central.domain.model.TipoEventoComision;
import com.visus.central.domain.model.Vendedor;
import com.visus.central.domain.port.in.ReglaComisionUseCase;
import com.visus.central.domain.port.out.ReglaComisionRepository;

@Service
@Transactional
public class ReglaComisionUseCaseImpl implements ReglaComisionUseCase {

	private static final BigDecimal PORCENTAJE_GLOBAL_DEFAULT = new BigDecimal("5.0000");

	private final ReglaComisionRepository repository;
	private final com.visus.central.domain.port.out.VendedorRepository vendedorRepository;

	public ReglaComisionUseCaseImpl(ReglaComisionRepository repository,
			com.visus.central.domain.port.out.VendedorRepository vendedorRepository) {
		this.repository = repository;
		this.vendedorRepository = vendedorRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReglaComision> findAll() {
		return repository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReglaComision> findByVendedorId(Integer idVendedor) {
		return repository.findByVendedorId(idVendedor);
	}

	@Override
	@Transactional(readOnly = true)
	public ReglaComision findById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Regla no encontrada: " + id));
	}

	@Override
	public ReglaComision save(ReglaComision regla) {
		if (regla.getId() == null) {
			regla.setFechaCreacion(LocalDate.now());
		}
		if (regla.getActivo() == null) {
			regla.setActivo(true);
		}
		return repository.save(regla);
	}

	@Override
	public void deleteById(Long id) {
		repository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public ReglaComision resolverReglaActiva(Integer idVendedor) {
		// 1. Buscar regla específica del vendedor
		var porVendedor = repository.findActivaByVendedorId(idVendedor);
		if (porVendedor.isPresent()) {
			return porVendedor.get();
		}

		// 2. Buscar regla global (sin vendedor asignado)
		var global = repository.findReglaGlobal();
		if (global.isPresent()) {
			return global.get();
		}

		// 3. Crear una ReglaComision sintética con valores por defecto
		Vendedor vendedor = vendedorRepository.findById(idVendedor)
				.orElse(null);

		ReglaComision defaultRule = new ReglaComision();
		defaultRule.setVendedor(vendedor);
		defaultRule.setNombre("Regla por defecto (5%)");
		defaultRule.setTipoBaseComisionable(TipoBaseComisionable.MONTO_PAGO);
		defaultRule.setTipoCalculo(TipoCalculoComision.PORCENTAJE);
		defaultRule.setValorCalculo(PORCENTAJE_GLOBAL_DEFAULT);
		defaultRule.setTipoEvento(TipoEventoComision.COBRO);
		defaultRule.setIncluirDescuentos(true);
		defaultRule.setAjustarDevoluciones(true);
		defaultRule.setVentanaAjusteDias(30);
		defaultRule.setActivo(true);
		return defaultRule;
	}
}
