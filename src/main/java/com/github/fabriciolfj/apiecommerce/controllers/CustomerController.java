package com.github.fabriciolfj.apiecommerce.controllers;

import com.github.fabriciolfj.apiecommerce.api.CustomerApi;
import com.github.fabriciolfj.apiecommerce.exceptions.ResourceNotFoundException;
import com.github.fabriciolfj.apiecommerce.hateoas.AddressRepresentationModelAssembler;
import com.github.fabriciolfj.apiecommerce.hateoas.CardRepresentationModelAssembler;
import com.github.fabriciolfj.apiecommerce.hateoas.UserRepresentationModelAssembler;
import com.github.fabriciolfj.apiecommerce.model.Address;
import com.github.fabriciolfj.apiecommerce.model.Card;
import com.github.fabriciolfj.apiecommerce.model.User;
import com.github.fabriciolfj.apiecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.ResponseEntity.*;


@RequiredArgsConstructor
@RestController
public class CustomerController implements CustomerApi {

    private final UserRepresentationModelAssembler assembler;
    private final AddressRepresentationModelAssembler addrAssembler;
    private final CardRepresentationModelAssembler cardAssembler;
    private final UserService service;

    @Override
    public Mono<ResponseEntity<Void>> deleteCustomerById(String id, ServerWebExchange exchange) {
        return service.getCustomerById(id)
                .flatMap(c -> service.deleteCustomerById(c.getId().toString())
                        .then(Mono.just(status(HttpStatus.ACCEPTED).<Void>build())))
                .switchIfEmpty(Mono.just(notFound().build()));
    }

    @Override
    public Mono<ResponseEntity<Flux<Address>>> getAddressesByCustomerId(String id,
                                                                        ServerWebExchange exchange) {

        return Mono.just(ok(service.getAddressesByCustomerId(id)
                .map(c -> addrAssembler.entityToModel(c, exchange))
                .switchIfEmpty(
                        Mono.error(new ResourceNotFoundException("No address found for given customer")))));
    }

    @Override
    public Mono<ResponseEntity<Flux<User>>> getAllCustomers(ServerWebExchange exchange) {
        return Mono.just(ok(assembler.toListModel(service.getAllCustomers(), exchange)));
    }

    @Override
    public Mono<ResponseEntity<Card>> getCardByCustomerId(String id, ServerWebExchange exchange) {
        return service.getCardByCustomerId(id).map(c -> cardAssembler.entityToModel(c, exchange))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(notFound().build());
    }

    @Override
    public Mono<ResponseEntity<User>> getCustomerById(String id, ServerWebExchange exchange) {
        return service.getCustomerById(id).map(c -> assembler.entityToModel(c, exchange))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(notFound().build());
    }
}
