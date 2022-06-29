package com.example.lasttask.dto.response;

import com.example.lasttask.dto.response.collection.CollectionResponseDto;
import com.example.lasttask.model.entity.collection.CollectionEntity;
import com.example.lasttask.model.entity.item.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MainPageDataResponseDto {

    private List<CollectionResponseDto> collectionList;
    private List<ItemResponseDto> itemList;

}
