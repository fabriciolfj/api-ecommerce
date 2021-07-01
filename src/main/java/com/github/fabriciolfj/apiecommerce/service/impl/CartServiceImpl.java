package com.github.fabriciolfj.apiecommerce.service.impl;

import com.github.fabriciolfj.apiecommerce.entity.CartEntity;
import com.github.fabriciolfj.apiecommerce.entity.ItemEntity;
import com.github.fabriciolfj.apiecommerce.exceptions.CustomerNotFoundException;
import com.github.fabriciolfj.apiecommerce.exceptions.GenericAlreadyExistsException;
import com.github.fabriciolfj.apiecommerce.exceptions.ItemNotFoundException;
import com.github.fabriciolfj.apiecommerce.model.Item;
import com.github.fabriciolfj.apiecommerce.repository.CartRepository;
import com.github.fabriciolfj.apiecommerce.repository.UserRepository;
import com.github.fabriciolfj.apiecommerce.service.CartService;
import com.github.fabriciolfj.apiecommerce.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.stream.Collectors.toList;
import static org.springframework.objenesis.instantiator.util.UnsafeUtils.getUnsafe;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository repository;
    private final UserRepository userRepo;
    private final ItemService itemService;

    @Override
    public List<Item> addCartItemsByCustomerId(final String customerId, @Valid final Item item) {
        final var entity = getCartByCustomerId(customerId);
        validaItemExists(item, entity);

        entity.getItems().add(itemService.toEntity(item));
        return itemService.toModelList(repository.save(entity).getItems());
    }

    @Override
    public List<Item> addOrReplaceItemsByCustomerId(final String customerId, @Valid final Item item) {
        final var entity = getCartByCustomerId(customerId);
        final List<ItemEntity> items =
                Objects.nonNull(entity.getItems()) ? entity.getItems() : Collections.emptyList();

        final AtomicBoolean itemExists = new AtomicBoolean(false);

        items.forEach(i -> {
            if (i.isProductExists(item.getId())) {
                i.setQuantity(item.getQuantity()).setPrice(i.getPrice());
                itemExists.set(true);
            }
        });

        if (!itemExists.get()) {
            items.add(itemService.toEntity(item));
        }

        return itemService.toModelList(repository.save(entity).getItems());
    }

    @Override
    public void deleteCart(String customerId) {
        final var entity = getCartByCustomerId(customerId);
        repository.deleteById(entity.getId());
    }

    @Override
    public void deleteItemFromCart(final String customerId, final String itemId) {
        final CartEntity entity = getCartByCustomerId(customerId);
        entity.setItems(entity
                .getItems()
                .stream()
                .filter(i -> !i.isProductExists(itemId))
                .collect(toList()));
        repository.save(entity);
    }

    @Override
    public CartEntity getCartByCustomerId(String customerId) {
        final var entity = repository.findByCustomerId(UUID.fromString(customerId))
                .orElse(new CartEntity());

        setUserIsExists(customerId, entity);

        return entity;
    }

    @Override
    public List<Item> getCartItemsByCustomerId(String customerId) {
        final var entity = getCartByCustomerId(customerId);
        return itemService.toModelList(entity.getItems());
    }

    @Override
    public Item getCartItemsByItemId(final String customerId, final String itemId) {
        return getCartByCustomerId(customerId)
                .getItems()
                .stream()
                .filter(i -> i.isProductExists(itemId))
                .findFirst()
                .map(itemService::toModel)
                .orElseThrow(() -> new ItemNotFoundException(String.format(" - %s", itemId)));
    }

    private void validaItemExists(Item item, CartEntity entity) {
        long count = entity.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(UUID.fromString(item.getId()))).count();

        if (count > 0) {
            throw new GenericAlreadyExistsException(
                    String.format("Item with Id (%s) already exists. You can update it.", item.getId()));
        }
    }

    private void setUserIsExists(final String customerId, final CartEntity entity) {
        if (Objects.isNull(entity.getUser())) {
            entity.setUser(userRepo.findById(UUID.fromString(customerId))
                    .orElseThrow(() -> new CustomerNotFoundException(
                            String.format(" - %s", customerId))));
        }
    }
}
