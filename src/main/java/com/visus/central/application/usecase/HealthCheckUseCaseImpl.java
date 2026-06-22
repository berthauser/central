package com.visus.central.application.usecase;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.visus.central.domain.port.in.HealthCheckUseCase;

@Service
public class HealthCheckUseCaseImpl implements HealthCheckUseCase {

private static final Logger log = LoggerFactory.getLogger(HealthCheckUseCaseImpl.class);
    
    private final DataSource dataSource;

    public HealthCheckUseCaseImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public DatabaseHealthStatus checkDatabaseHealth() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute("SELECT 1");
            String dbName = conn.getMetaData().getDatabaseProductName();
            String dbVersion = conn.getMetaData().getDatabaseProductVersion();
            
            return new DatabaseHealthStatus(
                true, "UP",
                "Conexión exitosa a " + dbName + " " + dbVersion,
                dbName
            );
        } catch (Exception e) {
            log.error("Error verificando salud de BD", e);
            return new DatabaseHealthStatus(
                false, "DOWN",
                "Error de conexión: " + e.getMessage(),
                "Unknown"
            );
        }
    }
}