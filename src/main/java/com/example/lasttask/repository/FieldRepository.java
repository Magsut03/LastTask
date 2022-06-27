package com.example.lasttask.repository;

import com.example.lasttask.model.entity.collection.FieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FieldRepository extends JpaRepository<FieldEntity, Long> {

    @Query("select f from field f " +
            "where f.collection.id = ?1 " +
            "order by f.createDate asc")
    List<FieldEntity> findByCollectionId(Long collectionId);
}

