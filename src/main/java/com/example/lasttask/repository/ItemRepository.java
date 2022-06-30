package com.example.lasttask.repository;

import com.example.lasttask.model.entity.item.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {


    List<ItemEntity> findByCollectionId(Long collectionId);

    @Query("select itm from items itm")
    List<ItemEntity> findTopByCreateDate();

    @Query(value = "delete from item_tag it " +
            "where it.item_id = ?1 ", nativeQuery = true)
    void deleteAllByItemId(Long itemId);

}
