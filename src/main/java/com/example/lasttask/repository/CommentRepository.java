package com.example.lasttask.repository;

import com.example.lasttask.model.entity.CommentEntity;
import com.example.lasttask.model.entity.collection.CollectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Modifying
    @Transactional
    @Query(value = "delete from comment c " +
            "where c.item_id = ?1", nativeQuery = true)
    void deleteAllByItemId(Long itemId);

    List<CommentEntity> findAllByItem_Id(Long itemId);

    @Query(value = "select * from comment c where c.doc @@ plainto_tsquery(:text)", nativeQuery = true)
    List<CommentEntity> search(String text);


}
