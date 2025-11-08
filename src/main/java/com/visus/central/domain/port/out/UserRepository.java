package com.visus.central.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.visus.central.domain.model.User;

public interface UserRepository {
	Optional<User> buscarPorUsername(String username);
	void guardar(User user);
    void actualizar(User user);
    void eliminarPorUsername(String username);
    List<User> listar();
    // Podés agregar más métodos si aplica: guardar, listar, etc.

}
