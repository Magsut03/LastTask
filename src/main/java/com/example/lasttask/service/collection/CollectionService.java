package com.example.lasttask.service.collection;
import com.example.lasttask.dto.request.collection.CollectionRequestDto;
import com.example.lasttask.dto.request.collection.FieldRequestDto;
import com.example.lasttask.dto.request.collection.ListFieldRequestDto;
import com.example.lasttask.dto.response.ApiResponse;
import com.example.lasttask.dto.response.MainPageDataResponseDto;
import com.example.lasttask.exception.NotFoundException;
import com.example.lasttask.model.entity.UserEntity;
import com.example.lasttask.model.entity.collection.CollectionEntity;
import com.example.lasttask.model.entity.collection.FieldEntity;
import com.example.lasttask.model.entity.item.ItemEntity;
import com.example.lasttask.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final ItemFieldRepository itemFieldRepository;
    private final CommentRepository commentRepository;
    private final FieldRepository fieldRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    public ApiResponse add(Long userId, CollectionRequestDto collectionRequestDto){
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()){
            throw new NotFoundException("User not found with this Id: " + userId);
        }
        UserEntity user = optionalUser.get();
        CollectionEntity collection = modelMapper.map(collectionRequestDto, CollectionEntity.class);
        collection.setUser(user);
        collectionRepository.save(collection);
        return new ApiResponse(1, "success", null);
    }


    public ApiResponse edit(Long collectionId, CollectionRequestDto collectionRequestDto){
        Optional<CollectionEntity> optionalCollection = collectionRepository.findById(collectionId);
        if (!optionalCollection.isPresent()){
            throw new NotFoundException("Collection not found with this Id: " + collectionId);
        }
        CollectionEntity collection = optionalCollection.get();
        collection.setDescription(collectionRequestDto.getDescription());
        collection.setName(collectionRequestDto.getName());
        collection.setTopic(collectionRequestDto.getTopic());
        collection.setImageUrl(collectionRequestDto.getImageUrl());
        collectionRepository.save(collection);
        return new ApiResponse(1, "success", null);
    }


    public ApiResponse delete(Long collectionId) {
        Optional<CollectionEntity> optionalCollection = collectionRepository.findById(collectionId);
        if (!optionalCollection.isPresent()){
            throw new NotFoundException("Collection not found with this Id: " + collectionId);
        }
        List<ItemEntity> itemEntityList = itemRepository.findByCollectionId(collectionId);
        List<FieldEntity> fieldEntityList = fieldRepository.findByCollectionId(collectionId);
        fieldEntityList.forEach(fieldEntity -> {
            itemFieldRepository.deleteByFieldEntityId(fieldEntity.getId());
            fieldRepository.deleteById(fieldEntity.getId());
        });
        itemEntityList.forEach(itemEntity -> {
            commentRepository.deleteByItemId(itemEntity.getId());
            itemRepository.deleteById(itemEntity.getId());
        });
        collectionRepository.deleteById(collectionId);
        return new ApiResponse(1, "success", null);
    }



    public ApiResponse getAll(){
        List<CollectionEntity> allCollections = collectionRepository.findAll();
        return new ApiResponse(1, "Successfully!", allCollections);
    }





     /////   FIELD   /////

    public ApiResponse addField(Long collectionId, ListFieldRequestDto listFieldRequestDto){
        Optional<CollectionEntity> optionalCollection = collectionRepository.findById(collectionId);
        if (!optionalCollection.isPresent()){
            throw new NotFoundException("collection not found with this Id: " + collectionId);
        }
        CollectionEntity collection = optionalCollection.get();
        List<FieldRequestDto> fieldRequestDtos = listFieldRequestDto.getList();
        fieldRequestDtos.forEach(fieldRequestDto -> {
            FieldEntity fieldEntity = modelMapper.map(fieldRequestDto, FieldEntity.class);
            fieldEntity.setCollection(collection);
            fieldEntity.setCreateDate(LocalDateTime.now());
            fieldRepository.save(fieldEntity);
        });
        return new ApiResponse(1, "success", null);
    }


    public ApiResponse getFields(Long collectionId){
        return new ApiResponse(1, "success", fieldRepository.findByCollectionId(collectionId));
    }


    //////   MAIN PAGE  ////////

    public ApiResponse getMainPageData(){
        List<CollectionEntity> collectionEntityList = collectionRepository.findTop();
        List<CollectionEntity> collectionEntities = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (i < collectionEntityList.size()){
                collectionEntities.add(collectionEntityList.get(i));
            }
        }

        List<ItemEntity> itemEntityList = itemRepository.findTopByCreateDate();
        List<ItemEntity> itemEntities = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (i < itemEntityList.size()){
                itemEntities.add(itemEntityList.get(i));
            }
        }

        return new ApiResponse(1, "success", new MainPageDataResponseDto(collectionEntities, itemEntities));
    }

}
