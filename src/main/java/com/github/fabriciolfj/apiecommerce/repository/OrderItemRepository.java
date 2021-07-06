package com.github.fabriciolfj.apiecommerce.repository;

import com.github.fabriciolfj.apiecommerce.entity.OrderItemEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;


public interface OrderItemRepository extends ReactiveCrudRepository<OrderItemEntity, UUID> {
}
