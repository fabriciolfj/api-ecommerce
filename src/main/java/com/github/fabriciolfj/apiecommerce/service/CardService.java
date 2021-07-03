package com.github.fabriciolfj.apiecommerce.service;

import com.github.fabriciolfj.apiecommerce.entity.CardEntity;
import com.github.fabriciolfj.apiecommerce.model.AddCardReq;

import javax.validation.Valid;
import java.util.Optional;

public interface CardService {
    void deleteCardById(String id);

    Iterable<CardEntity> getAllCards();

    Optional<CardEntity> getCardById(String id);

    Optional<CardEntity> registerCard(@Valid final AddCardReq addCardReq);
}
