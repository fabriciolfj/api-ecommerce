package com.github.fabriciolfj.apiecommerce.facade.converters;

import com.github.fabriciolfj.apiecommerce.entity.CardEntity;
import com.github.fabriciolfj.apiecommerce.entity.UserEntity;
import com.github.fabriciolfj.apiecommerce.model.AddCardReq;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CardConverter {

    public CardEntity toEntity(final AddCardReq req, final Optional<UserEntity> userEntity) {
        return CardEntity.builder()
                .number(req.getCardNumber())
                .cvv(req.getCvv())
                .user(userEntity.orElse(null))
                .expires(req.getExpires())
                .build();
    }
}
