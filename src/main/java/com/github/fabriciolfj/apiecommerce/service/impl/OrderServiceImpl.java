package com.github.fabriciolfj.apiecommerce.service.impl;

import com.github.fabriciolfj.apiecommerce.entity.CardEntity;
import com.github.fabriciolfj.apiecommerce.entity.ItemEntity;
import com.github.fabriciolfj.apiecommerce.entity.OrderEntity;
import com.github.fabriciolfj.apiecommerce.model.NewOrder;
import com.github.fabriciolfj.apiecommerce.repository.*;
import com.github.fabriciolfj.apiecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final UserRepository userRepo;
    private final AddressRepository addRepo;
    private final CardRepository cardRepo;
    private final ItemRepository itemRepo;
    private BiFunction<OrderEntity, List<ItemEntity>, OrderEntity> biOrderItem = (o, fi) -> o.setItems(fi);

    @Override
    public Mono<OrderEntity> addOrder(@Valid final Mono<NewOrder> newOrder) {
        return repository.insert(newOrder);
    }

    @Override
    public Mono<OrderEntity> updateMapping(@Valid OrderEntity orderEntity) {
        return repository.updateMapping(orderEntity);
    }

    @Override
    public Flux<OrderEntity> getOrdersByCustomerId(@NotNull @Valid String customerId) {
        return repository.findByCustomerId(UUID.fromString(customerId))
                .flatMap(order ->
                        toEnrichOrder(order));
    }

    @Override
    public Mono<OrderEntity> getByOrderId(String id) {
        return repository.findById(UUID.fromString(id))
                .flatMap(order -> toEnrichOrder(order));
    }

    private Mono<OrderEntity> toEnrichOrder(OrderEntity order) {
        return Mono.just(order)
                .zipWith(userRepo.findById(order.getCustomerId()))
                .map(t -> t.getT1().setUserEntity(t.getT2()))
                .zipWith(addRepo.findById(order.getAddressId()))
                .map(t -> t.getT1().setAddressEntity(t.getT2()))
                .zipWith(cardRepo.findById(order.getCardId() != null ? order.getCardId()
                        : UUID.fromString("0a59ba9f-629e-4445-8129-b9bce1985d6a"))
                        .defaultIfEmpty(new CardEntity()))
                .map(t -> t.getT1().setCardEntity(t.getT2()))
                .zipWith(itemRepo.findByCustomerId(order.getCustomerId().toString()).collectList()
                        , biOrderItem);
    }

}
