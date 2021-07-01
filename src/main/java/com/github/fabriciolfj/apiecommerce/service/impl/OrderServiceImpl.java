package com.github.fabriciolfj.apiecommerce.service.impl;

import com.github.fabriciolfj.apiecommerce.entity.OrderEntity;
import com.github.fabriciolfj.apiecommerce.facade.ValidationNewOrderFacade;
import com.github.fabriciolfj.apiecommerce.model.NewOrder;
import com.github.fabriciolfj.apiecommerce.repository.OrderRepository;
import com.github.fabriciolfj.apiecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final ValidationNewOrderFacade validation;

    @Override
    public Optional<OrderEntity> addOrder(@Valid final NewOrder newOrder) {
        validation.execute(newOrder);
        return repository.insert(newOrder);
    }

    @Override
    public Iterable<OrderEntity> getOrdersByCustomerId(@NotNull @Valid String customerId) {
        return repository.findByCustomerId(UUID.fromString(customerId));
    }

    @Override
    public Optional<OrderEntity> getByOrderId(String id) {
        return repository.findById(UUID.fromString(id));
    }

}
