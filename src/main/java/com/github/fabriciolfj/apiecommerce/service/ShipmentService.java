package com.github.fabriciolfj.apiecommerce.service;

import com.github.fabriciolfj.apiecommerce.entity.ShipmentEntity;
import reactor.core.publisher.Flux;

import javax.validation.constraints.Min;

public interface ShipmentService {

  Flux<ShipmentEntity> getShipmentByOrderId(@Min(value = 1L, message = "Invalid product ID.")  String id);
}
