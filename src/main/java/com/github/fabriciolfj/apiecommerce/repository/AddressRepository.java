package com.github.fabriciolfj.apiecommerce.repository;

import com.github.fabriciolfj.apiecommerce.entity.AddressEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

/**
 * @author : github.com/sharmasourabh
 * @project : Chapter04 - Modern API Development with Spring and Spring Boot
 **/
public interface AddressRepository extends CrudRepository<AddressEntity, UUID> {
}

