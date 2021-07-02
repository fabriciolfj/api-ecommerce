package com.github.fabriciolfj.apiecommerce.facade.converters;

import com.github.fabriciolfj.apiecommerce.entity.ItemEntity;
import com.github.fabriciolfj.apiecommerce.entity.ProductEntity;
import com.github.fabriciolfj.apiecommerce.model.Item;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ItemConverter {

    public ItemEntity toEntity(final Item m) {
        return ItemEntity
                .builder()
                .product(ProductEntity
                        .builder()
                        .id(UUID.fromString(m.getId()))
                        .price(m.getUnitPrice())
                        .build())
                .quantity(m.getQuantity())
                .price(m.getUnitPrice())
                .build();
    }
}
