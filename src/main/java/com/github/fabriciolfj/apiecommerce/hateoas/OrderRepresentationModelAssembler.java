package com.github.fabriciolfj.apiecommerce.hateoas;

import com.github.fabriciolfj.apiecommerce.controllers.OrderController;
import com.github.fabriciolfj.apiecommerce.entity.OrderEntity;
import com.github.fabriciolfj.apiecommerce.model.Order;
import com.github.fabriciolfj.apiecommerce.service.ItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class OrderRepresentationModelAssembler extends
        RepresentationModelAssemblerSupport<OrderEntity, Order> {

    private UserRepresentationModelAssembler uAssembler;
    private AddressRepresentationModelAssembler aAssembler;
    private CardRepresentationModelAssembler cAssembler;
    private ShipmentRepresentationModelAssembler sAssembler;
    private ItemService itemService;

    public OrderRepresentationModelAssembler(UserRepresentationModelAssembler uAssembler,
                                             AddressRepresentationModelAssembler aAssembler, CardRepresentationModelAssembler cAssembler,
                                             ShipmentRepresentationModelAssembler sAssembler, ItemService itemService) {
        super(OrderController.class, Order.class);
        this.uAssembler = uAssembler;
        this.aAssembler = aAssembler;
        this.cAssembler = cAssembler;
        this.sAssembler = sAssembler;
        this.itemService = itemService;
    }

    @Override
    public Order toModel(OrderEntity entity) {
        System.out.println("\n\n\nentity = " + entity);
        Order resource = createModelWithId(entity.getId(), entity);
        BeanUtils.copyProperties(entity, resource);
        updateResource(entity, resource);
        return resource;
    }

    public List<Order> toListModel(Iterable<OrderEntity> entities) {
        if (Objects.isNull(entities)) {
            return Collections.emptyList();
        }
        return StreamSupport.stream(entities.spliterator(), false).map(e -> toModel(e))
                .collect(toList());
    }

    private void updateResource(OrderEntity entity, Order resource) {
        resource.id(entity.getId().toString())
                .customer(uAssembler.toModel(entity.getUserEntity()))
                .address(aAssembler.toModel(entity.getAddressEntity()))
                .card(cAssembler.toModel(entity.getCardEntity()))
                .items(itemService.toModelList(entity.getItems()))
                .date(entity.getOrderDate().toInstant().atOffset(ZoneOffset.UTC));
        resource.add(linkTo(methodOn(OrderController.class).getByOrderId(entity.getId().toString()))
                .withSelfRel());
    }

}
