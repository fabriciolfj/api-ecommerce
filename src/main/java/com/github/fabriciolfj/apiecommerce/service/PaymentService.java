package com.github.fabriciolfj.apiecommerce.service;

import com.github.fabriciolfj.apiecommerce.entity.AuthorizationEntity;
import com.github.fabriciolfj.apiecommerce.model.PaymentReq;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface PaymentService {

  Optional<AuthorizationEntity> authorize(@Valid final PaymentReq paymentReq);
  Optional<AuthorizationEntity> getOrdersPaymentAuthorization(@NotNull String orderId);
}
