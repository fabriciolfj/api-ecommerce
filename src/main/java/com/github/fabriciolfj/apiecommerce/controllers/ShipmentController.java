package com.github.fabriciolfj.apiecommerce.controllers;

import com.github.fabriciolfj.apiecommerce.api.ShipmentApi;
import com.github.fabriciolfj.apiecommerce.hateoas.ShipmentRepresentationModelAssembler;
import com.github.fabriciolfj.apiecommerce.model.Shipment;
import com.github.fabriciolfj.apiecommerce.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@RestController
public class ShipmentController implements ShipmentApi {

    private final ShipmentRepresentationModelAssembler assembler;
    private final ShipmentService service;

    @Override
    public Mono<ResponseEntity<Flux<Shipment>>> getShipmentByOrderId(@NotNull @Valid String id,
                                                                     ServerWebExchange exchange) {
        return Mono
                .just(ResponseEntity.ok(assembler.toListModel(service.getShipmentByOrderId(id), exchange)));
    }
}
