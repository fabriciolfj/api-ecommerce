package com.github.fabriciolfj.apiecommerce.service.impl;

import com.github.fabriciolfj.apiecommerce.entity.ProductEntity;
import com.github.fabriciolfj.apiecommerce.repository.ProductRepository;
import com.github.fabriciolfj.apiecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

  private final ProductRepository repository;

  @Override
  public Iterable<ProductEntity> getAllProducts() {
    return repository.findAll();
  }

  @Override
  public Optional<ProductEntity> getProduct(String id) {
    return repository
        .findById(UUID.fromString(id));
  }
}
