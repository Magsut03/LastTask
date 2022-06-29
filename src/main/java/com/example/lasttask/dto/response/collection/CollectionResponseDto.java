package com.example.lasttask.dto.response.collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CollectionResponseDto {
    private Long id;
    private String name;
    private String topic;
    private String description;
    private String imageUrl;
}
