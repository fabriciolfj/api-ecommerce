package com.github.fabriciolfj.apiecommerce.repository;

import com.github.fabriciolfj.apiecommerce.entity.CartEntity;
import com.github.fabriciolfj.apiecommerce.entity.ItemEntity;
import com.github.fabriciolfj.apiecommerce.entity.OrderEntity;
import com.github.fabriciolfj.apiecommerce.entity.OrderItemEntity;
import com.github.fabriciolfj.apiecommerce.exceptions.ResourceNotFoundException;
import com.github.fabriciolfj.apiecommerce.model.NewOrder;
import com.github.fabriciolfj.apiecommerce.model.Order;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Transactional
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryExt {

  @PersistenceContext
  private EntityManager em;

  private final ItemRepository itemRepo;
  private final CartRepository cRepo;
  private final OrderItemRepository oiRepo;
  private Timestamp orderDate = Timestamp.from(Instant.now());;

  @Override
  public Optional<OrderEntity> insert(final NewOrder m) {
    final var items = getItemEntities(m);
    validListItems(m, items);

    final var total = calcTotal(items);
    createNewOrder(m, total, orderDate);

    final var cart = findCart(m);
    clearItemsCart(cart);

    final var entity = getOrderEntity(m);
    saveItemsOrder(cart, entity);
    return Optional.of(entity);
  }

  private void clearItemsCart(CartEntity cart) {
    itemRepo.deleteCartItemJoinById(cart.getItems().stream().map(i -> i.getId()).collect(toList()), cart.getId());
  }

  private List<ItemEntity> getItemEntities(final NewOrder m) {
    final var dbItems = itemRepo.findByCustomerId(m.getCustomerId());
    List<ItemEntity> items = StreamSupport.stream(dbItems.spliterator(), false).collect(toList());
    return items;
  }

  private void saveItemsOrder(final CartEntity cart, final OrderEntity entity) {
    final var itens = cart.getItems().stream().map(i -> new OrderItemEntity().setOrderId(entity.getId()).setItemId(i.getId())).collect(toList());
    oiRepo.saveAll(itens);
  }

  private OrderEntity getOrderEntity(final NewOrder m) {
    return (OrderEntity) em.createNativeQuery("SELECT o.* FROM ecomm.orders o WHERE o.customer_id = ? AND o.order_date >= ?",
            OrderEntity.class)
        .setParameter(1, m.getCustomerId())
        .setParameter(2, OffsetDateTime.ofInstant(orderDate.toInstant(), ZoneId.of("Z")).truncatedTo(
            ChronoUnit.MICROS))
        .getSingleResult();
  }

  private CartEntity findCart(final NewOrder m) {
    return cRepo.findByCustomerId(UUID.fromString(m.getCustomerId()))
            .orElseThrow(() ->
                    new ResourceNotFoundException(String.format("Cart not found for given customer (ID: %s)", m.getCustomerId())));
  }

  private BigDecimal calcTotal(final List<ItemEntity> items) {
    return items.stream().map(i -> BigDecimal.valueOf(i.getQuantity()).multiply(i.getPrice()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private void validListItems(final NewOrder m, final List<ItemEntity> items) {
    if (items.size() < 1) {
      throw new ResourceNotFoundException(String
          .format("There is no item found in customer's (ID: %s) cart.", m.getCustomerId()));
    }
  }

  private void createNewOrder(final NewOrder m, final BigDecimal total, final Timestamp orderDate) {
    em.createNativeQuery("INSERT INTO ecomm.orders (address_id, card_id, customer_id, order_date, total, status)  VALUES(?, ?, ?, ?, ?, ?) ")
        .setParameter(1, m.getAddress().getId())
        .setParameter(2, m.getCard().getId())
        .setParameter(3, m.getCustomerId())
        .setParameter(4, orderDate)
        .setParameter(5, total)
        .setParameter(6, Order.StatusEnum.CREATED.getValue())
        .executeUpdate();
  }
}
