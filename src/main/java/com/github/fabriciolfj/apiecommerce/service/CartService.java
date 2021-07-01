package com.github.fabriciolfj.apiecommerce.service;

import com.github.fabriciolfj.apiecommerce.entity.CartEntity;
import com.github.fabriciolfj.apiecommerce.model.Item;

import javax.validation.Valid;
import java.util.List;

public interface CartService {

    List<Item> addCartItemsByCustomerId(final String customerId, @Valid final Item item);
    List<Item> addOrReplaceItemsByCustomerId(final String customerId, @Valid final Item item);
    void deleteCart(final String customerId);
    void deleteItemFromCart(final String customerId, final String itemId);
    CartEntity getCartByCustomerId(final String customerId);
    List<Item> getCartItemsByCustomerId(final String customerId);
    Item getCartItemsByItemId(final String customerId, final String itemId);
}
