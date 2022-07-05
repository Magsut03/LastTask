package com.example.lasttask.service.item;

import com.example.lasttask.dto.request.item.TagRequestDto;
import com.example.lasttask.dto.response.ApiResponse;
import com.example.lasttask.exception.BadRequestException;
import com.example.lasttask.exception.NotFoundException;
import com.example.lasttask.model.entity.TagEntity;
import com.example.lasttask.model.entity.item.ItemEntity;
import com.example.lasttask.repository.*;
import com.example.lasttask.service.CheckService;
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
    private final CheckService checkService;




    public ApiResponse add(TagRequestDto tagRequestDto) {
        checkService.checkTagForNotExist(tagRequestDto.getName());
        TagEntity tag = modelMapper.map(tagRequestDto, TagEntity.class);
        tagRepository.save(tag);
        return new ApiResponse(1, "success", null);
    }

    public ApiResponse edit(Long tagId, TagRequestDto tagRequestDto) {
        TagEntity tag = checkService.checkTagForExist(tagId);
        tag.setName(tagRequestDto.getName());
        tagRepository.save(tag);
        return new ApiResponse(1, "success", null);
    }

    public ApiResponse delete(Long tagId){
        checkService.checkTagForExist(tagId);
        tagRepository.deleteAllByTagId(tagId);
        tagRepository.deleteById(tagId);
        return new ApiResponse(1, "success", null);
    }

    public ApiResponse getAll(){
        return new ApiResponse(1, "success", tagRepository.findAll());
    }

}
