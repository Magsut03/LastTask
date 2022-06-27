package com.example.lasttask.dto.request.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListItemFieldRequestDto {
    private List<ItemFieldRequestDto> fieldList;
    private List<TagRequestDto> tagList;
}
