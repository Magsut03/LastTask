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

    private static Storage storage = StorageOptions.getDefaultInstance().getService();
    @Value("${google.storage.bucket}")
    private String bucketName;

    private String saveImage(MultipartFile imageFile){
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

    private void checkPermission(Long userId, UserEntity user, CollectionEntity collection, String custom){
        if (!(collection.getUser().getId().equals(userId) || user.getRole().equals(ROLE_ADMIN))){
            throw new BadRequestException("you don't have permission to " + custom + " this collection!");
        }
    }


    private TopicEntity checkTopicForExist(String  name){
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
        collection.setImageUrl(saveImage(collectionRequestDto.getImageFile()));
        collection.setUser(user);
        collectionRepository.save(collection);
        return new ApiResponse(1, "success", null);
    }


    public ApiResponse edit(Long userId, Long collectionId, CollectionRequestDto collectionRequestDto){
        UserEntity user = checkUserForExist(userId);
        CollectionEntity collection = checkCollectionForExist(collectionId);
        TopicEntity topic = checkTopicForExist(collectionRequestDto.getTopic());
        checkPermission(userId, user, collection, "edit");

        collection.setName(collectionRequestDto.getName());
        collection.setTopic(topic);
        collection.setDescription(collectionRequestDto.getDescription());
        collection.setImageUrl(saveImage(collectionRequestDto.getImageFile()));
        collectionRepository.save(collection);
        return new ApiResponse(1, "success", null);
    }


    public ApiResponse delete(Long userId, Long collectionId) {
        UserEntity user = checkUserForExist(userId);
        CollectionEntity collection = checkCollectionForExist(collectionId);
        checkPermission(userId, user, collection, "delete");

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
        checkUserForExist(userId);

        List<CollectionEntity> collectionEntityList = collectionRepository.findAllByUserId(userId);
        List<CollectionResponseDto> collectionResponseDtoList = new ArrayList<>();
        collectionEntityList.forEach(collectionEntity -> {
            CollectionResponseDto collectionResponseDto = modelMapper.map(collectionEntity, CollectionResponseDto.class);
            collectionResponseDto.setTopic(collectionEntity.getTopic().getName());
            collectionResponseDtoList.add(collectionResponseDto);

        });

        return new ApiResponse(1, "success", collectionResponseDtoList);
    }

    public ApiResponse getByTopic(String name){
        TopicEntity topic = checkTopicForExist(name);
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
