package com.github.fabriciolfj.apiecommerce.repository;

import com.github.fabriciolfj.apiecommerce.entity.ItemEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface ItemRepository extends ReactiveCrudRepository<ItemEntity, UUID> {
  @Query("select i.* from cart c, item i, user u, cart_item ci where u.id = :customerId and c.user_id=u.id and c.id=ci.cart_id and i.id=ci.item_id")
  Flux<ItemEntity> findByCustomerId(String customerId);

  @Query("delete from cart_item where item_id in (:ids) and cart_id = :cartId")
  Mono<Void> deleteCartItemJoinById(List<String> ids, String cartId);

  @Query("delete from item where id in (:ids)")
  Mono<Void> deleteByIds(List<String> ids);

  @Query("insert into cart_item(cart_id, item_id) values(:cartId, :itemId)")
  Mono<Void> saveMapping(String cartId, String itemId);

}
