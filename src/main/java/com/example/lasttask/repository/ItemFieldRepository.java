package com.example.lasttask.repository;

import com.example.lasttask.model.entity.item.ItemFieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ItemFieldRepository extends JpaRepository<ItemFieldEntity, Long> {

    Optional<ItemFieldEntity> findByFieldEntityId(Long id);

    void deleteAllByFieldEntityId(Long fieldEntityId);


    @Query(value = "delete from item_fields it " +
            "where it.item_id = ?1", nativeQuery = true)
    void deleteAllByItemId(Long itemId);

}
