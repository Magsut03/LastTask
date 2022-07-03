package com.example.lasttask.service.item;

import com.example.lasttask.dto.request.item.IdRequestDto;
import com.example.lasttask.dto.request.item.ItemFieldRequestDto;
import com.example.lasttask.dto.request.item.ItemRequestDto;
import com.example.lasttask.dto.request.item.TagRequestDto;
import com.example.lasttask.dto.response.ApiResponse;
import com.example.lasttask.dto.response.item.ItemsResponseDto;
import com.example.lasttask.dto.response.item.SingleItemResponseDto;
import com.example.lasttask.exception.BadRequestException;
import com.example.lasttask.exception.NotFoundException;
import com.example.lasttask.model.entity.TagEntity;
import com.example.lasttask.model.entity.UserEntity;
import com.example.lasttask.model.entity.collection.CollectionEntity;
import com.example.lasttask.model.entity.collection.FieldEntity;
import com.example.lasttask.model.entity.collection.TopicEntity;
import com.example.lasttask.model.entity.item.ItemEntity;
import com.example.lasttask.model.entity.item.ItemFieldEntity;
import com.example.lasttask.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.lasttask.model.enums.RoleEnum.ROLE_ADMIN;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final CollectionRepository collectionRepository;
    private final ItemFieldRepository itemFieldRepository;
    private final CommentRepository commentRepository;
    private final FieldRepository fieldRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;


    private TagEntity checkTagForExist(String name){
        Optional<TagEntity> optionalTag = tagRepository.findByName(name);
        if (!optionalTag.isPresent()){
            throw new NotFoundException("Tag not found with this Name: " + name);
        }
        return optionalTag.get();
    }


    private ItemEntity checkItemForExist(Long id){
        Optional<ItemEntity> optionalItem = itemRepository.findById(id);
        if (!optionalItem.isPresent()){
            throw new NotFoundException("Item not found with this Id: " + id);
        }
        return optionalItem.get();
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
        if (!(collection.getUser()
                .getId().equals(userId) || user.getRole().equals(ROLE_ADMIN))){
            throw new BadRequestException("you don't have permission to " + custom + " this collection!");
        }
    }




    public ApiResponse add(Long userId, Long collectionId, ItemRequestDto itemRequestDto){
        UserEntity user = checkUserForExist(userId);
        CollectionEntity collection = checkCollectionForExist(collectionId);
        checkPermission(userId, user, collection, "add");

        ItemEntity item = new ItemEntity();
        item.setName((String) itemRequestDto.getFieldList().get(0).getData());
        List<TagEntity> tagList = new ArrayList<>();
        itemRequestDto.getTagList().forEach(tagRequestDto -> {
            TagEntity tag = checkTagForExist(tagRequestDto.getName());
            tagList.add(tag);
        });
        item.setTagList(tagList);
        collection.setNumberOfItem(collection.getNumberOfItem() + 1);
        item.setCollection(collection);
        collectionRepository.save(collection);
        itemRepository.save(item);

        List<FieldEntity> fieldEntities = fieldRepository.findByCollectionId(collectionId);
        for (int i = 0; i < fieldEntities.size(); i++){
            FieldEntity fieldEntity = fieldEntities.get(i);
            ItemFieldRequestDto itemFieldRequestDto = itemRequestDto.getFieldList().get(i + 1);
            ItemFieldEntity itemFieldEntity = new ItemFieldEntity();
            itemFieldEntity.setData((String) itemFieldRequestDto.getData());
            itemFieldEntity.setItem(item);
            itemFieldEntity.setFieldEntity(fieldEntity);
            itemFieldRepository.save(itemFieldEntity);
        }
        return new ApiResponse(1, "success", null);
    }



    public ApiResponse edit(Long userId, Long collectionId, Long itemId, ItemRequestDto itemRequestDto){
        checkCollectionForExist(collectionId);
        checkUserForExist(userId);
        ItemEntity item = checkItemForExist(itemId);

        item.setName((String) itemRequestDto.getFieldList().get(0).getData());

        List<TagEntity> tagList = new ArrayList<>();
        itemRequestDto.getTagList().forEach(tagRequestDto -> {
            TagEntity tag = checkTagForExist(tagRequestDto.getName());
            tagList.add(tag);
        });
        item.setTagList(tagList);


        List<FieldEntity> fieldEntities = fieldRepository.findByCollectionId(collectionId);
        for (int i = 0; i < fieldEntities.size(); i++){
            FieldEntity fieldEntity = fieldEntities.get(i);
            ItemFieldRequestDto itemFieldRequestDto = itemRequestDto.getFieldList().get(i + 1);

            Optional<ItemFieldEntity> optionalItemField = itemFieldRepository.findByFieldEntityId(fieldEntity.getId());
            ItemFieldEntity itemFieldEntity;
            if (!optionalItemField.isPresent()){
                itemFieldEntity = new ItemFieldEntity();
                itemFieldEntity.setItem(item);
                itemFieldEntity.setFieldEntity(fieldEntity);
            } else {
                itemFieldEntity = optionalItemField.get();
            }
            itemFieldEntity.setData((String) itemFieldRequestDto.getData());
            itemFieldRepository.save(itemFieldEntity);
        }
        return new ApiResponse(1, "success", null);
    }



    public ApiResponse delete(Long userId, Long collectionId, Long itemId) {
        UserEntity user = checkUserForExist(userId);
        CollectionEntity collection = checkCollectionForExist(collectionId);
        checkPermission(userId, user, collection, "delete");
        checkItemForExist(itemId);
        collection.setNumberOfItem(collection.getNumberOfItem() - 1);
        collectionRepository.save(collection);

        commentRepository.deleteAllByItemId(itemId);
        tagRepository.deleteAllByItemId(itemId);
        itemFieldRepository.deleteAllByItemId(itemId);
        itemRepository.deleteById(itemId);
        return new ApiResponse(1, "success", null);
    }




    public ApiResponse getById(Long collectionId, Long itemId){
        checkCollectionForExist(collectionId);
        ItemEntity item = checkItemForExist(itemId);
        List<ItemFieldEntity> itemFieldEntityList = new ArrayList<>();
        List<FieldEntity> fieldEntityList = fieldRepository.findByCollectionId(collectionId);
        fieldEntityList.forEach(fieldEntity -> {
            Optional<ItemFieldEntity> optionalItemField = itemFieldRepository.findByFieldEntityId(fieldEntity.getId());
            if (optionalItemField.isPresent()){
                itemFieldEntityList.add(optionalItemField.get());
            } else {
              itemFieldEntityList.add(new ItemFieldEntity(null, null, null, null));
            }
        });
        SingleItemResponseDto singleItemResponseDto = new SingleItemResponseDto(item, itemFieldEntityList, fieldEntityList);
        return new ApiResponse(1, "success", singleItemResponseDto);
    }

    public ApiResponse getAll(Long collectionId){
        checkCollectionForExist(collectionId);
        List<ItemEntity> itemEntities = itemRepository.findByCollectionId(collectionId);
        List<ItemsResponseDto> itemsResponseDtoList = new ArrayList<>();
        itemEntities.forEach(itemEntity -> {
            itemsResponseDtoList.add(modelMapper.map(itemEntity, ItemsResponseDto.class));
        });
        return new ApiResponse(1, "success", itemsResponseDtoList);
    }


    public ApiResponse getByTags(List<TagRequestDto> tagRequestDtos) {
        List<ItemEntity> itemEntities = itemRepository.findAll();
        List<Long> tagIdList = new ArrayList<>();
        tagRequestDtos.forEach(idRequestDto -> {
            TagEntity tag = checkTagForExist(idRequestDto.getName());
            tagIdList.add(tag.getId());
        });

        List<ItemsResponseDto> itemsResponseDtoList = new ArrayList<>();

        for (int i = 0; i < itemEntities.size(); i++){
            boolean ok = true;
            for (int j = 0; j < tagIdList.size(); j++){
                boolean ok2 = false;
                for (int k =  0; k < itemEntities.get(i).getTagList().size(); k++){
                    if (tagIdList.get(j).equals(itemEntities.get(i).getTagList().get(k).getId())){
                        ok2 = true;
                        break;
                    }
                }
                ok = ok & ok2;
            }
            if (ok){
                itemsResponseDtoList.add(modelMapper.map(itemEntities.get(i), ItemsResponseDto.class));
            }
        }
        return new ApiResponse(1, "success", itemsResponseDtoList);
    }
}
