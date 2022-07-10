package com.example.lasttask.repository;


import com.example.lasttask.model.entity.collection.CollectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.crypto.spec.OAEPParameterSpec;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface CollectionRepository extends JpaRepository<CollectionEntity, Long> {

    @Query("select c from collection c " +
            "order by c.numberOfItem asc ")
    List<CollectionEntity> findTop();

    List<CollectionEntity> findAllByUserId(Long userId);


    List<CollectionEntity> findByTopicId(Long topicId);

    @Query(value = "select * from collection c where c.doc @@ plainto_tsquery(:text)", nativeQuery = true)
    List<CollectionEntity> search(String text);

}