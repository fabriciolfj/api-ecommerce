package com.github.fabriciolfj.apiecommerce.service.impl;

import com.github.fabriciolfj.apiecommerce.entity.ItemEntity;
import com.github.fabriciolfj.apiecommerce.entity.ProductEntity;
import com.github.fabriciolfj.apiecommerce.model.Item;
import com.github.fabriciolfj.apiecommerce.service.ItemService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
public class ItemServiceImpl implements ItemService {

    @Override
    public ItemEntity toEntity(final Item m) {
        ItemEntity e = new ItemEntity();
        e.setProduct(new ProductEntity().setId(UUID.fromString(m.getId()))).setPrice(m.getUnitPrice())
                .setQuantity(m.getQuantity());
        return e;
    }

    @Override
    public List<ItemEntity> toEntityList(final List<Item> items) {
        if (Objects.isNull(items)) {
            return Collections.emptyList();
        }
        return items.stream().map(m -> toEntity(m)).collect(toList());
    }

    @Override
    public Item toModel(final ItemEntity e) {
        final Item m = new Item();
        m.id(e.getProduct().getId().toString()).unitPrice(e.getPrice()).quantity(e.getQuantity());
        return m;
    }

    @Override
    public List<Item> toModelList(final List<ItemEntity> items) {
        if (Objects.isNull(items)) {
            return Collections.emptyList();
        }
        return items.stream().map(e -> toModel(e)).collect(toList());
    }
}
