package com.github.fabriciolfj.apiecommerce.service;

import com.github.fabriciolfj.apiecommerce.entity.CardEntity;
import com.github.fabriciolfj.apiecommerce.model.AddCardReq;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

public interface CardService {
  Mono<Void> deleteCardById(String id);
  Mono<Void> deleteCardById(UUID id);
  Flux<CardEntity> getAllCards();
  Mono<CardEntity> getCardById(String id);
  Mono<CardEntity> registerCard(@Valid Mono<AddCardReq> addCardReq);
  CardEntity toEntity(AddCardReq model);
}
