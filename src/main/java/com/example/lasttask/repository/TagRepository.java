package com.example.lasttask.repository;

import com.example.lasttask.dto.request.item.TagRequestDto;
import com.example.lasttask.model.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<TagEntity, Long>{

    @Query(value = "select t from tag t " +
            "inner join tag_item ti on ti.tag_id = t.id " +
            "where ti.item_id = ?1 " +
            "order by t.name asc", nativeQuery = true)
    List<TagEntity> findByItemId(Long itemId);



    Optional<TagEntity> findByName(String name);

}
