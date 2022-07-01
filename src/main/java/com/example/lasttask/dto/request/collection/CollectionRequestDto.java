package com.example.lasttask.dto.request.collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CollectionRequestDto {
    private String topic;
    private String name;
    private String description;
    private String imageUrl;
}
