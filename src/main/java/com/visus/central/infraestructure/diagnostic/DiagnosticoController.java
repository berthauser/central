package com.visus.central.infraestructure.diagnostic;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.visus.central.domain.port.in.DomicilioUseCase;
import com.visus.central.domain.port.out.DomicilioRepository;

@RestController
public class DiagnosticoController {
	
	@Autowired
    private ApplicationContext context;
    
    @GetMapping("/beans/domicilio-use-case")
    public String getDomicilioUseCaseBeans() {
        String[] beanNames = context.getBeanNamesForType(DomicilioUseCase.class);
        return "DomicilioUseCase beans: " + Arrays.toString(beanNames);
    }
    
    @GetMapping("/beans/domicilio-repository")
    public String getDomicilioRepositoryBeans() {
        String[] beanNames = context.getBeanNamesForType(DomicilioRepository.class);
        return "DomicilioRepository beans: " + Arrays.toString(beanNames);
    }
    
    @GetMapping("/beans/vendedor-use-case")
    public String getVendedorUseCaseBeans() {
        String[] beanNames = context.getBeanNamesForType(
            com.visus.central.domain.port.in.VendedorUseCase.class
        );
        return "VendedorUseCase beans: " + Arrays.toString(beanNames);
    }

}
