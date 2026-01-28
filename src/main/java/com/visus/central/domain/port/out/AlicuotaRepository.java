package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.Alicuota;

public interface AlicuotaRepository {
	List<Alicuota> findAll();
    Optional<Alicuota> findById(Integer id);
    Alicuota save(Alicuota alicuota);
    void deleteById(Integer id);

}
