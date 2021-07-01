package com.github.fabriciolfj.apiecommerce.service.impl;

import com.github.fabriciolfj.apiecommerce.entity.CardEntity;
import com.github.fabriciolfj.apiecommerce.entity.UserEntity;
import com.github.fabriciolfj.apiecommerce.model.AddCardReq;
import com.github.fabriciolfj.apiecommerce.repository.CardRepository;
import com.github.fabriciolfj.apiecommerce.repository.UserRepository;
import com.github.fabriciolfj.apiecommerce.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

  private final CardRepository repository;
  private final UserRepository userRepo;

  @Override
  public void deleteCardById(String id) {
    repository.deleteById(UUID.fromString(id));
  }

  @Override
  public Iterable<CardEntity> getAllCards() {
    return repository.findAll();
  }

  @Override
  public Optional<CardEntity> getCardById(String id) {
    return repository.findById(UUID.fromString(id));
  }

  @Override
  public Optional<CardEntity> registerCard(@Valid final AddCardReq addCardReq) {
    return Optional.of(repository.save(toEntity(addCardReq)));
  }

  private CardEntity toEntity(final AddCardReq m) {
    final CardEntity e = new CardEntity();
    final Optional<UserEntity> user = userRepo.findById(UUID.fromString(m.getUserId()));
    user.ifPresent(u -> e.setUser(u));
    return e.setNumber(m.getCardNumber()).setCvv(m.getCvv())
        .setExpires(m.getExpires());
  }
}
