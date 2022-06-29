package com.example.lasttask.service.item;

import com.example.lasttask.dto.request.item.ItemFieldRequestDto;
import com.example.lasttask.dto.request.item.ListItemFieldRequestDto;
import com.example.lasttask.dto.request.item.TagRequestDto;
import com.example.lasttask.dto.response.ApiResponse;
import com.example.lasttask.dto.response.ItemResponseDto;
import com.example.lasttask.dto.response.user.UserResponseDto;
import com.example.lasttask.exception.BadRequestException;
import com.example.lasttask.exception.NotFoundException;
import com.example.lasttask.model.entity.TagEntity;
import com.example.lasttask.model.entity.UserEntity;
import com.example.lasttask.model.entity.collection.CollectionEntity;
import com.example.lasttask.model.entity.collection.FieldEntity;
import com.example.lasttask.model.entity.item.ItemEntity;
import com.example.lasttask.model.entity.item.ItemFieldEntity;
import com.example.lasttask.repository.*;
import lombok.RequiredArgsConstructor;
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

    private UserEntity chekUserForExist(Long userId){
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()){
            throw new NotFoundException("User not found with this Id: " + userId);
        }
        return optionalUser.get();
    }

    private CollectionEntity chekcCollectionForExist(Long collectionId){
        Optional<CollectionEntity> optionalCollection = collectionRepository.findById(collectionId);
        if (optionalCollection.isPresent()){
            throw new NotFoundException("Collection not found with this Id: " + collectionId);
        }
        return optionalCollection.get();
    }

    private void checkPermission(Long userId, Long collectionId, String custom){
        UserEntity user = collectionRepository.findById(collectionId).get().getUser();
        if (user.getId().equals(userId) || user.getRole().equals(ROLE_ADMIN)){
            throw new BadRequestException("you don't have permission to " + custom + " this collection!");
        }
    }


    public ApiResponse add(Long userId, Long collectionId, ListItemFieldRequestDto listItemFieldRequestDto){

        CollectionEntity collection = chekcCollectionForExist(collectionId);
        ItemEntity item = new ItemEntity();
        item.setCollection(collection);
        item.setName((String) listItemFieldRequestDto.getFieldList().get(0).getItemField());
        List<TagRequestDto> tagRequestDtos = listItemFieldRequestDto.getTagList();
        tagRequestDtos.forEach(tagRequestDto -> {
            TagEntity tag = new TagEntity();
            tag.setName(tagRequestDto.getName());
//            tag.setItem(item);
            tagRepository.save(tag);
        });
        collection.setNumberOfItem(collection.getNumberOfItem() + 1);
        collectionRepository.save(collection);
        itemRepository.save(item);

        List<FieldEntity> fieldEntities = fieldRepository.findByCollectionId(collectionId);
        for (int i = 0; i < fieldEntities.size(); i++){
            FieldEntity fieldEntity = fieldEntities.get(i);
            ItemFieldRequestDto itemFieldRequestDto = listItemFieldRequestDto.getFieldList().get(i + 1);
            ItemFieldEntity itemFieldEntity = new ItemFieldEntity();
            switch (fieldEntity.getType()){
                case 0:{
                    itemFieldEntity.setType0((Long) itemFieldRequestDto.getItemField());
                    break;
                }
                case 1:{
                    itemFieldEntity.setType1((String) itemFieldRequestDto.getItemField());
                    break;
                }
                case 2:{
                    itemFieldEntity.setType2((Boolean) itemFieldRequestDto.getItemField());
                    break;
                }
                case 3:{
                    itemFieldEntity.setType3((Date) itemFieldRequestDto.getItemField());
                    break;
                }
            }
            itemFieldEntity.setItem(item);
            itemFieldEntity.setFieldEntity(fieldEntity);
            itemFieldRepository.save(itemFieldEntity);
        }
        return new ApiResponse(1, "success", null);
    }



    public ApiResponse edit(Long userId, Long collectionId, Long itemId, ListItemFieldRequestDto listItemFieldRequestDto){

        Optional<CollectionEntity> optionalCollection = collectionRepository.findById(collectionId);
        if (!optionalCollection.isPresent()){
            throw new BadRequestException("collection not found with this Id: " + collectionId);
        }

        Optional<ItemEntity> optionalItem = itemRepository.findById(itemId);
        if (!optionalItem.isPresent()){
            throw new NotFoundException("Item not found with this Id: " + itemId);
        }

        CollectionEntity collection = optionalCollection.get();
        ItemEntity item = optionalItem.get();

        item.setName((String) listItemFieldRequestDto.getFieldList().get(0).getItemField());
        tagRepository.deleteAllByItemId(itemId);
        List<TagRequestDto> tagRequestDtos = listItemFieldRequestDto.getTagList();
        tagRequestDtos.forEach(tagRequestDto -> {
            TagEntity tag = new TagEntity();
            tag.setName(tagRequestDto.getName());
//            tag.setItem(item);
            tagRepository.save(tag);
        });

        List<FieldEntity> fieldEntities = fieldRepository.findByCollectionId(collectionId);
        for (int i = 0; i < fieldEntities.size(); i++){
            FieldEntity fieldEntity = fieldEntities.get(i);
            ItemFieldRequestDto itemFieldRequestDto = listItemFieldRequestDto.getFieldList().get(i + 1);
            Optional<ItemFieldEntity> optionalItemField = itemFieldRepository.findByFieldEntityId(fieldEntity.getId());
            if (!optionalItemField.isPresent()){
                throw new NotFoundException("Item Field not found with this Id: " + fieldEntity.getId());
            }
            ItemFieldEntity itemFieldEntity = optionalItemField.get();
            switch (fieldEntity.getType()){
                case 0:{
                    itemFieldEntity.setType0((Long) itemFieldRequestDto.getItemField());
                    break;
                }
                case 1:{
                    itemFieldEntity.setType1((String) itemFieldRequestDto.getItemField());
                    break;
                }
                case 2:{
                    itemFieldEntity.setType2((Boolean) itemFieldRequestDto.getItemField());
                    break;
                }
                case 3:{
                    itemFieldEntity.setType3((Date) itemFieldRequestDto.getItemField());
                    break;
                }
            }
            itemFieldRepository.save(itemFieldEntity);
        }
        return new ApiResponse(1, "success", null);
    }



    public ApiResponse delete(Long userId, Long collectionId, Long itemId) {
        Optional<ItemEntity> optionalItem = itemRepository.findById(itemId);
        if (!optionalItem.isPresent()){
            throw new NotFoundException("Item not found with this Id: " + itemId);
        }

        tagRepository.deleteAllByItemId(itemId);
        itemFieldRepository.deleteAllByItemId(itemId);
        commentRepository.deleteAllByItemId(itemId);
        itemRepository.deleteById(itemId);
        return new ApiResponse(1, "success", null);
    }




    public ApiResponse getById(Long collectionId, Long itemId){
        Optional<ItemEntity> optionalItemEntity = itemRepository.findById(itemId);
        if (!optionalItemEntity.isPresent()){
            throw new BadRequestException("item not found with this Id: " + itemId);
        }
        ItemEntity item = optionalItemEntity.get();
        List<TagEntity> tags = tagRepository.findByItemId(itemId);
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
        ItemResponseDto itemResponseDto = new ItemResponseDto(item, tags, itemFieldEntityList, fieldEntityList);
        return new ApiResponse(1, "success", itemResponseDto);
    }



    public ApiResponse getAll(Long collectionId){
        Optional<CollectionEntity> optionalCollection = collectionRepository.findById(collectionId);
        if (!optionalCollection.isPresent()) {
            throw new BadRequestException("collection not found with this Id: " + collectionId);
        }
        List<ItemEntity> itemEntities = itemRepository.findByCollectionId(collectionId);
        return new ApiResponse(1, "success", itemEntities);
    }

}
