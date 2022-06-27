package com.example.lasttask.repository;

import com.example.lasttask.model.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Query(value = "delete from comment where comment.item_id = ?1", nativeQuery = true)
    void deleteByItemId(Long itemId);

}
