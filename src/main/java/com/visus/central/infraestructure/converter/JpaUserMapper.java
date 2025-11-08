package com.visus.central.infraestructure.converter;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.User;
import com.visus.central.infraestructure.persistence.entity.JpaUserEntity;

@Component
public class JpaUserMapper {
	
	public User toDomain(JpaUserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setUsername(entity.getUsername());
        user.setPassword(entity.getPassword());
        user.setRole(entity.getRole());
        return user;
    }

    public JpaUserEntity toEntity(User model) {
        JpaUserEntity entity = new JpaUserEntity();
        entity.setId(model.getId());
        entity.setUsername(model.getUsername());
        entity.setPassword(model.getPassword());
        entity.setRole(model.getRole());
        return entity;
    }

}
