package com.example.lasttask.repository;

import com.example.lasttask.model.entity.collection.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TopicRepository extends JpaRepository<TopicEntity, Long> {
    Optional<TopicEntity> findByName(String name);
}
