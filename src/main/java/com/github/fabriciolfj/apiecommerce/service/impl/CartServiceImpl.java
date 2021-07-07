package com.github.fabriciolfj.apiecommerce.service.impl;

import com.github.fabriciolfj.apiecommerce.entity.CartEntity;
import com.github.fabriciolfj.apiecommerce.entity.ItemEntity;
import com.github.fabriciolfj.apiecommerce.exceptions.GenericAlreadyExistsException;
import com.github.fabriciolfj.apiecommerce.exceptions.ResourceNotFoundException;
import com.github.fabriciolfj.apiecommerce.model.Item;
import com.github.fabriciolfj.apiecommerce.repository.CartRepository;
import com.github.fabriciolfj.apiecommerce.repository.ItemRepository;
import com.github.fabriciolfj.apiecommerce.repository.UserRepository;
import com.github.fabriciolfj.apiecommerce.service.CartService;
import com.github.fabriciolfj.apiecommerce.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository repository;
    private final UserRepository userRepo;
    private final ItemRepository itemRepo;
    private final ItemService itemService;

    private BiFunction<CartEntity, List<ItemEntity>, CartEntity> cartItemBiFun = (c, i) -> c
            .setItems(i);

    @Override
    @Transactional
    public Flux<Item> addCartItemsByCustomerId(CartEntity cartEntity, @Valid Mono<Item> newItem) {
        final List<ItemEntity> cartItems = cartEntity.getItems();
        return newItem.flatMap(item -> {

            if (isItemExists(cartItems, item)) {
                return Mono
                        .error(new GenericAlreadyExistsException(String.format(
                                "Requested Item (%s) is already there in cart. Please make a PUT call for update.",
                                item.getId())));
            }

            return saveItemToCart(cartEntity, cartItems, item);
        }).flatMapMany(Flux::fromIterable);
    }

    @Override
    public Flux<Item> addOrReplaceItemsByCustomerId(final CartEntity cartEntity,
                                                    @Valid final Mono<Item> newItem) {
        final List<ItemEntity> cartItems = cartEntity.getItems();
        return newItem.flatMap(item -> {
            var itemEntity = findItemCart(cartItems, item.getId());
            if (itemEntity.isPresent()) {
                var exists = itemEntity.get();
                exists.setPrice(item.getUnitPrice()).setQuantity(item.getQuantity());
                return itemRepo.save(exists).flatMap(i -> getUpdatedList(
                        cartItems.stream().filter(j -> !j.getProductId().equals(UUID.fromString(item.getId())))
                                .collect(toList()), i));
            }

            return saveItemToCart(cartEntity, cartItems, item);
        }).flatMapMany(Flux::fromIterable);
    }

    @Override
    @Transactional
    public Mono<Void> deleteCart(final String customerId, final String cartId) {
        Mono<List<String>> monoIds = itemRepo.findByCustomerId(customerId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(
                        ". No items found in Cart of customer with Id - " + customerId)))
                .map(i -> i.getId().toString())
                .collectList().cache();
        return monoIds.zipWhen(l -> {
            List<String> ids = l.subList(0, l.size());
            return itemRepo.deleteCartItemJoinById(ids, cartId)
                    .then(itemRepo.deleteByIds(ids).subscribeOn(Schedulers.boundedElastic()));
        }).then();
    }

    @Override
    public Mono<Void> deleteItemFromCart(final CartEntity cartEntity, final String itemId) {
        List<ItemEntity> items = cartEntity.getItems();
        var item = findItemCart(items, itemId);
        if (item.isEmpty()) {
            return Mono
                    .error(new ResourceNotFoundException(". No items found in Cart with Id - " + itemId));
        }

        List<String> ids = items.stream().map(i -> i.getId().toString()).collect(toList());
        return itemRepo.deleteCartItemJoinById(ids, cartEntity.getId().toString())
                .then(itemRepo.deleteByIds(ids).subscribeOn(Schedulers.boundedElastic()));
    }

    @Override
    public Mono<CartEntity> getCartByCustomerId(String customerId) {
        Mono<CartEntity> cart = repository.findByCustomerId(customerId)
                .subscribeOn(Schedulers.boundedElastic());
        Flux<ItemEntity> items = itemRepo.findByCustomerId(customerId)
                .subscribeOn(Schedulers.boundedElastic());
        return Mono.zip(cart, items.collectList(), cartItemBiFun);
    }

    private boolean isItemExists(final List<ItemEntity> cartItems, final Item item) {
        return cartItems.stream()
                .filter(i -> i.getProductId().equals(UUID.fromString(item.getId()))).count() > 0;
    }

    private Mono<List<Item>> getUpdatedList(List<ItemEntity> cartItems, ItemEntity savedItem) {
        cartItems.add(savedItem);
        return Mono.just(itemService.toModelList(cartItems));
    }

    private Optional<ItemEntity> findItemCart(List<ItemEntity> cartItems, String itemId) {
        return cartItems.stream()
                .filter(i -> i.getProductId().equals(UUID.fromString(itemId)))
                .findAny();
    }

    private Mono<? extends List<Item>> saveItemToCart(CartEntity cartEntity, List<ItemEntity> cartItems, Item item) {
        return itemRepo.save(itemService.toEntity(item))
                .flatMap(i ->
                        itemRepo.saveMapping(cartEntity.getId().toString(), i.getId().toString())
                                .then(getUpdatedList(cartItems, i)));
    }
}
