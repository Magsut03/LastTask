package com.example.lasttask.repository;

import com.example.lasttask.model.entity.item.ItemFieldEntity;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ItemFieldRepository extends JpaRepository<ItemFieldEntity, Long> {

    Optional<ItemFieldEntity> findByFieldEntityIdAndAndItem_Id(Long fieldEntityId, Long itemId);

    @Modifying
    @Transactional
    @Query(value = "delete from item_fields itm " +
            "where itm.field_entity_id = ?1", nativeQuery = true)
    void deleteAllByFieldEntityId(Long fieldId);

    @Modifying
    @Transactional
    @Query(value = "delete from item_fields itm " +
            "where itm.item_id = ?1", nativeQuery = true)
    void deleteAllByItemId(Long itemId);
}
