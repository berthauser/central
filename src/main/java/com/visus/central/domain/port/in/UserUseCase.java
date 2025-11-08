package com.visus.central.domain.port.in;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.User;

public interface UserUseCase {
	boolean validateCredentials(String username, String rawPassword);
	String getRoleFor(String username);
    void create(User user);
    void update(User user);
    void delete(String username);
    List<User> findAll();
    Optional<User> buscarPorUsername(String username);

}
