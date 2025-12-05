package com.doostep.server.repo;

import com.doostep.server.model.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepo extends MongoRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);
}
