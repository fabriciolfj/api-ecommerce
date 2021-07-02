package com.github.fabriciolfj.apiecommerce.hateoas;

import com.github.fabriciolfj.apiecommerce.controllers.CardController;
import com.github.fabriciolfj.apiecommerce.entity.CardEntity;
import com.github.fabriciolfj.apiecommerce.model.Card;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CardRepresentationModelAssembler extends RepresentationModelAssemblerSupport<CardEntity, Card> {

    public CardRepresentationModelAssembler() {
        super(CardController.class, Card.class);
    }

    @Override
    public Card toModel(final CardEntity entity) {
        String uid = Objects.nonNull(entity.getUser()) ? entity.getUser().getId().toString() : null;
        Card resource = createModelWithId(entity.getId(), entity);
        BeanUtils.copyProperties(entity, resource);
        updateResource(entity, uid, resource);
        return resource;
    }

    private void updateResource(final CardEntity entity, final String uid, final Card resource) {
        resource.id(
                entity.getId().toString())
                .cardNumber(entity.getNumber())
                .cvv(entity.getCvv())
                .expires(entity.getExpires())
                .userId(uid);

        resource.add(linkTo(methodOn(CardController.class).getCardById(entity.getId().toString())).withSelfRel());
    }

    public List<Card> toListModel(Iterable<CardEntity> entities) {
        if (Objects.isNull(entities)) return Collections.emptyList();
        return StreamSupport.stream(entities.spliterator(), false).map(e -> toModel(e)).collect(toList());
    }

}
