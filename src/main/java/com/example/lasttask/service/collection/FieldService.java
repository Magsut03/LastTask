package com.example.lasttask.service.collection;

import com.example.lasttask.dto.request.collection.FieldRequestDto;
import com.example.lasttask.dto.response.ApiResponse;
import com.example.lasttask.exception.BadRequestException;
import com.example.lasttask.exception.NotFoundException;
import com.example.lasttask.model.entity.UserEntity;
import com.example.lasttask.model.entity.collection.CollectionEntity;
import com.example.lasttask.model.entity.collection.FieldEntity;
import com.example.lasttask.model.entity.item.ItemFieldEntity;
import com.example.lasttask.model.enums.RoleEnum;
import com.example.lasttask.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.lasttask.model.enums.RoleEnum.ROLE_ADMIN;

@RequiredArgsConstructor
@Service
public class FieldService {

    private final CollectionRepository collectionRepository;
    private final ItemFieldRepository itemFieldRepository;
    private final FieldRepository fieldRepository;
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

    private void checkFieldForNotExist(String name){
        Optional<FieldEntity> optionalField = fieldRepository.findByName(name);
        if (optionalField.isPresent()){
            throw new NotFoundException("Field is already exist with this Name: " + name);
        }
    }

    private FieldEntity checkFieldForExist(Long id){
        Optional<FieldEntity> optionalField = fieldRepository.findById(id);
        if (!optionalField.isPresent()){
            throw new NotFoundException("Field not found with this Id: " + id);
        }
        return optionalField.get();
    }

    private void checkPermission(Long userId,UserEntity user, CollectionEntity collection, String custom){
        if (!(collection.getUser().getId().equals(userId) || user.getRole().equals(ROLE_ADMIN))){
            throw new BadRequestException("you don't have permission to " + custom + " this collection!");
        }
    }


    public ApiResponse add(Long userId, Long collectionId, FieldRequestDto fieldRequestDto){
        UserEntity user = checkUserForExist(userId);
        CollectionEntity collection = checkCollectionForExist(collectionId);
        checkPermission(userId, user, collection, "add");
        checkFieldForNotExist(fieldRequestDto.getName());

        FieldEntity fieldEntity = modelMapper.map(fieldRequestDto, FieldEntity.class);
        fieldEntity.setCreateDate(LocalDateTime.now());
        fieldEntity.setCollection(collection);
        fieldRepository.save(fieldEntity);
        return new ApiResponse(1, "success", null);
    }


    public ApiResponse delete(Long userId, Long collectionId, Long fieldId) {
        UserEntity user = checkUserForExist(userId);
        CollectionEntity collection = checkCollectionForExist(collectionId);
        checkPermission(userId, user, collection, "delete");
        checkFieldForExist(fieldId);

        itemFieldRepository.deleteAllByFieldEntityId(fieldId);
        fieldRepository.deleteById(fieldId);
        return new ApiResponse(1, "success", null);
    }


    public ApiResponse getAll(Long userId, Long collectionId){
        UserEntity user = checkUserForExist(userId);
        CollectionEntity collection = checkCollectionForExist(collectionId);
        checkPermission(userId, user, collection, "get");
        return new ApiResponse(1, "success", fieldRepository.findByCollectionId(collectionId));
    }
}
