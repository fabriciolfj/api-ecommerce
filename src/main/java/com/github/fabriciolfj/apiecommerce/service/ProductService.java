package com.github.fabriciolfj.apiecommerce.service;

import com.github.fabriciolfj.apiecommerce.entity.ProductEntity;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Validated
public interface ProductService {
  @NotNull Iterable<ProductEntity> getAllProducts();
  Optional<ProductEntity> getProduct(@Min(value = 1L, message = "Invalid product ID.") String id);
}