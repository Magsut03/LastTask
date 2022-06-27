package com.example.lasttask.repository;

import com.example.lasttask.model.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    Optional<CommentEntity> deleteByItemId(Long itemId);

}
