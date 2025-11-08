package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Banco;

public interface BancoRepository {
	List<Banco> findAll();
    Optional<Banco> findById(Integer id);
    Banco save(Banco banco);
    void deleteById(Integer id);
}
