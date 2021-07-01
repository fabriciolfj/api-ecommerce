package com.github.fabriciolfj.apiecommerce.repository;


import com.github.fabriciolfj.apiecommerce.entity.OrderEntity;
import com.github.fabriciolfj.apiecommerce.model.NewOrder;

import java.util.Optional;


public interface OrderRepositoryExt {
  Optional<OrderEntity> insert(NewOrder m);
}

