package com.example.lasttask.repository;

import com.example.lasttask.dto.request.item.TagRequestDto;
import com.example.lasttask.model.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<TagEntity, Long>{

    @Query(value = "select t from tag t " +
            "inner join tag_item ti on ti.tag_id = t.id " +
            "where ti.item_id = ?1 " +
            "order by t.name asc", nativeQuery = true)
    List<TagEntity> findByItemId(Long itemId);


    @Modifying
    @Transactional
    @Query(value = "delete from item_tag it " +
            "where it.item_id = ?1 ", nativeQuery = true)
    void deleteAllByItemId(Long itemId);



    Optional<TagEntity> findByName(String name);

    @Modifying
    @Transactional
    @Query(value = "delete from item_tag it " +
            "where it.tag_id = ?1 ", nativeQuery = true)
    void deleteAllByTagId(Long tagId);
}
