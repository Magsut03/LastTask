package com.example.lasttask.service.item;

import com.example.lasttask.dto.request.item.TagRequestDto;
import com.example.lasttask.dto.response.ApiResponse;
import com.example.lasttask.exception.BadRequestException;
import com.example.lasttask.exception.NotFoundException;
import com.example.lasttask.model.entity.TagEntity;
import com.example.lasttask.model.entity.item.ItemEntity;
import com.example.lasttask.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.crypto.spec.OAEPParameterSpec;
import javax.swing.text.html.HTML;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TagService {
    private final CollectionRepository collectionRepository;
    private final ItemFieldRepository itemFieldRepository;
    private final CommentRepository commentRepository;
    private final FieldRepository fieldRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;


    private void checkTagForNotExist(String name){
        Optional<TagEntity> optionalTagEntity = tagRepository.findByName(name);
        if (optionalTagEntity.isPresent()){
            throw new BadRequestException("Tag is already exist with this Name: " + name);
        }
    }

    private TagEntity checkForExist(Long id){
        Optional<TagEntity> optionalTag = tagRepository.findById(id);
        if (!optionalTag.isPresent()){
            throw new NotFoundException("Tag not found with this Id: " + id);
        }
        return optionalTag.get();
    }

    public ApiResponse add(TagRequestDto tagRequestDto) {
        checkTagForNotExist(tagRequestDto.getName());
        TagEntity tag = modelMapper.map(tagRequestDto, TagEntity.class);
        tagRepository.save(tag);
        return new ApiResponse(1, "success", null);
    }

    public ApiResponse edit(Long tagId, TagRequestDto tagRequestDto) {
        TagEntity tag = checkForExist(tagId);
        tag.setName(tagRequestDto.getName());
        tagRepository.save(tag);
        return new ApiResponse(1, "success", null);
    }

    public ApiResponse delete(Long tagId){
        checkForExist(tagId);
//        List<ItemEntity> itemEntityList = tagRepository.findById(tagId).get().getItemEntityList();
//        itemEntityList.forEach(itemEntity -> {
//            for (int i = 0; i < itemEntity.getTagList().size(); i++)  {
//                TagEntity tagEntity = itemEntity.getTagList().get(i);
//                if (tagEntity.getId().equals(tagId)){
//                    itemEntity.getTagList().remove(i);
//                    break;
//                }
//            }
//        });
        tagRepository.deleteById(tagId);
        return new ApiResponse(1, "success", null);
    }

    public ApiResponse getAll(){
        return new ApiResponse(1, "success", tagRepository.findAll());
    }

}
