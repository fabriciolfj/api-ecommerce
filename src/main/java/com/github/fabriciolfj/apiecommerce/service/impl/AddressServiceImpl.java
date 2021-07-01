package com.github.fabriciolfj.apiecommerce.service.impl;

import com.github.fabriciolfj.apiecommerce.entity.AddressEntity;
import com.github.fabriciolfj.apiecommerce.model.AddAddressReq;
import com.github.fabriciolfj.apiecommerce.repository.AddressRepository;
import com.github.fabriciolfj.apiecommerce.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

  private final AddressRepository repository;

  @Override
  public Optional<AddressEntity> createAddress(final AddAddressReq addAddressReq) {
    return Optional.of(repository.save(toEntity(addAddressReq)));
  }

  @Override
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

  private AddressEntity toEntity(final AddAddressReq model) {
    final var entity = new AddressEntity();
    return entity.setNumber(model.getNumber()).setResidency(model.getResidency())
        .setStreet(model.getStreet()).setCity(model.getCity()).setState(model.getState())
        .setCountry(model.getCountry()).setPincode(model.getPincode());
  }
}
