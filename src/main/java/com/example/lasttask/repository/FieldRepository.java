package com.example.lasttask.repository;

import com.example.lasttask.model.entity.collection.FieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface FieldRepository extends JpaRepository<FieldEntity, Long> {

    List<FieldEntity> findByCollectionId(Long collectionId);

    Optional<FieldEntity> findByName(String name);

    @Modifying
    @Transactional
    @Query(value = "delete from field f " +
            "where f.collection_id = ?1 ", nativeQuery = true)
    void deleteAllByCollectionId(Long collectionId);
}

