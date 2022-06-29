package com.example.lasttask.dto.response;

import com.example.lasttask.dto.response.collection.CollectionResponseDto;
import com.example.lasttask.dto.response.item.ItemResponseDto;
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
