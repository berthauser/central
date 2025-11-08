package com.visus.central.domain.port.in;

public interface HealthCheckUseCase {
	DatabaseHealthStatus checkDatabaseHealth();
	record DatabaseHealthStatus(
			boolean isConnected,
			String status,
			String message,
			String databaseType
			) {
		
	}

}
