package com.github.fabriciolfj.apiecommerce.service.impl;

import com.github.fabriciolfj.apiecommerce.entity.ShipmentEntity;
import com.github.fabriciolfj.apiecommerce.repository.ShipmentRepository;
import com.github.fabriciolfj.apiecommerce.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository repository;

    @Override
    public Flux<ShipmentEntity> getShipmentByOrderId(@Min(value = 1L, message = "Invalid shipment ID.") String id) {
        return repository.getShipmentByOrderId(id);
    }
}
