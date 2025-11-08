package com.visus.central.infraestructure.persistence.adapter;

import com.visus.central.domain.model.User;
import com.visus.central.domain.port.out.UserRepository;
import com.visus.central.infraestructure.persistence.repository.JpaUserRepository;

import jakarta.transaction.Transactional;

import com.visus.central.infraestructure.converter.JpaUserMapper;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PostgresUserAdapter implements UserRepository {
    
    private final JpaUserRepository jpaRepo;
    private final JpaUserMapper mapper;

//    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public PostgresUserAdapter(JpaUserRepository jpaRepo, JpaUserMapper mapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
    }

    @Override
    public Optional<User> buscarPorUsername(String username) {
        return jpaRepo.findByUsername(username).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void guardar(User user) {
        jpaRepo.save(mapper.toEntity(user));
    }

    @Override
    public void actualizar(User user) {
        jpaRepo.save(mapper.toEntity(user));
    }

    @Override
    public void eliminarPorUsername(String username) {
        jpaRepo.findByUsername(username).ifPresent(jpaRepo::delete);
    }

    @Override
    public List<User> listar() {
        return jpaRepo.findAll().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

}