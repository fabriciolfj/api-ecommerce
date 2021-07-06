package com.github.fabriciolfj.apiecommerce.service.impl;

import com.github.fabriciolfj.apiecommerce.entity.AddressEntity;
import com.github.fabriciolfj.apiecommerce.facade.converters.AddressConverter;
import com.github.fabriciolfj.apiecommerce.model.AddAddressReq;
import com.github.fabriciolfj.apiecommerce.repository.AddressRepository;
import com.github.fabriciolfj.apiecommerce.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository repository;
    private final AddressConverter addressConverter;

    @Override
    public Mono<AddressEntity> createAddress(final Mono<AddAddressReq> addAddressReq) {
        return addAddressReq.map(addressConverter::toEntity)
                .flatMap(e -> Mono.just(e));
    }

    @Override
    public Mono<Void> deleteAddressesById(final String id) {
        return repository.deleteById(UUID.fromString(id));
    }

    @Override
    public Mono<Void> deleteAddressesById(final UUID id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono<AddressEntity> getAddressesById(final String id) {
        return repository.findById(UUID.fromString(id));
    }

    @Override
    public Flux<AddressEntity> getAllAddresses() {
        return repository.findAll();
    }
}
