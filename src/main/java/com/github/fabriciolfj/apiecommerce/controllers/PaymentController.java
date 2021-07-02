package com.github.fabriciolfj.apiecommerce.controllers;

import com.github.fabriciolfj.apiecommerce.api.PaymentApi;
import com.github.fabriciolfj.apiecommerce.hateoas.PaymentRepresentationModelAssembler;
import com.github.fabriciolfj.apiecommerce.model.Authorization;
import com.github.fabriciolfj.apiecommerce.model.PaymentReq;
import com.github.fabriciolfj.apiecommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@RestController
public class PaymentController implements PaymentApi {

    private final PaymentService service;
    private final PaymentRepresentationModelAssembler assembler;

    @Override
    public ResponseEntity<Authorization> authorize(@Valid PaymentReq paymentReq) {
        return null;
    }

    @Override
    public ResponseEntity<Authorization> getOrdersPaymentAuthorization(
            @NotNull @Valid String id) {
        return null;
    }
}
