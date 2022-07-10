package com.example.lasttask.service;

import com.example.lasttask.dto.response.ApiResponse;
import com.example.lasttask.dto.response.SearchResponseDto;
import com.example.lasttask.model.entity.CommentEntity;
import com.example.lasttask.model.entity.collection.CollectionEntity;
import com.example.lasttask.model.entity.item.ItemEntity;
import com.example.lasttask.repository.CollectionRepository;
import com.example.lasttask.repository.CommentRepository;
import com.example.lasttask.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final CollectionRepository collectionRepository;
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;


    public ApiResponse fullTextSearch(String text){
        SearchResponseDto searchResponseDto = new SearchResponseDto();
        searchResponseDto.setCollectionEntityList(searchCollection(text));
        searchResponseDto.setItemEntityList(searchItem(text));
        searchResponseDto.setCommentEntityList(searchComment(text));
        return new ApiResponse(1, "success", searchResponseDto);
    }

    public List<CollectionEntity> searchCollection(String text){
        return collectionRepository.search(text);
    }

    public List<ItemEntity> searchItem(String text){
        return itemRepository.search(text);
    }
    public List<CommentEntity> searchComment(String text){
        return commentRepository.search(text);
    }

}
