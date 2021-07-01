package com.github.fabriciolfj.apiecommerce.service;

import com.github.fabriciolfj.apiecommerce.entity.OrderEntity;
import com.github.fabriciolfj.apiecommerce.model.NewOrder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface OrderService {

  public Optional<OrderEntity> addOrder(@Valid final NewOrder newOrder);
  public Iterable<OrderEntity> getOrdersByCustomerId(@NotNull @Valid String customerId);
  public Optional<OrderEntity> getByOrderId(String id);
}
