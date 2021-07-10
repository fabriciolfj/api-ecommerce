package com.github.fabriciolfj.apiecommerce.service.impl;

import com.github.fabriciolfj.apiecommerce.entity.AddressEntity;
import com.github.fabriciolfj.apiecommerce.entity.CardEntity;
import com.github.fabriciolfj.apiecommerce.entity.UserEntity;
import com.github.fabriciolfj.apiecommerce.entity.UserTokenEntity;
import com.github.fabriciolfj.apiecommerce.exceptions.GenericAlreadyExistsException;
import com.github.fabriciolfj.apiecommerce.model.RefreshToken;
import com.github.fabriciolfj.apiecommerce.model.SignedInUser;
import com.github.fabriciolfj.apiecommerce.model.User;
import com.github.fabriciolfj.apiecommerce.repository.UserRepository;
import com.github.fabriciolfj.apiecommerce.repository.UserTokenRepository;
import com.github.fabriciolfj.apiecommerce.security.JwtManager;
import com.github.fabriciolfj.apiecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository repository;
  private final PasswordEncoder bCryptPasswordEncoder;
  private final JwtManager tokenManager;
  private final UserTokenRepository userTokenRepository;

  @Override
  public void deleteCustomerById(String id) {
    repository.deleteById(UUID.fromString(id));
  }

  @Override
  public Optional<Iterable<AddressEntity>> getAddressesByCustomerId(String id) {
    return repository.findById(UUID.fromString(id)).map(UserEntity::getAddresses);
  }

  @Override
  public Iterable<UserEntity> getAllCustomers() {
    return repository.findAll();
  }

  @Override
  public Optional<CardEntity> getCardByCustomerId(String id) {
    return of(repository.findById(UUID.fromString(id)).map(UserEntity::getCard).get().get(0));
  }

  @Override
  public Optional<UserEntity> getCustomerById(String id) {
    return repository.findById(UUID.fromString(id));
  }

  @Override
  public UserEntity findUserByUserName(final String username) {
    return ofNullable(username)
            .filter(Strings::isNotBlank)
            .map(String::trim)
            .flatMap(repository::findByUsername)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("User not found, %s", username)));
  }

  @Override
  public SignedInUser createUser(User user) {
    return of(repository.findByUsernameOrEmail(user.getUsername(), user.getEmail()))
            .filter(result -> result == 0)
            .map(value -> toEntity(user))
            .map(this::createSignedUserWithRefreshToken)
            .orElseThrow(() -> new GenericAlreadyExistsException("User different username and email."));
  }

  @Override
  public SignedInUser getSignedInUser(final UserEntity userEntity) {
    userTokenRepository.deleteByUserId(userEntity.getId());
    return createSignedUserWithRefreshToken(userEntity);
  }

  @Override
  public Optional<SignedInUser> getAccessToken(final RefreshToken refreshToken) {
    return userTokenRepository
            .findByRefreshToken(refreshToken.getRefreshToken())
            .map(u -> of(createSignedInUser(u.getUser())
            .refreshToken(refreshToken.getRefreshToken())))
            .orElseThrow(() -> new InvalidBearerTokenException("Invalid token"));
  }

  @Override
  public void removeRefreshToken(RefreshToken refreshToken) {
    userTokenRepository.findByRefreshToken(refreshToken.getRefreshToken())
            .ifPresentOrElse(userTokenRepository::delete, () -> {
              throw new UsernameNotFoundException("token not found");
    });
  }

  private UserEntity toEntity(User user) {
    UserEntity userEntity = new UserEntity();
    BeanUtils.copyProperties(user, userEntity);
    userEntity.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    return userEntity;
  }

  private SignedInUser createSignedUserWithRefreshToken(UserEntity userEntity) {
    return createSignedInUser(userEntity).refreshToken(createRefreshToken(userEntity));
  }

  private SignedInUser createSignedInUser(UserEntity userEntity) {
    String token = tokenManager.create(org.springframework.security.core.userdetails.User.builder()
            .username(userEntity.getUsername())
            .password(userEntity.getPassword())
            .authorities(Objects.nonNull(userEntity.getRole()) ? userEntity.getRole().name() : "")
            .build());
    return new SignedInUser().username(userEntity.getUsername()).accessToken(token);
  }

  private String createRefreshToken(UserEntity user) {
    String token = RandomHolder.randomKey(128);
    userTokenRepository.save(new UserTokenEntity().setRefreshToken(token).setUser(user));
    return token;
  }

  private static class RandomHolder {
    static final Random random = new SecureRandom();
    public static String randomKey(int length) {
      return String.format("%"+length+"s", new BigInteger(length*5/*base 32,2^5*/, random)
              .toString(32)).replace('\u0020', '0');
    }
  }
}