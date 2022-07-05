package com.example.lasttask.service.collection;

import com.example.lasttask.dto.request.collection.CollectionRequestDto;
import com.example.lasttask.dto.response.ApiResponse;
import com.example.lasttask.dto.response.collection.CollectionResponseDto;
import com.example.lasttask.exception.BadRequestException;
import com.example.lasttask.exception.NotFoundException;
import com.example.lasttask.model.entity.UserEntity;
import com.example.lasttask.model.entity.collection.CollectionEntity;
import com.example.lasttask.model.entity.collection.TopicEntity;
import com.example.lasttask.model.entity.item.ItemEntity;
import com.example.lasttask.repository.*;
import com.example.lasttask.service.CheckService;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;
    private final CheckService checkService;

    private static Storage storage = StorageOptions.getDefaultInstance().getService();
    @Value("${google.storage.bucket}")
    private String bucketName;

    public String saveImage(MultipartFile imageFile){
        String fileName = System.nanoTime() + imageFile.getOriginalFilename();

        try {
            BlobInfo blobInfo = storage.create(
                    BlobInfo.newBuilder(bucketName, fileName)
                            .setContentType(imageFile.getContentType())
                            .setAcl(new ArrayList<>(
                                    Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))
                            )).build(),
                    imageFile.getInputStream()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "https://storage.googleapis.com/" + bucketName + "/" + fileName;
    }

    public ApiResponse add(Long userId, CollectionRequestDto collectionRequestDto){
        UserEntity user = checkService.checkUserForExist(userId);
        TopicEntity topic = checkService.checkTopicForExist(collectionRequestDto.getTopicId());
        CollectionEntity collection = modelMapper.map(collectionRequestDto, CollectionEntity.class);
        collection.setTopic(topic);
        collection.setImageUrl(collectionRequestDto.getImageUrl());
        collection.setUser(user);
        collectionRepository.save(collection);
        return new ApiResponse(1, "success", null);
    }



    public ApiResponse edit(Long userId, Long collectionId, CollectionRequestDto collectionRequestDto){
        UserEntity user = checkService.checkUserForExist(userId);
        CollectionEntity collection = checkService.checkCollectionForExist(collectionId);
        TopicEntity topic = checkService.checkTopicForExist(collectionRequestDto.getTopicId());
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
