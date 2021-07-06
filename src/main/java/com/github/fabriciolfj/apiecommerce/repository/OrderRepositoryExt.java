package com.github.fabriciolfj.apiecommerce.repository;


import com.github.fabriciolfj.apiecommerce.entity.OrderEntity;
import com.github.fabriciolfj.apiecommerce.model.NewOrder;
import reactor.core.publisher.Mono;

import java.util.Optional;


public interface OrderRepositoryExt {
  Mono<OrderEntity> insert(Mono<NewOrder> m);

  Mono<OrderEntity> updateMapping(OrderEntity orderEntity);
}

