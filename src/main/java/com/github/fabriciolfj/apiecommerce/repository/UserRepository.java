package com.github.fabriciolfj.apiecommerce.repository;

import com.github.fabriciolfj.apiecommerce.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface UserRepository extends ReactiveCrudRepository<UserEntity, UUID> {
}

