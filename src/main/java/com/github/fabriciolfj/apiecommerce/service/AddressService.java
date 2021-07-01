package com.github.fabriciolfj.apiecommerce.service;

import com.github.fabriciolfj.apiecommerce.entity.AddressEntity;
import com.github.fabriciolfj.apiecommerce.model.AddAddressReq;

import java.util.Optional;

public interface AddressService {
  Optional<AddressEntity> createAddress(final AddAddressReq addAddressReq);
  void deleteAddressesById(String id);
  Optional<AddressEntity> getAddressesById(String id);
  Iterable<AddressEntity> getAllAddresses();
}
