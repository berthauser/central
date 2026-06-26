package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Porcentual;

public interface PorcentualRepository {
    List<Porcentual> findAll();
    Optional<Porcentual> findById(Integer id);
    Porcentual save(Porcentual porcentual);
    void deleteById(Integer id);
}
