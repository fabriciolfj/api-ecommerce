package com.github.fabriciolfj.apiecommerce.service.impl;

import com.github.fabriciolfj.apiecommerce.entity.AuthorizationEntity;
import com.github.fabriciolfj.apiecommerce.model.PaymentReq;
import com.github.fabriciolfj.apiecommerce.repository.OrderRepository;
import com.github.fabriciolfj.apiecommerce.repository.PaymentRepository;
import com.github.fabriciolfj.apiecommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepo;

    @Override
    public Optional<AuthorizationEntity> authorize(@Valid final PaymentReq paymentReq) {
        return Optional.empty();
    }

    @Override
    public Optional<AuthorizationEntity> getOrdersPaymentAuthorization(@NotNull String orderId) {
        return orderRepo.findById(UUID.fromString(orderId)).map(oe -> oe.getAuthorizationEntity());
    }
}
