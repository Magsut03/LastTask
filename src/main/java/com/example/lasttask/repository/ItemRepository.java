package com.example.lasttask.repository;

import com.example.lasttask.model.entity.collection.CollectionEntity;
import com.example.lasttask.model.entity.item.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {


    List<ItemEntity> findByCollectionId(Long collectionId);

    @Query("select itm from items itm")
    List<ItemEntity> findTopByCreateDate();

    @Query(value = "select * from items i where i.doc @@ plainto_tsquery(:text)", nativeQuery = true)
    List<ItemEntity> search(String text);



}
