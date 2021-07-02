package com.github.fabriciolfj.apiecommerce.facade.validation;

import com.github.fabriciolfj.apiecommerce.entity.CartEntity;
import com.github.fabriciolfj.apiecommerce.exceptions.GenericAlreadyExistsException;
import com.github.fabriciolfj.apiecommerce.model.Item;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ValidationCartFacade {

    public void execute(final Item item, final CartEntity entity) {
        long count = entity.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(UUID.fromString(item.getId()))).count();

        if (count > 0) {
            throw new GenericAlreadyExistsException(
                    String.format("Item with Id (%s) already exists. You can update it.", item.getId()));
        }
    }
}
