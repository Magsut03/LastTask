package com.example.lasttask.dto.request.collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CollectionRequestDto {
    private Long topicId;
    private String name;
    private String description;
    private String imageUrl;
}
