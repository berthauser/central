package com.visus.central.application.usecase;

import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Service;

import com.visus.central.domain.port.in.HealthCheckUseCase;

@Service
public class HealthCheckUseCaseImpl implements HealthCheckUseCase {

    private final HealthEndpoint healthEndpoint;

    public HealthCheckUseCaseImpl(HealthEndpoint healthEndpoint) {
        this.healthEndpoint = healthEndpoint;
    }

    @Override
    public DatabaseHealthStatus checkDatabaseHealth() {
        try {
            HealthComponent healthComponent = healthEndpoint.health();
            Status status = healthComponent.getStatus();
            
            // Estrategia simple: si el estado general es UP, asumimos que la BD está bien
            // Esto funciona porque Spring Boot marca el health general como DOWN si la BD falla
            boolean isDatabaseConnected = status.equals(Status.UP);
            
            String details;
            if (isDatabaseConnected) {
                details = "Sistema y base de datos operativos";
            } else {
                // Analizar el mensaje para determinar si es problema de BD
                String healthMessage = healthComponent.toString();
                if (healthMessage.toLowerCase().contains("database") || 
                    healthMessage.toLowerCase().contains("postgres") ||
                    healthMessage.toLowerCase().contains("jdbc")) {
                    details = "Problema de conexión con la base de datos";
                } else {
                    details = "Problema en el sistema";
                }
            }
            
            return new DatabaseHealthStatus(
                isDatabaseConnected,
                status.getCode(),
                details,
                "PostgreSQL"
            );
            
        } catch (Exception e) {
            return new DatabaseHealthStatus(
                false, 
                "ERROR", 
                "Error al verificar estado: " + e.getMessage(), 
                "Unknown"
            );
        }
    }
}