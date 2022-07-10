package com.example.lasttask.dto.response;

import com.example.lasttask.model.entity.CommentEntity;
import com.example.lasttask.model.entity.collection.CollectionEntity;
import com.example.lasttask.model.entity.item.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponseDto {
    List<CollectionEntity> collectionEntityList;
    List<ItemEntity> itemEntityList;
    List<CommentEntity> commentEntityList;
}
