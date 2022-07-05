package com.example.lasttask.service.collection;

import com.example.lasttask.dto.request.collection.CollectionRequestDto;
import com.example.lasttask.dto.response.ApiResponse;
import com.example.lasttask.dto.response.collection.CollectionResponseDto;
import com.example.lasttask.model.entity.UserEntity;
import com.example.lasttask.model.entity.collection.CollectionEntity;
import com.example.lasttask.model.entity.collection.TopicEntity;
import com.example.lasttask.model.entity.item.ItemEntity;
import com.example.lasttask.repository.*;
import com.example.lasttask.service.CheckService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final ItemFieldRepository itemFieldRepository;
    private final CommentRepository commentRepository;
    private final FieldRepository fieldRepository;
    private final ItemRepository itemRepository;
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;
    private final CheckService checkService;


    public ApiResponse add(Long userId, CollectionRequestDto collectionRequestDto){
        UserEntity user = checkService.checkUserForExist(userId);
        TopicEntity topic = checkService.checkTopicForExistByName(collectionRequestDto.getTopic());
        CollectionEntity collection = new CollectionEntity();
        collection = modelMapper.map(collectionRequestDto, CollectionEntity.class);
        collection.setTopic(topic);
        collection.setImageUrl(collectionRequestDto.getImageUrl());
        collection.setUser(user);
        collectionRepository.save(collection);
        return new ApiResponse(1, "success", null);
    }



    public ApiResponse edit(Long userId, Long collectionId, CollectionRequestDto collectionRequestDto){
        UserEntity user = checkService.checkUserForExist(userId);
        CollectionEntity collection = checkService.checkCollectionForExist(collectionId);
        TopicEntity topic = checkService.checkTopicForExistByName(collectionRequestDto.getTopic());
        checkService.checkPermission(userId, user, collection, "edit");

        collection.setName(collectionRequestDto.getName());
        collection.setTopic(topic);
        collection.setDescription(collectionRequestDto.getDescription());
        collection.setImageUrl(collectionRequestDto.getImageUrl());
        collectionRepository.save(collection);
        return new ApiResponse(1, "success", null);
    }


    public ApiResponse delete(Long userId, Long collectionId) {
        UserEntity user = checkService.checkUserForExist(userId);
        CollectionEntity collection = checkService.checkCollectionForExist(collectionId);
        checkService.checkPermission(userId, user, collection, "delete");

        List<ItemEntity> itemEntityList = itemRepository.findByCollectionId(collectionId);
        itemEntityList.forEach(itemEntity -> {
            Long itemId = itemEntity.getId();
            commentRepository.deleteAllByItemId(itemId);
            itemFieldRepository.deleteAllByItemId(itemId);
            tagRepository.deleteAllByTagId(itemId);
            itemRepository.deleteById(itemId);
        });
        fieldRepository.deleteAllByCollectionId(collectionId);
        collectionRepository.deleteById(collectionId);
        return new ApiResponse(1, "success", null);
    }



    public ApiResponse getAll(){
        List<CollectionEntity> allCollections = collectionRepository.findAll();
        List<CollectionResponseDto> collectionResponseDtos = new ArrayList<>();
        allCollections.forEach(collectionEntity -> {
            CollectionResponseDto collectionResponseDto =
                    modelMapper.map(collectionEntity, CollectionResponseDto.class);
            collectionResponseDto.setTopic(collectionEntity.getTopic().getName());
            collectionResponseDtos.add(collectionResponseDto);
        });
        return new ApiResponse(1, "Successfully!", collectionResponseDtos);
    }


    public ApiResponse getUserCollections(Long userId){
        checkService.checkUserForExist(userId);

        List<CollectionEntity> collectionEntityList = collectionRepository.findAllByUserId(userId);
        List<CollectionResponseDto> collectionResponseDtoList = new ArrayList<>();
        collectionEntityList.forEach(collectionEntity -> {
            CollectionResponseDto collectionResponseDto = modelMapper.map(collectionEntity, CollectionResponseDto.class);
            collectionResponseDto.setTopic(collectionEntity.getTopic().getName());
            collectionResponseDtoList.add(collectionResponseDto);

        });

        return new ApiResponse(1, "success", collectionResponseDtoList);
    }

    public ApiResponse getByTopic(Long id){
        TopicEntity topic = checkService.checkTopicForExist(id);
        List<CollectionEntity> collectionEntityList = collectionRepository.findByTopicId(topic.getId());
        List<CollectionResponseDto> collectionResponseDtoList = new ArrayList<>();
        collectionEntityList.forEach(collection -> {
            CollectionResponseDto collectionResponseDto = modelMapper.map(collection, CollectionResponseDto.class);
            collectionResponseDto.setTopic(collection.getTopic().getName());
            collectionResponseDtoList.add(collectionResponseDto);
        });
        return new ApiResponse(1, "success", collectionResponseDtoList);
    }
}
