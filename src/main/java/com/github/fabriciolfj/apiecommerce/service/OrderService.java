package com.github.fabriciolfj.apiecommerce.service;

import com.github.fabriciolfj.apiecommerce.entity.OrderEntity;
import com.github.fabriciolfj.apiecommerce.model.NewOrder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface OrderService {

  Mono<OrderEntity> addOrder(@Valid Mono<NewOrder> newOrder);

  Mono<OrderEntity> updateMapping(@Valid OrderEntity orderEntity);

  Flux<OrderEntity> getOrdersByCustomerId(@NotNull @Valid String customerId);

  Mono<OrderEntity> getByOrderId(String id);
}
