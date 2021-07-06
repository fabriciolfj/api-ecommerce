package com.github.fabriciolfj.apiecommerce.service;

import com.github.fabriciolfj.apiecommerce.entity.CartEntity;
import com.github.fabriciolfj.apiecommerce.entity.ItemEntity;
import com.github.fabriciolfj.apiecommerce.model.Item;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ItemService {

  Mono<ItemEntity> toEntity(Mono<Item> e);

  Mono<List<Item>> fluxTolList(Flux<ItemEntity> items);

  Flux<Item> toItemFlux(Mono<CartEntity> items);

  ItemEntity toEntity(Item m);

  List<ItemEntity> toEntityList(List<Item> items);

  Item toModel(ItemEntity e);

  List<Item> toModelList(List<ItemEntity> items);

  Flux<Item> toModelFlux(List<ItemEntity> items);
}
