package com.github.fabriciolfj.apiecommerce.service.impl;

import com.github.fabriciolfj.apiecommerce.entity.AddressEntity;
import com.github.fabriciolfj.apiecommerce.facade.converters.AddressConverter;
import com.github.fabriciolfj.apiecommerce.model.AddAddressReq;
import com.github.fabriciolfj.apiecommerce.repository.AddressRepository;
import com.github.fabriciolfj.apiecommerce.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository repository;
    private final AddressConverter addressConverter;

    @Override
    public Optional<AddressEntity> createAddress(final AddAddressReq addAddressReq) {
        return Optional.of(repository.save(addressConverter.toEntity(addAddressReq)));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAddressesById(String id) {
        repository.deleteById(UUID.fromString(id));
    }

    @Override
    public Optional<AddressEntity> getAddressesById(String id) {
        return repository.findById(UUID.fromString(id));
    }

    @Override
    public Iterable<AddressEntity> getAllAddresses() {
        return repository.findAll();
    }
}
