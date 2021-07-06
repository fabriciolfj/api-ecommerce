package com.github.fabriciolfj.apiecommerce.service;

import com.github.fabriciolfj.apiecommerce.entity.AddressEntity;
import com.github.fabriciolfj.apiecommerce.entity.CardEntity;
import com.github.fabriciolfj.apiecommerce.entity.UserEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
  Mono<Void> deleteCustomerById(String id);
  Mono<Void> deleteCustomerById(UUID id);
  Flux<AddressEntity> getAddressesByCustomerId(String id);
  Flux<UserEntity> getAllCustomers();
  Mono<CardEntity> getCardByCustomerId(String id);
  Mono<UserEntity> getCustomerById(String id);
}
