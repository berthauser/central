package com.visus.central.domain.port.in;

public interface PasswordChangeUseCase {
	
	void changePassword(String username, String oldPassword, String newPassword);

}
