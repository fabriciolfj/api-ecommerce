package com.github.fabriciolfj.apiecommerce.service.impl;

import com.github.fabriciolfj.apiecommerce.entity.CardEntity;
import com.github.fabriciolfj.apiecommerce.model.AddCardReq;
import com.github.fabriciolfj.apiecommerce.repository.CardRepository;
import com.github.fabriciolfj.apiecommerce.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository repository;

    @Override
    public Mono<Void> deleteCardById(String id) {
        return deleteCardById(UUID.fromString(id));
    }

    @Override
    public Mono<Void> deleteCardById(UUID id) {
        return repository.deleteById(id);
    }

    @Override
    public Flux<CardEntity> getAllCards() {
        return repository.findAll();
    }

    @Override
    public Mono<CardEntity> getCardById(String id) {
        return repository.findById(UUID.fromString(id));
    }

    @Override
    public Mono<CardEntity> registerCard(@Valid Mono<AddCardReq> addCardReq) {
        return addCardReq.map(this::toEntity).flatMap(repository::save);
    }

    @Override
    public CardEntity toEntity(AddCardReq model) {
        final CardEntity e = new CardEntity();
        BeanUtils.copyProperties(model, e);
        e.setNumber(model.getCardNumber());
        return e;
    }
}
