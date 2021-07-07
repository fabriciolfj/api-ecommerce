package com.github.fabriciolfj.apiecommerce.service.impl;

import com.github.fabriciolfj.apiecommerce.entity.ProductEntity;
import com.github.fabriciolfj.apiecommerce.entity.TagEntity;
import com.github.fabriciolfj.apiecommerce.repository.ProductRepository;
import com.github.fabriciolfj.apiecommerce.repository.TagRepository;
import com.github.fabriciolfj.apiecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

  private final TagRepository tagRepo;
  private final ProductRepository repository;

  private BiFunction<ProductEntity, List<TagEntity>, ProductEntity> productTagBiFun = (p, t) -> p.setTags(t);

  @Override
  public Flux<ProductEntity> getAllProducts() {
    return repository.findAll()
            .flatMap(products ->
                    Mono.just(products)
                            .zipWith(tagRepo.findTagsByProductId(products.getId().toString()).collectList())
                            .map(t -> t.getT1().setTags(t.getT2()))
            );
  }

  @Override
  public Mono<ProductEntity> getProduct(String id) {
    Mono<ProductEntity> product = repository.findById(UUID.fromString(id))
            .subscribeOn(Schedulers.boundedElastic());
    Flux<TagEntity> tags = tagRepo.findTagsByProductId(id).subscribeOn(Schedulers.boundedElastic());
    return Mono.zip(product, tags.collectList(), productTagBiFun);
  }
}
