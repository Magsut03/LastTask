package com.example.lasttask.repository;


import com.example.lasttask.model.entity.collection.CollectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CollectionRepository extends JpaRepository<CollectionEntity, Long> {

    @Query("select c from collection c " +
            "order by c.numberOfItem asc ")
    List<CollectionEntity> findTop();

}