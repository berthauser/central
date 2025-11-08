package com.visus.central.application.usecase;

import com.visus.central.domain.model.User;
import com.visus.central.domain.port.in.UserUseCase;
import com.visus.central.domain.port.out.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserUseCaseImpl implements UserUseCase {
	
	private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserUseCaseImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String getRoleFor(String username) {
        return repository.buscarPorUsername(username)
                .map(User::getRole)
                .orElse("ROLE_USER");
    }
    
    @Override
    public Optional<User> buscarPorUsername(String username) {
        return repository.buscarPorUsername(username);
    }

    @Override
    public boolean validateCredentials(String username, String rawPassword) {
        return repository.buscarPorUsername(username)
            .map(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
            .orElse(false);
    }

    @Override
    public void create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.guardar(user);
    }

    @Override
    public void update(User user) {
        repository.actualizar(user);
    }

    @Override
    public void delete(String username) {
        repository.eliminarPorUsername(username);
    }

    @Override
    public List<User> findAll() {
        return repository.listar();
    }

}
