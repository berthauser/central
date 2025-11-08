package com.visus.central.domain.port.in;

import java.util.List;

public interface CrudUseCase <T> {
    List<T> findAll();
    T findById(Integer id);
    T save(T model);
    void deleteById(Integer id);

}
