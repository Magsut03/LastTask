package com.example.lasttask.service.collection;
import com.example.lasttask.dto.request.collection.CollectionRequestDto;
import com.example.lasttask.dto.response.ApiResponse;
import com.example.lasttask.dto.response.collection.CollectionResponseDto;
import com.example.lasttask.exception.BadRequestException;
import com.example.lasttask.exception.NotFoundException;
import com.example.lasttask.model.entity.UserEntity;
import com.example.lasttask.model.entity.collection.CollectionEntity;
import com.example.lasttask.model.entity.collection.FieldEntity;
import com.example.lasttask.model.entity.collection.TopicEntity;
import com.example.lasttask.model.entity.item.ItemEntity;
import com.example.lasttask.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.lasttask.model.enums.RoleEnum.ROLE_ADMIN;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final ItemFieldRepository itemFieldRepository;
    private final CommentRepository commentRepository;
    private final FieldRepository fieldRepository;
    private final TopicRepository topicRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    private UserEntity checkUserForExist(Long userId){
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()){
            throw new NotFoundException("User not found with this Id: " + userId);
        }
        return optionalUser.get();
    }

    private CollectionEntity checkCollectionForExist(Long collectionId){
        Optional<CollectionEntity> optionalCollection = collectionRepository.findById(collectionId);
        if (!optionalCollection.isPresent()){
            throw new NotFoundException("Collection not found with this Id: " + collectionId);
        }
        return optionalCollection.get();
    }

    private void checkPermission(Long userId, Long collectionId, String custom){
        UserEntity user = collectionRepository.findById(collectionId).get().getUser();
        UserEntity user2 = userRepository.findById(userId).get();
        if (!(user.getId().equals(userId) || user2.getRole().equals(ROLE_ADMIN))){
            throw new BadRequestException("you don't have permission to " + custom + " this collection!");
        }
    }

    private TopicEntity checkTopicForExist(String name){
        Optional<TopicEntity> optionalTopic = topicRepository.findByName(name);
        if (!optionalTopic.isPresent()){
            throw new BadRequestException("Topic not found with this Name: " + name);
        }
        return optionalTopic.get();
    }



    public ApiResponse add(Long userId, CollectionRequestDto collectionRequestDto){
        UserEntity user = checkUserForExist(userId);
        TopicEntity topic = checkTopicForExist(collectionRequestDto.getTopic());
        CollectionEntity collection = modelMapper.map(collectionRequestDto, CollectionEntity.class);
        collection.setTopic(topic);
        collection.setUser(user);
        collectionRepository.save(collection);
        return new ApiResponse(1, "success", null);
    }


    public ApiResponse edit(Long userId, Long collectionId, CollectionRequestDto collectionRequestDto){
        checkUserForExist(userId);
        CollectionEntity collection = checkCollectionForExist(collectionId);
        TopicEntity topic = checkTopicForExist(collectionRequestDto.getTopic());
        checkPermission(userId, collectionId, "edit");

        collection.setName(collectionRequestDto.getName());
        collection.setTopic(topic);
        collection.setDescription(collectionRequestDto.getDescription());
        collection.setImageUrl(collectionRequestDto.getImageUrl());
        collectionRepository.save(collection);
        return new ApiResponse(1, "success", null);
    }


    public ApiResponse delete(Long userId, Long collectionId) {
        checkUserForExist(userId);
        checkCollectionForExist(collectionId);
        checkPermission(userId, collectionId, "delete");

        List<ItemEntity> itemEntityList = itemRepository.findByCollectionId(collectionId);
        List<FieldEntity> fieldEntityList = fieldRepository.findByCollectionId(collectionId);
        fieldEntityList.forEach(fieldEntity -> {
            itemFieldRepository.deleteAllByFieldId(fieldEntity.getId());
            collectionRepository.deleteAllFields(fieldEntity.getId());
            fieldRepository.deleteById(fieldEntity.getId());
        });
        itemEntityList.forEach(itemEntity -> {
            commentRepository.deleteAllByItemId(itemEntity.getId());
            itemRepository.deleteById(itemEntity.getId());
        });
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

        checkUserForExist(userId);

        List<CollectionEntity> collectionEntityList = collectionRepository.findAllUserId(userId);
        List<CollectionResponseDto> collectionResponseDtoList = new ArrayList<>();
        collectionEntityList.forEach(collectionEntity -> {
            collectionResponseDtoList.add(modelMapper.map(collectionEntity, CollectionResponseDto.class));
        });
        return new ApiResponse(1, "success", collectionResponseDtoList);
    }

}
