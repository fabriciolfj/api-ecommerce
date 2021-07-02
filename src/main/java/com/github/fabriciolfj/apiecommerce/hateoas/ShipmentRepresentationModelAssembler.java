package com.github.fabriciolfj.apiecommerce.hateoas;

import com.github.fabriciolfj.apiecommerce.controllers.ShipmentController;
import com.github.fabriciolfj.apiecommerce.entity.ShipmentEntity;
import com.github.fabriciolfj.apiecommerce.model.Shipment;
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
public class ShipmentRepresentationModelAssembler extends RepresentationModelAssemblerSupport<ShipmentEntity, Shipment> {

    public ShipmentRepresentationModelAssembler() {
        super(ShipmentController.class, Shipment.class);
    }

    @Override
    public Shipment toModel(ShipmentEntity entity) {
        final var resource = createModelWithId(entity.getId(), entity);
        BeanUtils.copyProperties(entity, resource);
        resource.setId(entity.getId().toString());
        resource.add(linkTo(methodOn(ShipmentController.class).getShipmentByOrderId(entity.getId().toString())).withRel("byOrderId"));
        return resource;
    }

    public List<Shipment> toListModel(Iterable<ShipmentEntity> entities) {
        if (Objects.isNull(entities)) return Collections.emptyList();
        return StreamSupport.stream(entities.spliterator(), false).map(e -> toModel(e)).collect(toList());
    }

}
