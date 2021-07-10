package com.github.fabriciolfj.apiecommerce.service;

import com.github.fabriciolfj.apiecommerce.entity.AddressEntity;
import com.github.fabriciolfj.apiecommerce.entity.CardEntity;
import com.github.fabriciolfj.apiecommerce.entity.UserEntity;
import com.github.fabriciolfj.apiecommerce.model.RefreshToken;
import com.github.fabriciolfj.apiecommerce.model.SignedInUser;
import com.github.fabriciolfj.apiecommerce.model.User;

import java.util.Optional;

public interface UserService {
  void deleteCustomerById(String id);
  Optional<Iterable<AddressEntity>> getAddressesByCustomerId(String id);
  Iterable<UserEntity> getAllCustomers();
  Optional<CardEntity> getCardByCustomerId(String id);
  Optional<UserEntity> getCustomerById(String id);
  UserEntity findUserByUserName(final String username);
  SignedInUser createUser(final User user);
  SignedInUser getSignedInUser(final UserEntity userEntity);
  Optional<SignedInUser> getAccessToken(final RefreshToken refreshToken);
  void removeRefreshToken(final RefreshToken refreshToken);
}
