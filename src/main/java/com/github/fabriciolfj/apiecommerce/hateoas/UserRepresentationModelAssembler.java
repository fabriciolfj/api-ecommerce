package com.github.fabriciolfj.apiecommerce.hateoas;

import com.github.fabriciolfj.apiecommerce.controllers.CustomerController;
import com.github.fabriciolfj.apiecommerce.entity.UserEntity;
import com.github.fabriciolfj.apiecommerce.model.User;
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
public class UserRepresentationModelAssembler extends
    RepresentationModelAssemblerSupport<UserEntity, User> {

  public UserRepresentationModelAssembler() {
    super(CustomerController.class, User.class);
  }

  @Override
  public User toModel(UserEntity entity) {
    final var resource = createModelWithId(entity.getId(), entity);
    BeanUtils.copyProperties(entity, resource);
    updateResources(entity, resource);
    return resource;
  }

  private void updateResources(UserEntity entity, User resource) {
    resource.setId(entity.getId().toString());
    resource.add(
        linkTo(methodOn(CustomerController.class).getCustomerById(entity.getId().toString()))
            .withSelfRel());
    resource.add(
        linkTo(methodOn(CustomerController.class).getAllCustomers()).withRel("customers"));
    resource
        .add(
            linkTo(methodOn(CustomerController.class)
                .getAddressesByCustomerId(entity.getId().toString())).withRel("self_addresses"));
  }

  public List<User> toListModel(Iterable<UserEntity> entities) {
    if (Objects.isNull(entities)) {
      return Collections.emptyList();
    }

    return StreamSupport.stream(entities.spliterator(), false).map(e -> toModel(e))
        .collect(toList());
  }

}
