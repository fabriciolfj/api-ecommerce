package com.github.fabriciolfj.apiecommerce.controllers;

import com.github.fabriciolfj.apiecommerce.api.CartApi;
import com.github.fabriciolfj.apiecommerce.hateoas.CartRepresentationModelAssembler;
import com.github.fabriciolfj.apiecommerce.model.Cart;
import com.github.fabriciolfj.apiecommerce.model.Item;
import com.github.fabriciolfj.apiecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;


@Log4j2
@RestController
@RequiredArgsConstructor
public class CartsController implements CartApi {

    private final CartService service;
    private final CartRepresentationModelAssembler assembler;

    @Override
    public ResponseEntity<List<Item>> addCartItemsByCustomerId(final String customerId, @Valid final Item item) {
        log.info("Request for customer ID: {}\nItem: {}", customerId, item);
        return ok(service.addCartItemsByCustomerId(customerId, item));
    }

    @Override
    public ResponseEntity<List<Item>> addOrReplaceItemsByCustomerId(final String customerId, @Valid final Item item) {
        return ok(service.addCartItemsByCustomerId(customerId, item));
    }

    @Override
    public ResponseEntity<Void> deleteCart(final String customerId) {
        service.deleteCart(customerId);
        return noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteItemFromCart(final String customerId, final String itemId) {
        service.deleteItemFromCart(customerId, itemId);
        return noContent().build();
    }

    @Override
    public ResponseEntity<Cart> getCartByCustomerId(final String customerId) {
        return ok(assembler.toModel(service.getCartByCustomerId(customerId)));
    }

    @Override
    public ResponseEntity<List<Item>> getCartItemsByCustomerId(String customerId) {
        return ok(service.getCartItemsByCustomerId(customerId));
    }

    @Override
    public ResponseEntity<Item> getCartItemsByItemId(final String customerId, final String itemId) {
        return ok(service.getCartItemsByItemId(customerId, itemId));
    }
}
