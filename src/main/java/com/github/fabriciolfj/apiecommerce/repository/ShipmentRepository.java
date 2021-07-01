package com.github.fabriciolfj.apiecommerce.repository;

import com.github.fabriciolfj.apiecommerce.entity.ShipmentEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ShipmentRepository extends CrudRepository<ShipmentEntity, UUID> {
}

