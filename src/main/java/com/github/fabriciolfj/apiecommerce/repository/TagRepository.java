package com.github.fabriciolfj.apiecommerce.repository;

import com.github.fabriciolfj.apiecommerce.entity.TagEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;


public interface TagRepository extends CrudRepository<TagEntity, UUID> {
}
