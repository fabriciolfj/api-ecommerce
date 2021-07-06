package com.github.fabriciolfj.apiecommerce.service;

import com.github.fabriciolfj.apiecommerce.entity.AuthorizationEntity;
import com.github.fabriciolfj.apiecommerce.model.PaymentReq;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface PaymentService {

  Mono<AuthorizationEntity> authorize(@Valid Mono<PaymentReq> paymentReq);
  Mono<AuthorizationEntity> getOrdersPaymentAuthorization(@NotNull String orderId);
}
