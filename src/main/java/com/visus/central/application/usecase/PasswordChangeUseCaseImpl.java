package com.visus.central.application.usecase;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.visus.central.domain.model.User;
import com.visus.central.domain.port.in.PasswordChangeUseCase;
import com.visus.central.domain.port.out.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PasswordChangeUseCaseImpl implements PasswordChangeUseCase {
	
	private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public PasswordChangeUseCaseImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        User user = repository.buscarPorUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("La contraseña actual no es correcta");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        repository.actualizar(user);
    }
	
}
