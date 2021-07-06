package com.github.fabriciolfj.apiecommerce.repository;

import com.github.fabriciolfj.apiecommerce.entity.CartEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface CartRepository extends ReactiveCrudRepository<CartEntity, UUID> {

    @Query("select c.* from cart c join user u on c.user_id=u.id where u.id = :customerId")
    Mono<CartEntity> findByCustomerId(String customerId);
}
