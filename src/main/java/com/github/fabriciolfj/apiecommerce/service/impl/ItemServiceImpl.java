package com.github.fabriciolfj.apiecommerce.service.impl;

import com.github.fabriciolfj.apiecommerce.entity.ItemEntity;
import com.github.fabriciolfj.apiecommerce.facade.converters.ItemConverter;
import com.github.fabriciolfj.apiecommerce.model.Item;
import com.github.fabriciolfj.apiecommerce.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemConverter itemConverter;

    @Override
    public ItemEntity create(final Item m) {
        return itemConverter.toEntity(m);
    }

    @Override
    public List<ItemEntity> toEntityList(final List<Item> items) {
        if (Objects.isNull(items)) {
            return Collections.emptyList();
        }

        return items.stream().map(m -> create(m)).collect(toList());
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
