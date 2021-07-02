package com.github.fabriciolfj.apiecommerce.hateoas;

import com.github.fabriciolfj.apiecommerce.controllers.PaymentController;
import com.github.fabriciolfj.apiecommerce.entity.PaymentEntity;
import com.github.fabriciolfj.apiecommerce.model.Payment;
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
public class PaymentRepresentationModelAssembler extends
    RepresentationModelAssemblerSupport<PaymentEntity, Payment> {

  public PaymentRepresentationModelAssembler() {
    super(PaymentController.class, Payment.class);
  }

  @Override
  public Payment toModel(PaymentEntity entity) {
    Payment resource = createModelWithId(entity.getId(), entity);
    BeanUtils.copyProperties(entity, resource);
    resource.setId(entity.getId().toString());
    resource.add(linkTo(
        methodOn(PaymentController.class).getOrdersPaymentAuthorization(entity.getId().toString()))
        .withSelfRel());
    return resource;
  }

  public List<Payment> toListModel(Iterable<PaymentEntity> entities) {
    if (Objects.isNull(entities)) {
      return Collections.emptyList();
    }
    return StreamSupport.stream(entities.spliterator(), false).map(e -> toModel(e))
        .collect(toList());
  }

}
