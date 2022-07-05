package com.example.lasttask.service.collection;

import com.example.lasttask.dto.request.collection.FieldRequestDto;
import com.example.lasttask.dto.response.ApiResponse;
import com.example.lasttask.model.entity.UserEntity;
import com.example.lasttask.model.entity.collection.CollectionEntity;
import com.example.lasttask.model.entity.collection.FieldEntity;
import com.example.lasttask.repository.FieldRepository;
import com.example.lasttask.repository.ItemFieldRepository;
import com.example.lasttask.service.CheckService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Service
public class FieldService {

    private final ItemFieldRepository itemFieldRepository;
    private final FieldRepository fieldRepository;
    private final ModelMapper modelMapper;
    private final CheckService checkService;



    public ApiResponse add(Long userId, Long collectionId, FieldRequestDto fieldRequestDto){
        UserEntity user = checkService.checkUserForExist(userId);
        CollectionEntity collection = checkService.checkCollectionForExist(collectionId);
        checkService.checkPermission(userId, user, collection, "add");
        checkService.checkFieldForNotExist(fieldRequestDto.getName());

        FieldEntity fieldEntity = modelMapper.map(fieldRequestDto, FieldEntity.class);
        fieldEntity.setCreateDate(LocalDateTime.now());
        fieldEntity.setCollection(collection);
        fieldRepository.save(fieldEntity);
        return new ApiResponse(1, "success", null);
    }


    public ApiResponse delete(Long userId, Long collectionId, Long fieldId) {
        UserEntity user = checkService.checkUserForExist(userId);
        CollectionEntity collection = checkService.checkCollectionForExist(collectionId);
        checkService.checkPermission(userId, user, collection, "delete");
        checkService.checkFieldForExist(fieldId);

        itemFieldRepository.deleteAllByFieldEntityId(fieldId);
        fieldRepository.deleteById(fieldId);
        return new ApiResponse(1, "success", null);
    }


    public ApiResponse getAll(Long userId, Long collectionId){
        UserEntity user = checkService.checkUserForExist(userId);
        CollectionEntity collection = checkService.checkCollectionForExist(collectionId);
        checkService.checkPermission(userId, user, collection, "get");
        return new ApiResponse(1, "success", fieldRepository.findByCollectionId(collectionId));
    }
}
