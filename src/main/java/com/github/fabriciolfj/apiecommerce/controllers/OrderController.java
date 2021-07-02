package com.github.fabriciolfj.apiecommerce.controllers;

import com.github.fabriciolfj.apiecommerce.api.OrderApi;
import com.github.fabriciolfj.apiecommerce.hateoas.OrderRepresentationModelAssembler;
import com.github.fabriciolfj.apiecommerce.model.NewOrder;
import com.github.fabriciolfj.apiecommerce.model.Order;
import com.github.fabriciolfj.apiecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.List;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
public class OrderController implements OrderApi {

    private final OrderRepresentationModelAssembler assembler;
    private final OrderService service;

    @Override
    public ResponseEntity<Order> addOrder(@Valid NewOrder newOrder) {
        return service.addOrder(newOrder).map(assembler::toModel).map(ResponseEntity::ok)
                .orElse(notFound().build());
    }

    @Override
    public ResponseEntity<List<Order>> getOrdersByCustomerId(@NotNull @Valid String customerId) {
        return ok(assembler.toListModel(service.getOrdersByCustomerId(customerId)));
    }

    @Override
    public ResponseEntity<Order> getByOrderId(String id) {
        return service.getByOrderId(id).map(assembler::toModel).map(ResponseEntity::ok)
                .orElse(notFound().build());
    }
}
