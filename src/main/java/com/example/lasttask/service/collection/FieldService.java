package com.example.lasttask.service.collection;

import com.example.lasttask.dto.request.collection.FieldRequestDto;
import com.example.lasttask.dto.response.ApiResponse;
import com.example.lasttask.exception.BadRequestException;
import com.example.lasttask.exception.NotFoundException;
import com.example.lasttask.model.entity.UserEntity;
import com.example.lasttask.model.entity.collection.CollectionEntity;
import com.example.lasttask.model.entity.collection.FieldEntity;
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

    private void checkPermission(Long userId, Long collectionId, String custom){
        UserEntity user = collectionRepository.findById(collectionId).get().getUser();
        UserEntity user2 = userRepository.findById(userId).get();
        if (!(user.getId().equals(userId) || user2.getRole().equals(ROLE_ADMIN))){
            throw new BadRequestException("you don't have permission to " + custom + " this collection!");
        }
    }


    public ApiResponse add(Long userId, Long collectionId, FieldRequestDto fieldRequestDto){
        checkUserForExist(userId);
        CollectionEntity collection = checkCollectionForExist(collectionId);
        checkPermission(userId, collectionId, "add");
        checkFieldForNotExist(fieldRequestDto.getName());

        FieldEntity fieldEntity = modelMapper.map(fieldRequestDto, FieldEntity.class);
        fieldEntity.setCreateDate(LocalDateTime.now());
        fieldEntity.setCollection(collection);
        List<FieldEntity> fieldEntityList = collection.getFieldEntityList();
        fieldEntityList.add(fieldEntity);
        collection.setFieldEntityList(fieldEntityList);
        fieldRepository.save(fieldEntity);
        return new ApiResponse(1, "success", null);
    }


    public ApiResponse delete(Long userId, Long collectionId, Long fieldId) {
        checkUserForExist(userId);
        checkCollectionForExist(collectionId);
        checkPermission(userId, collectionId, "delete");
        checkFieldForExist(fieldId);

        collectionRepository.deleteAllFields(fieldId);
        itemFieldRepository.deleteAllByFieldId(fieldId);
        fieldRepository.deleteById(fieldId);
        return new ApiResponse(1, "success", null);
    }

    public ApiResponse getAll(Long userId, Long collectionId){
        checkUserForExist(userId);
        checkCollectionForExist(collectionId);
        checkPermission(userId, collectionId, "get");
        return new ApiResponse(1, "success", fieldRepository.findByCollectionId(collectionId));
    }
}
