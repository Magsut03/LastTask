package com.example.lasttask.repository;


import com.example.lasttask.model.entity.collection.CollectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface CollectionRepository extends JpaRepository<CollectionEntity, Long> {

    @Query("select c from collection c " +
            "order by c.numberOfItem asc ")
    List<CollectionEntity> findTop();

    @Modifying
    @Transactional
    @Query(value = "select c from collection c " +
            "where c.user_id = ?1", nativeQuery = true)
    List<CollectionEntity> findAllUserId(Long userId);

    @Modifying
    @Transactional
    @Query(value = "delete from collection_field cf " +
            "where cf.field_id = ?1", nativeQuery = true)
    void deleteAllFields(Long fieldId);
}