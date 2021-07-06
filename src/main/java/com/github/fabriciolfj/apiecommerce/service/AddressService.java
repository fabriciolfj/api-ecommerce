package com.github.fabriciolfj.apiecommerce.service;

import com.github.fabriciolfj.apiecommerce.entity.AddressEntity;
import com.github.fabriciolfj.apiecommerce.model.AddAddressReq;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

public interface AddressService {

    Mono<AddressEntity> createAddress(Mono<AddAddressReq> addAddressReq);
    Mono<Void> deleteAddressesById(String id);
    Mono<Void> deleteAddressesById(UUID id);
    Mono<AddressEntity> getAddressesById(String id);
    Flux<AddressEntity> getAllAddresses();
}
