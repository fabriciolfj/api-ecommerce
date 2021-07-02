package com.github.fabriciolfj.apiecommerce.hateoas;

import com.github.fabriciolfj.apiecommerce.controllers.CartsController;
import com.github.fabriciolfj.apiecommerce.entity.CartEntity;
import com.github.fabriciolfj.apiecommerce.model.Cart;
import com.github.fabriciolfj.apiecommerce.service.ItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CartRepresentationModelAssembler extends RepresentationModelAssemblerSupport<CartEntity, Cart> {

    @Autowired
    private ItemService itemService;

    public CartRepresentationModelAssembler() {
        super(CartsController.class, Cart.class);
    }

    @Override
    public Cart toModel(final CartEntity entity) {
        var uid = Objects.nonNull(entity.getUser()) ? entity.getIdUser() : null;
        var cid = Objects.nonNull(entity.getId()) ? entity.getId().toString() : null;
        var resource = new Cart();
        BeanUtils.copyProperties(entity, resource);
        updateResources(entity, uid, cid, resource);
        return resource;
    }

    public List<Cart> toListModel(Iterable<CartEntity> entities) {
        if (Objects.isNull(entities)) {
            return Collections.emptyList();
        }

        return stream(entities.spliterator(), false)
                .map(e -> toModel(e))
                .collect(toList());
    }

    private void updateResources(CartEntity entity, String uid, String cid, Cart resource) {
        resource
                .id(cid)
                .customerId(uid)
                .items(itemService.toModelList(entity.getItems()));
        resource.add(linkTo(methodOn(CartsController.class).getCartByCustomerId(uid)).withSelfRel());
        resource.add(linkTo(methodOn(CartsController.class).getCartItemsByCustomerId(uid))
                .withRel("cart-items"));
    }
}
