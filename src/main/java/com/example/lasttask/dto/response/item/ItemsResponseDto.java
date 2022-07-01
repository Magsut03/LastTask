package com.example.lasttask.dto.response.item;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemsResponseDto {
    private Long id;
    private String name;
    private int likes;
}
