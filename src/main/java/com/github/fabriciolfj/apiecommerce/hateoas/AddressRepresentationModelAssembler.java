package com.github.fabriciolfj.apiecommerce.hateoas;

import com.github.fabriciolfj.apiecommerce.controllers.AddressController;
import com.github.fabriciolfj.apiecommerce.entity.AddressEntity;
import com.github.fabriciolfj.apiecommerce.model.Address;
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
public class AddressRepresentationModelAssembler extends
        RepresentationModelAssemblerSupport<AddressEntity, Address> {

    public AddressRepresentationModelAssembler() {
        super(AddressController.class, Address.class);
    }

    @Override
    public Address toModel(final AddressEntity entity) {
        Address resource = createModelWithId(entity.getId(), entity);
        BeanUtils.copyProperties(entity, resource);
        resource.setId(entity.getId().toString());
        resource.add(
                linkTo(methodOn(AddressController.class).getAddressesById(entity.getId().toString()))
                        .withSelfRel());
        return resource;
    }

    public List<Address> toListModel(final Iterable<AddressEntity> entities) {
        if (Objects.isNull(entities)) {
            return Collections.emptyList();
        }

        return StreamSupport.stream(entities.spliterator(), false).map(e -> toModel(e))
                .collect(toList());
    }

}
