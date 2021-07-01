package com.github.fabriciolfj.apiecommerce.service;

import com.github.fabriciolfj.apiecommerce.entity.AddressEntity;
import com.github.fabriciolfj.apiecommerce.entity.CardEntity;
import com.github.fabriciolfj.apiecommerce.entity.UserEntity;

import java.util.Optional;

public interface UserService {
  void deleteCustomerById(String id);
  Optional<Iterable<AddressEntity>> getAddressesByCustomerId(String id);
  Iterable<UserEntity> getAllCustomers();
  Optional<CardEntity> getCardByCustomerId(String id);
  Optional<UserEntity> getCustomerById(String id);
}
