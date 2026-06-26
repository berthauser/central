package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Lista;

public interface ListaRepository {
    List<Lista> findAll();
    Optional<Lista> findById(Integer id);
    Lista save(Lista lista);
    void deleteById(Integer id);
}
