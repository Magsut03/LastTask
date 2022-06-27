package com.example.lasttask.repository;

import com.example.lasttask.model.entity.item.ItemFieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ItemFieldRepository extends JpaRepository<ItemFieldEntity, Long> {
    @Query("select itm from item_fields itm where itm.fieldEntity.id = ?1")
    Optional<ItemFieldEntity> findByFieldEntityId(Long id);

    @Query(value = "delete from item_fields where item_fields.field_entity.id = ?1", nativeQuery = true)
    void deleteByFieldEntityId(Long fieldEntityId);


    void deleteAllByItemId(Long itemId);

}
