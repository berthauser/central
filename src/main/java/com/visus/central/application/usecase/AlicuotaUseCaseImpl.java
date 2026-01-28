package com.visus.central.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.visus.central.domain.model.Alicuota;
import com.visus.central.domain.port.in.AlicuotaUseCase;
import com.visus.central.domain.port.out.AlicuotaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AlicuotaUseCaseImpl implements AlicuotaUseCase {
	
	private final AlicuotaRepository alicuotaRepository;

	public AlicuotaUseCaseImpl(AlicuotaRepository alicuotaRepository) {
		this.alicuotaRepository = alicuotaRepository;
	}

	@Override
    public List<Alicuota> findAll() {
        return alicuotaRepository.findAll();
    }

    @Override
    public Alicuota findById(Integer id) {
        return alicuotaRepository.findById(id)
            .orElse(null);
    }

    @Override
    public Alicuota save(Alicuota alicuota) {
        return alicuotaRepository.save(alicuota);
    }

    @Override
    public void deleteById(Integer id) {
    	alicuotaRepository.deleteById(id);
    }


}
