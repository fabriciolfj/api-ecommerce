package com.github.fabriciolfj.apiecommerce.repository;

import com.github.fabriciolfj.apiecommerce.entity.UserTokenEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserTokenRepository extends CrudRepository<UserTokenEntity, UUID> {

    Optional<UserTokenEntity> findByRefreshToken(final String refreshToken);
    Optional<UserTokenEntity> deleteByUserId(final UUID userID);
}
