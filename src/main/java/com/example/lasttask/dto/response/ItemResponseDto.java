package com.example.lasttask.dto.response;

import com.example.lasttask.model.entity.TagEntity;
import com.example.lasttask.model.entity.collection.FieldEntity;
import com.example.lasttask.model.entity.item.ItemEntity;
import com.example.lasttask.model.entity.item.ItemFieldEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponseDto {
    private ItemEntity item;
    private List<TagEntity> tags;
    private List<ItemFieldEntity> itemFields;
    private List<FieldEntity> fields;
}
