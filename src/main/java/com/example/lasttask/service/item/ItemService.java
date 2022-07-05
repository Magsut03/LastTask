package com.example.lasttask.service.item;

import com.example.lasttask.dto.request.item.ItemFieldRequestDto;
import com.example.lasttask.dto.request.item.ItemRequestDto;
import com.example.lasttask.dto.request.item.TagIdRequestDto;
import com.example.lasttask.dto.response.ApiResponse;
import com.example.lasttask.dto.response.item.ItemsResponseDto;
import com.example.lasttask.dto.response.item.SingleItemResponseDto;
import com.example.lasttask.model.entity.TagEntity;
import com.example.lasttask.model.entity.UserEntity;
import com.example.lasttask.model.entity.collection.CollectionEntity;
import com.example.lasttask.model.entity.collection.FieldEntity;
import com.example.lasttask.model.entity.item.ItemEntity;
import com.example.lasttask.model.entity.item.ItemFieldEntity;
import com.example.lasttask.repository.*;
import com.example.lasttask.service.CheckService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final CollectionRepository collectionRepository;
    private final ItemFieldRepository itemFieldRepository;
    private final CommentRepository commentRepository;
    private final FieldRepository fieldRepository;
    private final ItemRepository itemRepository;
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;
    private final CheckService checkService;









    public ApiResponse add(Long userId, Long collectionId, ItemRequestDto itemRequestDto){
        UserEntity user = checkService.checkUserForExist(userId);
        CollectionEntity collection = checkService.checkCollectionForExist(collectionId);
        checkService.checkPermission(userId, user, collection, "add");

        ItemEntity item = new ItemEntity();
        item.setName((String) itemRequestDto.getFieldList().get(0).getData());
        List<TagEntity> tagList = new ArrayList<>();
        itemRequestDto.getTagList().forEach(tagRequestDto -> {
            TagEntity tag = checkService.checkTagForExist(tagRequestDto.getId());
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
        checkService.checkCollectionForExist(collectionId);
        checkService.checkUserForExist(userId);
        ItemEntity item = checkService.checkItemForExist(itemId);

        item.setName((String) itemRequestDto.getFieldList().get(0).getData());

        List<TagEntity> tagList = new ArrayList<>();
        itemRequestDto.getTagList().forEach(tagRequestDto -> {
            TagEntity tag = checkService.checkTagForExist(tagRequestDto.getId());
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
        UserEntity user = checkService.checkUserForExist(userId);
        CollectionEntity collection = checkService.checkCollectionForExist(collectionId);
        checkService.checkPermission(userId, user, collection, "delete");
        checkService.checkItemForExist(itemId);
        collection.setNumberOfItem(collection.getNumberOfItem() - 1);
        collectionRepository.save(collection);

        commentRepository.deleteAllByItemId(itemId);
        tagRepository.deleteAllByItemId(itemId);
        itemFieldRepository.deleteAllByItemId(itemId);
        itemRepository.deleteById(itemId);
        return new ApiResponse(1, "success", null);
    }




    public ApiResponse getById(Long collectionId, Long itemId){
        checkService.checkCollectionForExist(collectionId);
        ItemEntity item = checkService.checkItemForExist(itemId);
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
        checkService.checkCollectionForExist(collectionId);
        List<ItemEntity> itemEntities = itemRepository.findByCollectionId(collectionId);
        List<ItemsResponseDto> itemsResponseDtoList = new ArrayList<>();
        itemEntities.forEach(itemEntity -> {
            itemsResponseDtoList.add(modelMapper.map(itemEntity, ItemsResponseDto.class));
        });
        return new ApiResponse(1, "success", itemsResponseDtoList);
    }


    public ApiResponse getByTags(List<TagIdRequestDto> tagIdRequestDtos) {
        List<ItemEntity> itemEntities = itemRepository.findAll();
        List<Long> tagIdList = new ArrayList<>();
        tagIdRequestDtos.forEach(idRequestDto -> {
            TagEntity tag = checkService.checkTagForExist(idRequestDto.getId());
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
