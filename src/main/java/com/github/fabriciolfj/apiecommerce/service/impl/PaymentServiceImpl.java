package com.github.fabriciolfj.apiecommerce.service.impl;

import com.github.fabriciolfj.apiecommerce.entity.AuthorizationEntity;
import com.github.fabriciolfj.apiecommerce.model.PaymentReq;
import com.github.fabriciolfj.apiecommerce.repository.OrderRepository;
import com.github.fabriciolfj.apiecommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepo;

    @Override
    public Mono<AuthorizationEntity> authorize(@Valid Mono<PaymentReq> paymentReq) {
        return Mono.empty();
    }

    @Override
    public Mono<AuthorizationEntity> getOrdersPaymentAuthorization(@NotNull String orderId) {
        return orderRepo.findById(UUID.fromString(orderId)).map(oe -> oe.getAuthorizationEntity());
    }
}
